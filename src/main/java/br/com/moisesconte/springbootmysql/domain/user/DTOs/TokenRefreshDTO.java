package br.com.moisesconte.springbootmysql.domain.user.DTOs;

import jakarta.validation.constraints.NotEmpty;

public class TokenRefreshDTO {

  @NotEmpty(message = "RefreshToken is required!")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
