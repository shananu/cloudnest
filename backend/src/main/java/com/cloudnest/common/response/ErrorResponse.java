package com.cloudnest.common.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String message;
    private List<String> errors;

}