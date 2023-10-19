package br.com.moisesconte.springbootmysql.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import br.com.moisesconte.springbootmysql.domain.user.models.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.domain.user.models.UserModel;

import java.util.Optional;
import java.util.List;



@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshTokenModel, UUID> {
  // Optional<RefreshTokenModel> findByToken(UUID token);
  Optional<RefreshTokenModel> findByToken(String token);
  
  List<RefreshTokenModel> findByUser(UserModel user);

  @Modifying
  String deleteByUserId(UserModel user);
}
