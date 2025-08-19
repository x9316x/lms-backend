package com.example.lms.auth;

import com.example.lms.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final Key key;
    private final long accessTtlMinutes;
    private final long refreshTtlDays;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.accessTtlMinutes}") long accessTtlMinutes,
            @Value("${app.jwt.refreshTtlDays}") long refreshTtlDays
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlMinutes = accessTtlMinutes;
        this.refreshTtlDays = refreshTtlDays;
    }

    public String generateAccessToken(User u) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTtlMinutes * 60);
        return Jwts.builder()
                .setSubject(u.getEmail())
                .addClaims(Map.of("role", u.getRole().name(), "type", "access"))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User u) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshTtlDays * 24 * 60 * 60);
        return Jwts.builder()
                .setSubject(u.getEmail())
                .addClaims(Map.of("role", u.getRole().name(), "type", "refresh"))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isRefreshToken(Jws<Claims> jws) {
        Object type = jws.getBody().get("type");
        return type != null && "refresh".equals(type.toString());
    }

    public String email(Jws<Claims> jws) {
        return jws.getBody().getSubject();
    }

    public String role(Jws<Claims> jws) {
        Object r = jws.getBody().get("role");
        return r == null ? null : r.toString();
    }
}
