package br.com.moisesconte.springbootmysql.domain.user.DTOs;

public record LoginResponseDTO(String token, String refreshToken) {

}
