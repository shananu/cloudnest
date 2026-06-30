package com.cloudnest.auth.service;

import com.cloudnest.auth.dto.request.LoginRequest;
import com.cloudnest.auth.dto.request.RegisterRequest;
import com.cloudnest.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}