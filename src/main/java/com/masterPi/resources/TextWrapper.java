package com.masterPi.resources;

import java.util.Date;

public class TextWrapper {
    private Long id;
    private String title;
    private Date timeStamp;
    private String category;

    public TextWrapper() {
    }

    public TextWrapper(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public TextWrapper(Long id, String title, Date timeStamp) {
        this.id = id;
        this.title = title;
        this.timeStamp = timeStamp;
    }

    public TextWrapper(Long id, String title, Date timeStamp,String category) {
        this.id = id;
        this.title = title;
        this.timeStamp = timeStamp;
        this.category=category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
