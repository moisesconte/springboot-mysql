package br.com.moisesconte.springbootmysql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moisesconte.springbootmysql.domain.user.UserModel;
import br.com.moisesconte.springbootmysql.repositories.IUserRepository;


@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<?> users(@RequestBody UserModel userModel) {

    var user = this.userRepository.save(userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }
}
