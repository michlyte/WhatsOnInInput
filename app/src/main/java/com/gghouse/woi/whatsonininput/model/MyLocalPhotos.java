package com.gghouse.woi.whatsonininput.model;

import java.util.List;

/**
 * Created by michael on 4/26/2017.
 */

public class MyLocalPhotos {
    private List<StoreFileLocation> photos;

    public MyLocalPhotos(List<StoreFileLocation> photos) {
        this.photos = photos;
    }

    public List<StoreFileLocation> getPhotos() {
        return photos;
    }

    public void setPhotos(List<StoreFileLocation> photos) {
        this.photos = photos;
    }
}
