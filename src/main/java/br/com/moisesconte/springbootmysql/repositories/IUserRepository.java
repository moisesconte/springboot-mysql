package br.com.moisesconte.springbootmysql.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.moisesconte.springbootmysql.domain.user.models.UserModel;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  UserDetails findByLogin(String login);
}
