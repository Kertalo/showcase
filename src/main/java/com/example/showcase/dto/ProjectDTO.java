package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
//TODO
public class ProjectDTO {
    private Integer id; // автогенерация или инкремент
    private Integer trackId; // Бакалавриат - 1 ???
    private String title; //Название команды
    private String description; // Анн Аннотация к проекту
    private Integer grade; // Общ Итого оценка
    private String repo; // Анн Репозиторий
    private MultipartFile[] screenshots; // ---
    private String presentation; // ---
    private List<Integer> tagsId; // Анн Тип проекта
    private List<Integer> usersId; // ключ на UserDTO???
    private String date; // 2023-2024 (а.г)
    private MultipartFile mainScreenshot; // ---
}
