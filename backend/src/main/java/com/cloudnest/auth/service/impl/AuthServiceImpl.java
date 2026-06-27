package com.cloudnest.auth.service.impl;

import com.cloudnest.auth.dto.request.LoginRequest;
import com.cloudnest.auth.dto.request.RegisterRequest;
import com.cloudnest.auth.dto.response.AuthResponse;
import com.cloudnest.auth.service.AuthService;
import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.exception.EmailAlreadyExistsException;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.mapper.UserMapper;
import com.cloudnest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cloudnest.auth.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User"));

    String accessToken = jwtService.generateAccessToken(user);

    String refreshToken = jwtService.generateRefreshToken(user);

    return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(900L)
            .tokenType("Bearer")
            .build();
}
}