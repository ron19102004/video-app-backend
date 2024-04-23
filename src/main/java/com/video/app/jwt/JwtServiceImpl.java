package com.video.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private final String KEY_SECRET = "Cc6tJXABIioNn5fSRTfhTcPWky0lFnSP7eZL2pS9b4upvEE7oT7Qn11KHyVxXt0I\n";
    private final long EXPIRATION_TIME_MS = 3600 * 1000 * 2;

    @Override
    public String generate(UserDetails userDetails, Map<String, String> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(this.getSingingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generate(UserDetails userDetails) {
        return this.generate(userDetails, new HashMap<>());
    }

    @Override
    public boolean isTokenValid(String token) {
        Date expiration = extractClaims(token, Claims::getExpiration);
        return !isExpired(expiration);
    }

    private boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSingingKey() {
        byte[] keyByte = Decoders.BASE64.decode(this.KEY_SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = this.extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    @Override
    public String extractUsername(String token) {
        return this.extractClaims(token, Claims::getSubject);
    }
}
