package com.gghouse.woi.whatsonininput.model;

/**
 * Created by michael on 3/16/2017.
 */

public class StoreFileLocation {
    private Long storeFileId;
    private String location;
    private String fileName;
    private String status;
    private Long storeId;
    private String createdBy;
    private Long createdOn;
    private String updatedBy;
    private String updatedOn;

    public Long getStoreFileId() {
        return storeFileId;
    }

    public void setStoreFileId(Long storeFileId) {
        this.storeFileId = storeFileId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
