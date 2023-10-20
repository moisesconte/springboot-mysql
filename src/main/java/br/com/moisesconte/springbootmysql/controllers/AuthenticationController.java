package br.com.moisesconte.springbootmysql.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
import jakarta.validation.Valid;

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
  public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO data) {

    var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
    var auth = this.authenticationManager.authenticate(usernamePassword);
    var token = this.tokenService.generateToken((UserModel) auth.getPrincipal());

    UserModel user = (UserModel) this.userRepository.findByLogin(data.getLogin());
    RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(user.getId());

    return ResponseEntity.ok(new LoginResponseDTO(token, refreshToken.getToken().toString()));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerRequest) {

    if (this.userRepository.findByLogin(registerRequest.getLogin()) != null) {
      LoginAlreadyExistsException loginAlreadyExistsException = new LoginAlreadyExistsException();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginAlreadyExistsException);
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.getPassword());
    UserModel newUser = new UserModel(
        registerRequest.getName(),
        registerRequest.getLogin(),
        encryptedPassword,
        registerRequest.getRole());

    this.userRepository.save(newUser);

    return ResponseEntity.ok().body(null);
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshDTO request) {
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

  // Responsável pela formatação e retorno da validação da requisição.
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}
