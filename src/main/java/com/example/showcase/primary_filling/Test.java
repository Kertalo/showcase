package com.example.showcase.primary_filling;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<UserDTO> users= new ArrayList<>();
        List<ProjectDTO> projects = new ArrayList<>();
        primaryFillingExecutor.execute(users,projects);
    }
}
