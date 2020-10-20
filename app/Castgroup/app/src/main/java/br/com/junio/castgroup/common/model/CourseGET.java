package br.com.junio.castgroup.common.model;

import lombok.Data;

@Data
public class CourseGET {
    private int status;
    private String message;
    private CourseObj data;
}
