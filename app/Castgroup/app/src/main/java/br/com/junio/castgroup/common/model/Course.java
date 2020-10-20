package br.com.junio.castgroup.common.model;

import java.util.HashMap;

import br.com.junio.castgroup.common.services.DateUtil;
import lombok.Data;

@Data
public class Course {
    private long id;
    private String _id;
    private String subjectDesc;
    private long startDate;
    private long endDate;
    private Integer students;
    private long fkCategory;


    public HashMap<String, Object> getHash() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("_id", get_id());
        map.put("subjectDesc", getSubjectDesc());
        map.put("startDate", DateUtil.toString(getStartDate()));
        map.put("endDate", DateUtil.toString(getEndDate()));
        map.put("students", getStudents());
        map.put("fkCategory", getFkCategory());

        return map;
    }
}
