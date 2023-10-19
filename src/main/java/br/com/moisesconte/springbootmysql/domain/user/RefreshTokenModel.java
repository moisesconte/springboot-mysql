package br.com.moisesconte.springbootmysql.domain.user;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity(name = "refresh_token")

public class RefreshTokenModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(Types.VARCHAR)
  @Column(columnDefinition = "VARCHAR(200)")
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserModel user;

  @Column(columnDefinition = "VARCHAR(200)", nullable = false, unique = true)
  @JdbcTypeCode(Types.VARCHAR)
  private String token;

  private Instant expiryDate;
  
}
