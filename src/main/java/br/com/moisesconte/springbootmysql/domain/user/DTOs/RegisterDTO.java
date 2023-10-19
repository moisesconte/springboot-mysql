package br.com.moisesconte.springbootmysql.domain.user.DTOs;

import br.com.moisesconte.springbootmysql.domain.user.models.UserRoleModel;

// public record RegisterDTO(String name, String login, String password, UserRole role) {

// }

public class RegisterDTO {

  private String name;
  private String login;
  private String password;
  private UserRoleModel role;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRoleModel getRole() {
    return role;
  }

  public void setRole(UserRoleModel role) {
    this.role = role;
  }

}