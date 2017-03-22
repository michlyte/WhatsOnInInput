package com.gghouse.woi.whatsonininput.model;

/**
 * Created by michael on 3/22/2017.
 */

public class AreaCategory {
    private Long categoryId;
    private String categoryName;
    private Integer type;

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getType() {
        return type;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
