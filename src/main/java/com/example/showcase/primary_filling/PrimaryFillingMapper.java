package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrimaryFillingMapper {
    PrimaryFillingMapper INSTANCE = Mappers.getMapper(PrimaryFillingMapper.class);


    //@Mapping(target ="id", constant = "1")
    @Mapping(target ="trackId", constant = "1") //Бакалавриат - 1
    @Mapping(target ="title",source = "annotation.name")
    @Mapping(target ="description",source ="annotation.description")
    @Mapping(target ="repo",source = "annotation.repository")
    //@Mapping(target ="tagsId",) как правильно связать ???
    //@Mapping(target ="usersId") как правильно связать ???
    @Mapping(target ="date",constant = "2023-2024")
    ProjectDTO mapToProjectDTO(AnnotationTableDTO annotation,Integer grade); //Grade из другой таблицы


    //@Mapping(target ="id",constant = "1")
    @Mapping(target = "fullName",source ="summary",qualifiedByName="concatName")
    @Mapping(target="roleId",constant = "1") // Участник - 1
    UserDTO mapToUserDTO(SummaryTableDTO summary);

    @Named("concatName") // можно вызывать по имени
    default String concatName(SummaryTableDTO summaryTableDTO) {
        return String.format("%s %s %s",
                summaryTableDTO.getSurName(),
                summaryTableDTO.getFirstName(),
                summaryTableDTO.getSecondName());
    }
}