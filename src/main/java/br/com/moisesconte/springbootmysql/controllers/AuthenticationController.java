package br.com.moisesconte.springbootmysql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moisesconte.springbootmysql.domain.user.AuthenticationDTO;
import br.com.moisesconte.springbootmysql.domain.user.LoginResponseDTO;
import br.com.moisesconte.springbootmysql.domain.user.RegisterDTO;
import br.com.moisesconte.springbootmysql.domain.user.UserModel;
import br.com.moisesconte.springbootmysql.infra.security.TokenService;
import br.com.moisesconte.springbootmysql.repositories.IUserRepository;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private TokenService tokenService;

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody AuthenticationDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = this.tokenService.generateToken((UserModel) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody RegisterDTO data) {
    if (this.userRepository.findByLogin(data.login()) != null)
      return ResponseEntity.badRequest().build();

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    UserModel newUser = new UserModel(data.login(), encryptedPassword, data.role());

    this.userRepository.save(newUser);

    return ResponseEntity.ok().build();
  }
}
