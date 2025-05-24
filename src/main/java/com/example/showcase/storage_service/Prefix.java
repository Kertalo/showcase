package com.example.showcase.storage_service;

public enum Prefix {
    USER("user_images"),
    PROJECT("project_images");

    private final String value;

    Prefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}