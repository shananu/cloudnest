package com.cloudnest.trash.service;

import com.cloudnest.trash.dto.TrashResponse;
import org.springframework.security.core.Authentication;

public interface TrashService {

    TrashResponse getTrash(Authentication authentication);

}