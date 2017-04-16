package com.gghouse.woi.whatsonininput.webservices.request;

import com.gghouse.woi.whatsonininput.model.StoreFileLocation;

/**
 * Created by michaelhalim on 4/16/17.
 */

public class StoreFileLocationRequest extends StoreFileLocation {
    private String strImgBase64; //Base 64

    public StoreFileLocationRequest(String location, String fileName, String status, Long storeId, String createdBy, String updatedBy, String strImgBase64) {
        super(location, fileName, status, storeId, createdBy, updatedBy);
        this.strImgBase64 = strImgBase64;
    }

    public String getStrImgBase64() {
        return strImgBase64;
    }

    public void setStrImgBase64(String strImgBase64) {
        this.strImgBase64 = strImgBase64;
    }
}
