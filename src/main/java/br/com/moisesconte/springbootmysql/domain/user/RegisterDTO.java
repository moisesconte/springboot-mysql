package br.com.moisesconte.springbootmysql.domain.user;

public record RegisterDTO(String login, String password, UserRole role) {

}
