package com.video.app.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generate(UserDetails userDetails, Map<String,String> claims);
    String generate(UserDetails userDetails);
    boolean isTokenValid(String token);
    Claims extractAllClaims(String token);
    <T> T extractClaims(String token, Function<Claims,T> claimResolver);
    String extractUsername(String token);
}
