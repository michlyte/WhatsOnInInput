package com.gghouse.woi.whatsonininput.webservices.request;

import com.gghouse.woi.whatsonininput.model.AreaCategory;
import com.gghouse.woi.whatsonininput.model.StoreFileLocation;

import java.util.List;

/**
 * Created by michael on 3/27/2017.
 */

public class StoreCreateRequest {
    private AreaCategory category;
    private String district;
    private String name;
    private String description;
    private String address;
    private String phoneNo;
    private String web;
    private String email;
    private String floor;
    private String blockNumber;
    private List<StoreFileLocation> photos;
    private String stringTags;
    private Long areaId;

    public AreaCategory getCategory() {
        return category;
    }

    public void setCategory(AreaCategory category) {
        this.category = category;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public List<StoreFileLocation> getPhotos() {
        return photos;
    }

    public void setPhotos(List<StoreFileLocation> photos) {
        this.photos = photos;
    }

    public String getStringTags() {
        return stringTags;
    }

    public void setStringTags(String stringTags) {
        this.stringTags = stringTags;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}
