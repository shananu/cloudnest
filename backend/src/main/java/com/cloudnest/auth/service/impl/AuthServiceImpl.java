package com.cloudnest.auth.service.impl;

import com.cloudnest.auth.dto.request.LoginRequest;
import com.cloudnest.auth.dto.request.RegisterRequest;
import com.cloudnest.auth.dto.response.AuthResponse;
import com.cloudnest.auth.service.AuthService;
import com.cloudnest.exception.EmailAlreadyExistsException;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.mapper.UserMapper;
import com.cloudnest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = userMapper.toEntity(request);

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(null)
                .refreshToken(null)
                .expiresIn(0L)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Login will be implemented in PR-2");
    }
}