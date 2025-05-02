package com.example.showcase.primary_filling;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class AnnotationTableDTO {

    @CsvBindByName(column = "Название проекта")
    private String name;

    @CsvBindByName(column = "Тип проекта")
    private String tag;

    @CsvBindByName(column = "Состав команды")
    private String team;

    @CsvBindByName(column = "ФИО тимлида")
    private String lead;

    @CsvBindByName(column = "Аннотация к проекту")
    private String description;

    @CsvBindByName(column = "Цель проекта")
    private String goal;

    @CsvBindByName(column = "Задачи проекта")
    private String tasks;

    @CsvBindByName(column = "Репозиторий")
    private String repository;
}
