package com.masterPi.resources;

import java.util.List;

public class CategoryWrapper {
    private Long id;
    private String name;
    private String description;
    private List<TextWrapper> texts;

    public CategoryWrapper() {
    }

    public CategoryWrapper(Long id, String name, String description, List<TextWrapper> texts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.texts = texts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<TextWrapper> getTexts() {
        return texts;
    }

    public void setTexts(List<TextWrapper> texts) {
        this.texts = texts;
    }
}
