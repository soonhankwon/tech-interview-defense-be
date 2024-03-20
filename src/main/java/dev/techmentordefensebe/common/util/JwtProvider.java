package dev.techmentordefensebe.common.util;

import dev.techmentordefensebe.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token-expiration-minutes}")
    private long accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-minutes}")
    private long refreshTokenExpirationMinutes;

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        Map<String, Object> claims = createClaims(user);
        return Jwts.builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getSecretKey())
                .compact();
    }

    public String createRefreshToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshTokenExpirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, Object> createClaims(User user) {
        return Map.of(
                "id", user.getId(),
                "email", user.getEmail()
        );
    }
}
