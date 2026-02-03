package com.skillLink.skillLink.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private  String SECRET;

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) //  include custom claims
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
                .signWith(getKey())
                .compact();
    }

    public String generateToken(Map<String, Object>claims,String subject) {
        return createToken(claims, subject);
    }

    public String extractEmail(String token) {
        return  extractClaims(token, Claims::getSubject);
    }

    private boolean isTokenExpiration(String token) {
        return  extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return (Date) extractClaims(token,Claims::getExpiration);
    }

    private  <T>T extractClaims(String token, Function<Claims, T> ClaimResolver ) {
        final Claims claims = extractAllClaims(token);
        return ClaimResolver.apply(claims);

    }
    public  boolean validateToken(String token, UserDetails userDetails)  {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }
}
