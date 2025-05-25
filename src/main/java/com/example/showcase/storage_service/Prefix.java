package com.example.showcase.storage_service;

public enum Prefix {
    USER("user/images"),
    PROJECT_MAIN_IMAGE("project/main_image"),
    PROJECT_OTHER_SCREENSHOTS("project/screenshots");

    private final String value;

    Prefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}