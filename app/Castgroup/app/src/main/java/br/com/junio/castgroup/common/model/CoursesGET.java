package br.com.junio.castgroup.common.model;

import java.util.List;

import lombok.Data;

@Data
public class CoursesGET {
    private int status;
    private String message;
    private List<CourseObj> data;
}
