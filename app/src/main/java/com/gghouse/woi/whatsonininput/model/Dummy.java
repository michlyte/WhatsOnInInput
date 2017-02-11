package com.gghouse.woi.whatsonininput.model;

/**
 * Created by michaelhalim on 2/11/17.
 */

public class Dummy {
    private String title;
    private String subtitle;
    private Integer drawable;

    public Dummy(String title, String subtitle, Integer drawable) {
        this.title = title;
        this.subtitle = subtitle;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }
}
