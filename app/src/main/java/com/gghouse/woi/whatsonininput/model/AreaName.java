package com.gghouse.woi.whatsonininput.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael on 3/22/2017.
 */

public class AreaName implements Serializable {
    private Long areaId;
    private City city;
    private AreaCategory category;
    private String name;
    private String description;
    private String address;
    private String developer;
    private String phoneNumber;
    private String website;
    private List<StoreFileLocation> photos;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public AreaCategory getCategory() {
        return category;
    }

    public void setCategory(AreaCategory category) {
        this.category = category;
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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<StoreFileLocation> getPhotos() {
        return photos;
    }

    public void setPhotos(List<StoreFileLocation> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return name;
    }
}
