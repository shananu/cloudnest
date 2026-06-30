package com.cloudnest.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShareTokenGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

}