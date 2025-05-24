package com.example.showcase.storage_service;

import java.util.UUID;


public class FileNameGenerator {

    /**
     * Генерация имени файла для бакета
     * @param prefix
     * @param title
     * @return
     */
    public static String generateFileName(Prefix prefix,String title) {
        return String.format("%s_%s_%s.png", prefix.getValue(), title, UUID.randomUUID());
    }
}
