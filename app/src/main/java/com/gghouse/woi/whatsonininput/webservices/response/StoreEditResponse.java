package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.Store;

/**
 * Created by michaelhalim on 4/14/17.
 */

public class StoreEditResponse extends GenericResponse {
    private Store data;

    public Store getData() {
        return data;
    }

    public void setData(Store data) {
        this.data = data;
    }
}
