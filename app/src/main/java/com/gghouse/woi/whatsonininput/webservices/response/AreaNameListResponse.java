package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.AreaName;

/**
 * Created by michael on 3/22/2017.
 */

public class AreaNameListResponse extends GenericResponse {
    private AreaName[] data;

    public AreaName[] getData() {
        return data;
    }

    public void setData(AreaName[] data) {
        this.data = data;
    }
}
