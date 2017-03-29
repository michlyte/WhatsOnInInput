package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.Store;

/**
 * Created by michael on 3/29/2017.
 */

public class StoreGetResponse extends GenericResponse {
    private Store data;

    public Store getData() {
        return data;
    }

    public void setData(Store data) {
        this.data = data;
    }
}
