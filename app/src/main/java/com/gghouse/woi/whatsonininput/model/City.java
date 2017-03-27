package com.gghouse.woi.whatsonininput.model;

import java.io.Serializable;

/**
 * Created by michael on 3/22/2017.
 */

public class City implements Serializable {
    private Long cityId;
    private String cityName;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
