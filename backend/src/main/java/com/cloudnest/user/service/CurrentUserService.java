package com.cloudnest.user.service;

import com.cloudnest.user.entity.User;
import org.springframework.security.core.Authentication;

public interface CurrentUserService {

    User getCurrentUser(Authentication authentication);

}