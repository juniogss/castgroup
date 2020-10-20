package br.com.junio.castgroup.common.model;

import lombok.Data;

@Data
public class CourseObj {
    private String _id;
    private String subjectDesc;
    private String startDate;
    private String endDate;
    private Integer students;
    private long fkCategory;

}
