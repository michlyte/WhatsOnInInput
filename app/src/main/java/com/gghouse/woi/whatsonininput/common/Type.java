package com.gghouse.woi.whatsonininput.common;

/**
 * Created by michael on 5/3/2017.
 */

public enum Type {
    INDOOR(0, "Indoor"),
    OUTDOOR(1, "Outdoor");

    private Integer id;
    private String desc;

    Type(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Integer getTypeIdByDesc(String desc) {
        for (Type type : Type.values()) {
            if (type.desc.equals(desc)) {
                return type.id;
            }
        }
        return null;
    }

    public static Type getById(int id) {
        for (Type type : Type.values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return desc;
    }
}
