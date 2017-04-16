package com.gghouse.woi.whatsonininput.model;

import java.io.Serializable;

/**
 * Created by michael on 3/16/2017.
 */

public class StoreFileLocation implements Serializable {
    protected Long storeFileId;
    protected String location;
    protected String fileName;
    protected String status;
    protected Long storeId;
    protected String createdBy;
    protected Long createdOn;
    protected String updatedBy;
    protected String updatedOn;

    public StoreFileLocation() {

    }

    public StoreFileLocation(String location, String fileName, String status, Long storeId, String createdBy, String updatedBy) {
        this.location = location;
        this.fileName = fileName;
        this.status = status;
        this.storeId = storeId;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

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
