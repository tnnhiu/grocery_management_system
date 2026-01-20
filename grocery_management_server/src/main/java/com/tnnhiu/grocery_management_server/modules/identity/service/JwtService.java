package com.tnnhiu.grocery_management_server.modules.identity.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.tnnhiu.grocery_management_server.common.exception.AppException;
import com.tnnhiu.grocery_management_server.modules.identity.entity.InvalidatedToken;
import com.tnnhiu.grocery_management_server.modules.identity.entity.User;
import com.tnnhiu.grocery_management_server.modules.identity.enums.TokenTypeEnum;
import com.tnnhiu.grocery_management_server.modules.identity.exception.IdentityErrorCode;
import com.tnnhiu.grocery_management_server.modules.identity.repository.InvalidatedTokenRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    @Value("${jwt.secret-key}")
    String secretKey;

    @Value("${jwt.access-token-expiration}")
    long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    long refreshTokenExpiration;

    final InvalidatedTokenRepository invalidatedTokenRepository;

    JwtDecoder jwtDecoder;
    JwtEncoder jwtEncoder;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HS512");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(keyBytes));
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, TokenTypeEnum.ACCESS);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, TokenTypeEnum.REFRESH);
    }

    private String generateToken(User user, long expiration, TokenTypeEnum type) {
        Instant now = Instant.now();
        Instant validity = now.plus(expiration, ChronoUnit.SECONDS);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("type", type.name());

        if (TokenTypeEnum.ACCESS.equals(type)) {
            claimsBuilder.claim("userId", user.getId())
                    .claim("name", user.getName())
                    .claim("role", user.getRole().getName().name());
        }

        return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claimsBuilder.build()))
                .getTokenValue();
    }

    public Jwt verifyToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            
            if (jwt.getSubject() == null) {
                throw new AppException(IdentityErrorCode.JWT_INVALID);
            }

            if (invalidatedTokenRepository.existsById(jwt.getId())) {
                throw new AppException(IdentityErrorCode.JWT_INVALID);
            }
            return jwt;
        } catch (JwtException e) {
            throw new AppException(IdentityErrorCode.JWT_INVALID);
        }
    }

    public void invalidateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            long ttl = ChronoUnit.SECONDS.between(Instant.now(), jwt.getExpiresAt());
            if (ttl > 0) {
                InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                        .jwtId(jwt.getId())
                        .ttl(ttl)
                        .build();
                invalidatedTokenRepository.save(invalidatedToken);
            }
        } catch (JwtException e) {
            // Token is already invalid or expired
        }
    }
}
