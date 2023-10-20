package br.com.moisesconte.springbootmysql.domain.user.DTOs;

import br.com.moisesconte.springbootmysql.domain.user.models.UserRoleModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// public record RegisterDTO(String name, String login, String password, UserRole role) {

// }

@Data
@AllArgsConstructor
public class RegisterDTO {

  @NotBlank(message = "Name is required!")
  private String name;

  @NotBlank(message = "Login is required!")
  private String login;

  @NotBlank(message = "Password is required!")
  private String password;

  private UserRoleModel role = UserRoleModel.USER;
}