package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.City;

/**
 * Created by michael on 3/22/2017.
 */

public class CityListResponse extends GenericResponse {
    private City[] data;

    public City[] getData() {
        return data;
    }

    public void setData(City[] data) {
        this.data = data;
    }
}
