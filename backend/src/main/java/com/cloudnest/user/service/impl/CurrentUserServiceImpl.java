package com.cloudnest.user.service.impl;

import com.cloudnest.common.exception.ResourceNotFoundException;
import com.cloudnest.user.entity.User;
import com.cloudnest.user.repository.UserRepository;
import com.cloudnest.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser(Authentication authentication) {

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User"));
    }
}