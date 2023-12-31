package br.com.moisesconte.springbootmysql.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.moisesconte.springbootmysql.domain.user.models.RefreshTokenModel;
import br.com.moisesconte.springbootmysql.repositories.IRefreshTokenRepository;
import br.com.moisesconte.springbootmysql.repositories.IUserRepository;

@Service
public class RefreshTokenService {
  @Value("${api.security.jwtRefreshExpirationMs}")
  private Long refreshTokenDurantionMs;

  @Autowired
  private IRefreshTokenRepository refreshTokenRepository;

  @Autowired
  private IUserRepository userRepository;

  public Optional<RefreshTokenModel> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshTokenModel createRefreshToken(UUID userId) {

    for (RefreshTokenModel refreshTokenStorage : refreshTokenRepository
        .findByUser(userRepository.findById(userId).get())) {
      refreshTokenRepository.deleteById(refreshTokenStorage.getId());
    }

    RefreshTokenModel refreshToken = new RefreshTokenModel();

    refreshToken.setToken(userId.toString());
    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurantionMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);

    return refreshToken;
  }

  public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new JWTVerificationException("Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public String deleteByUserId(String userId) {
    return refreshTokenRepository.deleteByUserId(userRepository.findById(UUID.fromString(userId)).get());
  }

}
