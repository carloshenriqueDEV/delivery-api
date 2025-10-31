package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    //Todo: externalizar secret e expiration em application.properties
    @Value("${jwt.secret:kdadlfewlkjgiofgjlkgjwerçfljfdlkdfjgkljeiogfjdsfklgjeoh}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}")
    private long expiration;

  
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Usuario) {
            Usuario usuario = (Usuario) userDetails;
            if (usuario.getId() != null) claims.put("userId", usuario.getId());
            claims.put("role", usuario.getRole().name());
            claims.put("nome", usuario.getNome());
            if (usuario.getRestauranteId() != null) claims.put("restauranteId", usuario.getRestauranteId());
        }
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object v = claims.get("userId");
        if (v == null) return null;
        if (v instanceof Number) {
            return ((Number) v).longValue();
        }
        try {
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        Object v = claims.get("role");
        return v != null ? v.toString() : null;
    }

    public String extractNome(String token) {
        Claims claims = extractAllClaims(token);
        Object v = claims.get("nome");
        return v != null ? v.toString() : null;
    }

    public Long extractRestauranteId(String token) {
        Claims claims = extractAllClaims(token);
        Object v = claims.get("restauranteId");
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).longValue();
        try {
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Long getExpirationInMillis() {
        return expiration;
    }
}
