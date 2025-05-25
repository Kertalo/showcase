package com.example.showcase.primary_filling;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class SummaryTableDTO {

    @CsvBindByName(column = "Фамилия")
    private String surName;

    @CsvBindByName(column = "Имя")
    private String firstName;

    @CsvBindByName(column = "Отчество")
    private String secondName;

    @CsvBindByName(column = "Курс")
    private String course;

    @CsvBindByName(column = "Группа")
    private String group;

    @CsvBindByName(column = "Команда")
    private String team;

    @CsvBindByName(column = "Примечание")
    private String comment;

    @CsvBindByName(column = "Итого")
    private Integer grade;

}
