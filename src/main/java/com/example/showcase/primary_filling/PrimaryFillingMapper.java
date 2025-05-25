package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import com.example.showcase.service.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PrimaryFillingMapper {
    //static NewPrimaryFillingMapper mapper = Mappers.getMapper(NewPrimaryFillingMapper.class);

    @Autowired
    private TrackService trackService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DateService dateService;

    @Mapping(target = "id",constant = "0") //для автоинкремента при добавлении Entity
    @Mapping(target = "title", source = "annotation.name")
    @Mapping(target = "description", source = "annotation.description")
    @Mapping(target = "repo", source = "annotation.repository")
    @Mapping(target = "trackId", source = "trackName", qualifiedByName = "setTrackId")
    @Mapping(target = "dateId", source = "dateName",qualifiedByName = "setDateId")
    @Mapping(target = "tagsId", source = "annotation.tag", qualifiedByName = "setTags")
    @Mapping(target = "usersId",source = "annotation.team",qualifiedByName = "setTeam")
    @Mapping(target = "grade", source = "score.grade")
    @Mapping(target = "presentation",source ="annotation.presentation")
    abstract ProjectDTO mapToProjectDTO(AnnotationTableDTO annotation, ScoreTableDTO score, String trackName,String dateName);


    @Named("setTrackId")
    protected Integer setTrackId(String trackName){
        return trackService.getTrackByName(trackName).getId();
    }

    @Named("setDateId")
    protected Integer setDateId(String dateName){
        return dateService.getDateByName(dateName).getId();
    }

    @Named("setTags")
    protected List<Integer> setTags(String tagStr) {
        return Arrays.stream(getTagsFromStr(tagStr)).map(x->tagService.getTagByName(x).getId()).toList();
    }

    @Named("setTeam")
    protected List<Integer> setTeam(String teamStr){
        List<Integer> result = new ArrayList<>();
        String[] team = Pattern.compile("\n").split(teamStr);
        for (String participant: team){
            String[] participantInfo = Pattern.compile("\\s+").split(participant);
            if (participantInfo.length == 3){ //может отсутствовать отчество
                result.add(userService.getUserByFullNameAndCourse(
                        getFullName(participantInfo[0], participantInfo[1], ""),
                        participantInfo[2]).getId());
            }
            else {
                result.add(userService.getUserByFullNameAndCourse(
                        getFullName(participantInfo[0], participantInfo[1], participantInfo[2]),
                        participantInfo[3]).getId());
            }
        }
        return result;
    }

    @Mapping(target = "id",constant = "0") //для автоинкремента при добавлении Entity
    @Mapping(target = "fullName", source = "summary", qualifiedByName = "concatName")
    @Mapping(target = "roleId", source = "summary.comment", qualifiedByName = "setRole")
    abstract UserDTO mapToUserDTO(SummaryTableDTO summary);

    abstract List<UserDTO> mapToUserDTOList(List<SummaryTableDTO> summaryTableDTOList);

    @Named("concatName")
    protected String concatName(SummaryTableDTO summaryTableDTO) {
        return getFullName(
                summaryTableDTO.getSurName(),
                summaryTableDTO.getFirstName(),
                summaryTableDTO.getSecondName()
        );
    }

    @Named("setRole")
    protected Integer setRole(String comment) {
//        { "id": 1, "name": "Участник"},
//        { "id": 2, "name": "Админ"},
//        { "id": 3, "name": "Староста"},
//        { "id": 4, "name": "СуперАдмин"}
        return comment.contains("Староста") ? 3 : 1;
    }

    public static String[] getTagsFromStr(String tagStr){
        String[] tags = Pattern.compile("\n").split(tagStr);
        for (int i=0;i<tags.length;i++) {
            tags[i]=tags[i].trim();
        }
        return tags;
    }

    public static String getFullName(String surName, String firstName,String secondName) {
        return String.format("%s %s %s",
                surName,
                firstName,
                secondName).trim();
    }
}