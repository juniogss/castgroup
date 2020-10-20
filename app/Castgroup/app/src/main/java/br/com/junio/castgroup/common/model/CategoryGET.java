package br.com.junio.castgroup.common.model;

import lombok.Data;

@Data
public class CategoryGET {
    private int status;
    private String message;
    private Category data;
}
