package br.com.moisesconte.springbootmysql.domain.user;

public record RegisterDTO(String name, String login, String password, UserRole role) {

}
