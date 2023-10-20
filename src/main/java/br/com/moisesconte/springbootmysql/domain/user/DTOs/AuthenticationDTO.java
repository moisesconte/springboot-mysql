package br.com.moisesconte.springbootmysql.domain.user.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

// public record AuthenticationDTO(String login, String password) { }

@Data
public class AuthenticationDTO {

  @NotEmpty(message = "Login is required!")
  private String login;

  @NotEmpty(message = "Password is required!")
  private String password;
  
}
