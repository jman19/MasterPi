package com.masterPi.resources;

import java.util.Date;

public class TextWrapper {
    private Long id;
    private String title;
    private Date timeStamp;

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
}
