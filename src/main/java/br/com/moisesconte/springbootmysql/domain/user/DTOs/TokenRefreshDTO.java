package br.com.moisesconte.springbootmysql.domain.user.DTOs;

public class TokenRefreshDTO {
  
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
