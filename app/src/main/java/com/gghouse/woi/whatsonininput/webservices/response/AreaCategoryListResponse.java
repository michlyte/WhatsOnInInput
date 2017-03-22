package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.AreaCategory;

/**
 * Created by michael on 3/22/2017.
 */

public class AreaCategoryListResponse extends GenericResponse {
    private AreaCategory[] data;

    public AreaCategory[] getData() {
        return data;
    }

    public void setData(AreaCategory[] data) {
        this.data = data;
    }
}
