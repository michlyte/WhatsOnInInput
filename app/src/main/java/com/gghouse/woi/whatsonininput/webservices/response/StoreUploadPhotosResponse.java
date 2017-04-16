package com.gghouse.woi.whatsonininput.webservices.response;

import com.gghouse.woi.whatsonininput.model.StoreFileLocation;

import java.util.List;

/**
 * Created by michaelhalim on 4/16/17.
 */

public class StoreUploadPhotosResponse extends GenericResponse {
    private List<StoreFileLocation> data;

    public List<StoreFileLocation> getData() {
        return data;
    }

    public void setData(List<StoreFileLocation> data) {
        this.data = data;
    }
}
