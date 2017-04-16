package com.gghouse.woi.whatsonininput.webservices.response;

import com.github.pwittchen.prefser.library.Prefser;

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
        if (code == null) {
            return 400;
        } else {
            return code;
        }
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
