package com.cloudnest.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource) {
        super(resource + " not found.");
    }

    public ResourceNotFoundException(
            String resource,
            Object identifier
    ) {
        super(resource + " '" + identifier + "' not found.");
    }

}