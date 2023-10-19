package br.com.moisesconte.springbootmysql.domain.user;

// public record RegisterDTO(String name, String login, String password, UserRole role) {

// }

public class RegisterRequestDTO {

  private String name;
  private String login;
  private String password;
  private UserRole role;

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

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

}