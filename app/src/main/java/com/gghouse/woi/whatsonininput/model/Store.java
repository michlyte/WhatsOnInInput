package com.gghouse.woi.whatsonininput.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael on 3/16/2017.
 */

public class Store implements Serializable {
    private Long storeId;
    private AreaCategory category;
    private String district;
    private String name;
    private String developer;
    private String description;
    private String address;
    private String phoneNo;
    private String web;
    private String email;
    private String floor;
    private String blockNumber;
    private List<StoreFileLocation> photos;
    private Long areaId;
    private String stringTags;

    /*
     * Dev
     */

    private Integer drawable;

    public Store(Long storeId, String name, String description, Integer drawable) {
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.drawable = drawable;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getStringTags() {
        return stringTags;
    }

    public void setStringTags(String stringTags) {
        this.stringTags = stringTags;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }
}
