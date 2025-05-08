package com.example.showcase.utils.primary_filling;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;


@Data
public class ScoreTableDTO {

    @CsvBindByName(column = "Название команды")
    private String name;

    @CsvBindByName(column = "Итого")
    private Integer grade;
}
