package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrimaryFillingMapper {
    PrimaryFillingMapper INSTANCE = Mappers.getMapper(PrimaryFillingMapper.class);


    @Mapping(target = "trackId", constant = "1") //Бакалавриат
    @Mapping(target = "title", source = "annotation.name")
    @Mapping(target = "description", source = "annotation.description")
    @Mapping(target = "repo", source = "annotation.repository")
    @Mapping(target = "tagsId", source = "annotation.tag", qualifiedByName = "setTags")
    @Mapping(target = "dateId", constant = "2")
        //для команд 2023-2024
    ProjectDTO mapToProjectDTO(AnnotationTableDTO annotation, Integer grade); //Grade из другой таблицы

    @Named("setTags")
    default List<Integer> setTags(String tags) {
//        { "id": 1, "name": "Web-приложение" },
//        { "id": 2, "name": "Desktop-приложение" },
//        { "id": 3, "name": "Мобильное приложение" },
//        { "id": 4, "name": "Игра" },
//        { "id": 5, "name": "Мод" }
        Map<String, Integer> dictionary = Map.of(
                "Web-приложение", 1,
                "Desktop-приложение", 2,
                "Мобильное приложение", 3,
                "Игра", 4,
                "Мод", 5);
        Pattern pattern = Pattern.compile("\\s+и\\s+");
        return Arrays.stream(pattern.split(tags)).map(String::trim).filter(dictionary::containsKey).map(dictionary::get).toList();
    }

    @Mapping(target = "fullName", source = "summary", qualifiedByName = "concatName")
    @Mapping(target = "roleId", source = "summary.comment", qualifiedByName = "setRole")
    UserDTO mapToUserDTO(SummaryTableDTO summary);

    @Named("concatName")
    default String concatName(SummaryTableDTO summaryTableDTO) {
        return String.format("%s %s %s",
                summaryTableDTO.getSurName(),
                summaryTableDTO.getFirstName(),
                summaryTableDTO.getSecondName());
    }

    @Named("setRole")
    default Integer setRole(String comment) {
//        { "id": 1, "name": "Участник"},
//        { "id": 2, "name": "Админ"},
//        { "id": 3, "name": "Староста"},
//        { "id": 4, "name": "СуперАдмин"}
        return comment.contains("Староста") ? 3 : 1;
    }
}