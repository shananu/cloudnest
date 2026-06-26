package com.cloudnest.auth.jwt;

public interface JwtService {

    String generateAccessToken(String email);

    String generateRefreshToken(String email);

    String extractUsername(String token);

    boolean isTokenValid(String token);

}