package br.com.moisesconte.springbootmysql.domain.user.models;

public enum UserRoleModel {
  ADMIN("ADMIN"),
  USER("USER");

  private String role;

  UserRoleModel(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
