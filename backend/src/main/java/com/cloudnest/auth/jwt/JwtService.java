package com.cloudnest.auth.jwt;

import com.cloudnest.user.entity.User;

public interface JwtService {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, User user);
}