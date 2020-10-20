package br.com.junio.castgroup.common.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseObj {
    private int status;
    private String message;
    private List<ResponseData> data;
}
