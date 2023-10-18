package br.com.moisesconte.springbootmysql.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import br.com.moisesconte.springbootmysql.domain.user.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.domain.user.UserModel;

public interface IRefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
  Optional<RefreshTokenModel> findByToken(UUID token);

  @Modifying
  String deleteByUserId(UserModel user);
}