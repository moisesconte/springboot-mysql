package br.com.moisesconte.springbootmysql.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moisesconte.springbootmysql.domain.user.AuthenticationDTO;
import br.com.moisesconte.springbootmysql.domain.user.LoginResponseDTO;
import br.com.moisesconte.springbootmysql.domain.user.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.domain.user.RegisterDTO;
import br.com.moisesconte.springbootmysql.domain.user.UserModel;
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
  public ResponseEntity login(@RequestBody AuthenticationDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = this.tokenService.generateToken((UserModel) auth.getPrincipal());

    UserModel user = (UserModel) this.userRepository.findByLogin(data.login());
    RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(user.getId());

    return ResponseEntity.ok(new LoginResponseDTO(token, refreshToken.getToken().toString()));
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody RegisterDTO data) {
    if (this.userRepository.findByLogin(data.login()) != null)
      return ResponseEntity.badRequest().build();

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    UserModel newUser = new UserModel(data.name(), data.login(), encryptedPassword, data.role());

    System.out.println(newUser.getRole());

    this.userRepository.save(newUser);

    return ResponseEntity.ok().build();
  }
}
