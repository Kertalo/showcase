package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class primaryFillingExecutor {

    //Общие списки
    private static final String summaryTable = "src/main/resources/primary_filling/Списки команд '23-'24 - Общие списки.csv";

    //Таблица с аннотациями
    private static final String annotationTable = "src/main/resources/primary_filling/Списки команд '23-'24 - Аннотации.csv";

    //Таблица с баллами
    private static final String scoreTable = "src/main/resources/primary_filling/Списки команд '23-'24 - Баллы.csv";

    public static void execute(List<UserDTO> users,List<ProjectDTO> projects){

        int userCnt =0;
        int projectCnt=0;
        try {
            List<SummaryTableDTO> dataSummaryTable= executeSummaryTable();
            List<AnnotationTableDTO> dataAnnotationTable= executeAnnotationTable();

            for(SummaryTableDTO singleSummary: dataSummaryTable){
                UserDTO user = PrimaryFillingMapper.INSTANCE.mapToUserDTO(singleSummary);
                user.setId(++userCnt);
                users.add(user);
            }
            for (AnnotationTableDTO singleAnnotation: dataAnnotationTable){
                ProjectDTO project = PrimaryFillingMapper.INSTANCE.mapToProjectDTO(
                        singleAnnotation,
                        getGrade(singleAnnotation,dataSummaryTable)
                        );
                project.setId(++projectCnt);


                project.setUsersId(getUsersId(project,users,dataSummaryTable));
                projects.add(project);
            }

        }
        catch (FileNotFoundException e){
            System.out.println(users.size());
            System.out.println(projects.size());
        }
    }


    private static List<SummaryTableDTO> executeSummaryTable() throws FileNotFoundException {
        return new CsvToBeanBuilder<SummaryTableDTO>(new FileReader(summaryTable))
                .withType(SummaryTableDTO.class)
                .build()
                .parse();
    }

    private static List<AnnotationTableDTO> executeAnnotationTable() throws FileNotFoundException {
        return new CsvToBeanBuilder<AnnotationTableDTO>(new FileReader(annotationTable))
                .withType(AnnotationTableDTO.class)
                .build()
                .parse();
    }

    //Нахождение grade из summaryTable для AnnotationTable
    private static Integer getGrade(AnnotationTableDTO singleAnnotation,List<SummaryTableDTO> dataSummaryTable){
        Optional<SummaryTableDTO> singleSummary = dataSummaryTable.stream().filter(x->singleAnnotation.getName().equals(x.getTeam())).findFirst();
        return singleSummary.map(SummaryTableDTO::getGrade).orElse(null);
    }
    //Заполнение поля usersId для project
    private static List<Integer> getUsersId(ProjectDTO project, List<UserDTO> users, List<SummaryTableDTO> dataSummaryTable) {
        List<String> userNames = dataSummaryTable.stream().filter(x->x.getTeam().equals(project.getTitle()))
                .map(x->String.format("%s %s %s",
                        x.getSurName(),
                        x.getFirstName(),
                        x.getSecondName())).toList();
        return users.stream().filter(x->userNames.contains(x.getFullName())).map(UserDTO::getId).toList();
    }
}
