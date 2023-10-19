package br.com.moisesconte.springbootmysql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moisesconte.springbootmysql.domain.user.DTOs.AuthenticationDTO;
import br.com.moisesconte.springbootmysql.domain.user.DTOs.LoginResponseDTO;
import br.com.moisesconte.springbootmysql.domain.user.DTOs.RegisterDTO;
import br.com.moisesconte.springbootmysql.domain.user.DTOs.TokenRefreshDTO;
import br.com.moisesconte.springbootmysql.domain.user.DTOs.TokenRefreshResponseDTO;
import br.com.moisesconte.springbootmysql.domain.user.models.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.domain.user.models.UserModel;
import br.com.moisesconte.springbootmysql.exceptions.LoginAlreadyExistsException;
import br.com.moisesconte.springbootmysql.exceptions.TokenRefreshException;
import br.com.moisesconte.springbootmysql.infra.security.TokenService;
import br.com.moisesconte.springbootmysql.repositories.IUserRepository;
import br.com.moisesconte.springbootmysql.services.RefreshTokenService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody AuthenticationDTO data) {

    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);
    var token = this.tokenService.generateToken((UserModel) auth.getPrincipal());

    UserModel user = (UserModel) this.userRepository.findByLogin(data.login());
    RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(user.getId());

    return ResponseEntity.ok(new LoginResponseDTO(token, refreshToken.getToken().toString()));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Validated @RequestBody RegisterDTO registerRequest) {

    if (this.userRepository.findByLogin(registerRequest.getLogin()) != null) {
      LoginAlreadyExistsException loginAlreadyExistsException = new LoginAlreadyExistsException();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginAlreadyExistsException);
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.getPassword());
    UserModel newUser = new UserModel(registerRequest.getName(), registerRequest.getLogin(), encryptedPassword,
        registerRequest.getRole());

    this.userRepository.save(newUser);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshToken(@Validated @RequestBody TokenRefreshDTO request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshTokenModel::getUser)
        .map(user -> {
          String token = tokenService.generateToken(user);
          var refreshTokenModel = refreshTokenService.createRefreshToken(user.getId());
          return ResponseEntity.ok(new TokenRefreshResponseDTO(token, refreshTokenModel.getToken()));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

  }

  
}
