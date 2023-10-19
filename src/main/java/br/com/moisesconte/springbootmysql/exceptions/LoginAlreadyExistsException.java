package br.com.moisesconte.springbootmysql.exceptions;

public class LoginAlreadyExistsException {
  private String message = "Login já em uso.";

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
