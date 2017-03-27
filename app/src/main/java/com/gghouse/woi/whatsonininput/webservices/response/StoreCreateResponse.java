package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.Store;

/**
 * Created by michael on 3/27/2017.
 */

public class StoreCreateResponse extends GenericResponse {
    private Store data;

    public Store getData() {
        return data;
    }

    public void setData(Store data) {
        this.data = data;
    }
}
