package project.dealdrop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import project.dealdrop.entity.User;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token) {
        System.out.println("Issued at : " + new Date(System.currentTimeMillis()));
        System.out.println("Expires at: " + new Date(System.currentTimeMillis() + jwtExpiration));
        Claims claim = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claim.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claim.getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String email = getUsernameFromToken(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
