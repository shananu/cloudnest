package com.cloudnest.common.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resource) {
        super(resource + " already exists.");
    }

    public DuplicateResourceException(
            String resource,
            Object identifier
    ) {
        super(resource + " '" + identifier + "' already exists.");
    }

}