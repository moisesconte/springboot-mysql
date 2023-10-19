package br.com.moisesconte.springbootmysql.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import br.com.moisesconte.springbootmysql.domain.user.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.domain.user.UserModel;
import java.util.Optional;


@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
  // Optional<RefreshTokenModel> findByToken(UUID token);
  Optional<RefreshTokenModel> findByToken(String token);

  @Modifying
  String deleteByUserId(UserModel user);
}
