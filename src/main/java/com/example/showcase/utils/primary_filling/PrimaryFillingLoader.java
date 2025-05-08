package com.example.showcase.utils.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Date;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.service.*;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrimaryFillingLoader {
    @Autowired
    private PrimaryFillingMapper mapper;

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

    @Getter
    @Setter
    private String dateName;

    @Getter
    @Setter
    private String trackName;

    //Общие списки
    @Getter
    @Setter
    private String summaryTable;

    //Таблица с аннотациями
    @Getter
    @Setter
    private String annotationTable;

    //Таблица с баллами
    @Getter
    @Setter
    private String scoreTable;

    public void load(@NonNull String dateName,
                     @NonNull String trackName,
                     @NonNull String summaryTable,
                     @NonNull String annotationTable,
                     @NonNull String scoreTable){
        this.setDateName(dateName);
        this.setTrackName(trackName);
        this.setSummaryTable(summaryTable);
        this.setAnnotationTable(annotationTable);
        this.setScoreTable(scoreTable);
        this.fillDB();
    }


    @Transactional
    protected void fillDB(){
        //добавление треков
        if(!trackService.existsByName(trackName)){
            Track track = new Track();
            track.setName(trackName);
            trackService.createTrack(track);
        }
        System.out.println("Tracks Saved!");

        //добавление дат
        if(!dateService.existsByName(dateName)){
            Date date = new Date();
            date.setName(dateName);
            dateService.createDate(date);
        }
        System.out.println("Dates Saved!");

        //добавление тегов
        List<String> tagNames;
        try {
            tagNames = new ArrayList<>(scrapAnnotationTable().stream().map(x -> PrimaryFillingMapper.getTagsFromStr(x.getTag())).flatMap(Arrays::stream).collect(Collectors.toSet()));
        }
        catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
        for (String tagName: tagNames){
            if(!tagService.existsByName(tagName)){
                Tag tag = new Tag();
                tag.setName(tagName);
                tagService.createTag(tag);
            }
        }
        System.out.println("Tags Saved!");

        //добавление юзеров
        List<UserDTO> userDTOs;
        try {
            userDTOs = new ArrayList<>(
                   mapper.mapToUserDTOList(scrapSummaryTable())
                            .stream().filter(x->!userService.exitsByFullNameAndCourse(x)).toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(!userDTOs.isEmpty()) userService.saveUsersFromDTO(userDTOs);
        System.out.println("Users Saved!");

        //добавление проектов
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        try{
            List<AnnotationTableDTO> annotationList = scrapAnnotationTable();
            for (AnnotationTableDTO annotation: annotationList){
                if (isProjectValid(annotation)) projectDTOs.add(mapper.mapToProjectDTO(annotation, joinScoreByAnnotationName(annotation), trackName, dateName));
            }
            projectDTOs.stream().filter(x->!projectService.existsByTitle(x.getTitle())).toList();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(!projectDTOs.isEmpty()) projectService.saveProjectsFromDTO(projectDTOs);
        System.out.println("Projects Saved!");
        System.out.println("Primary Filling completed");

    }

    private List<SummaryTableDTO> scrapSummaryTable() throws FileNotFoundException {
        return new CsvToBeanBuilder<SummaryTableDTO>(new FileReader(summaryTable))
                .withType(SummaryTableDTO.class)
                .build()
                .parse();
    }

    private List<AnnotationTableDTO> scrapAnnotationTable() throws FileNotFoundException {
        return new CsvToBeanBuilder<AnnotationTableDTO>(new FileReader(annotationTable))
                .withType(AnnotationTableDTO.class)
                .build()
                .parse();
    }

    private List<ScoreTableDTO> scrapScoreTable() throws FileNotFoundException {
        return new CsvToBeanBuilder<ScoreTableDTO>(new FileReader(scoreTable))
                .withType(ScoreTableDTO.class)
                .build()
                .parse();
    }

    //Получение записей таблицы "Общие списки", связанных с данной записью "Аннотации"
    private List<SummaryTableDTO> joinSummariesByAnnotationName(AnnotationTableDTO annotation) throws FileNotFoundException {
        return scrapSummaryTable().stream().filter(x->x.getTeam().equals(annotation.getName())).toList();
    }

    //Проверка того, что запись в таблице "Аннотации" актуальна (есть участники в таблице "Общие списки")
    private boolean isProjectValid(AnnotationTableDTO annotation) throws FileNotFoundException {
        return !scrapSummaryTable().stream().filter(x->x.getTeam().equals(annotation.getName())).toList().isEmpty();
    }

    //Получение записи таблицы "Баллы", связанных с данной записью "Аннотации"
    private ScoreTableDTO joinScoreByAnnotationName(AnnotationTableDTO annotation) throws FileNotFoundException {
        return scrapScoreTable().stream().filter(x->x.getName().equals(annotation.getName())).toList().getFirst();
    }

}
