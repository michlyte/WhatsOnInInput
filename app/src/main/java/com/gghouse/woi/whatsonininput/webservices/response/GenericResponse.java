package com.gghouse.woi.whatsonininput.webservices.response;

/**
 * Created by michael on 3/16/2017.
 */

public class GenericResponse {
    private String status;
    private Integer code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return (code != null ? code : 400);
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
