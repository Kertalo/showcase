package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Map;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrimaryFillingMapper {
    PrimaryFillingMapper INSTANCE = Mappers.getMapper(PrimaryFillingMapper.class);


    @Mapping(target ="trackId", constant = "1") //Бакалавриат 2022-2023 = 1
    @Mapping(target ="title",source = "annotation.name")
    @Mapping(target ="description",source ="annotation.description")
    @Mapping(target ="repo",source = "annotation.repository")
    @Mapping(target ="tagsId",source="annotation.tag",qualifiedByName="setTags")
    //@Mapping(target ="usersId") как правильно связать ???
    @Mapping(target ="date",constant = "2023-2024")
    ProjectDTO mapToProjectDTO(AnnotationTableDTO annotation,Integer grade); //Grade из другой таблицы

    //@Mapping(target ="id",constant = "1")
    @Named("setTags") // можно вызывать по имени
    default List<Integer> setTags(String tag) {
        //[
        //  { "id": 1, "name": "Игра" },
        //  { "id": 2, "name": "Мобильное + Desktop-приложение" },
        //  { "id": 3, "name": "Мобильное приложение" },
        //  { "id": 4, "name": "Мод" },
        //  { "id": 5, "name": "Web-приложение" }
        //]
        Map<String,Integer> dictionary = Map.of(
                "Игра",1,
                "Мобильное + Desktop-приложение",2,
                "Мобильное приложение",3,
                "Мод",4,
                "Web-приложение",5);

        return Objects.requireNonNull(List.of(dictionary.get(tag)));
    }

    //@Mapping(target ="id",constant = "1")
    @Mapping(target = "fullName",source ="summary",qualifiedByName="concatName")
    @Mapping(target="roleId",constant = "1") // Участник = 1
    UserDTO mapToUserDTO(SummaryTableDTO summary);

    @Named("concatName") // можно вызывать по имени
    default String concatName(SummaryTableDTO summaryTableDTO) {
        return String.format("%s %s %s",
                summaryTableDTO.getSurName(),
                summaryTableDTO.getFirstName(),
                summaryTableDTO.getSecondName());
    }
}