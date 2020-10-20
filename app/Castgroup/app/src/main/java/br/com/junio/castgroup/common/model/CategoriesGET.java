package br.com.junio.castgroup.common.model;

import java.util.List;

import lombok.Data;

@Data
public class CategoriesGET {
    private int status;
    private String message;
    private List<Category> data;
}
