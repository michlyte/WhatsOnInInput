package com.gghouse.woi.whatsonininput.model;

/**
 * Created by michael on 3/16/2017.
 */

public class Category {
    private Integer categoryId;
    private String categoryName;
    private Integer type; //0 area, 1 store

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
