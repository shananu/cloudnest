package com.cloudnest.storage.model;

public record StoredFile(
        String storedFilename,
        String storageKey
) {
}