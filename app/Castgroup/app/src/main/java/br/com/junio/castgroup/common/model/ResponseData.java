package br.com.junio.castgroup.common.model;

import lombok.Data;

@Data
public class ResponseData {
    private String value;
    private String msg;
    private String param;
    private String location;
}
