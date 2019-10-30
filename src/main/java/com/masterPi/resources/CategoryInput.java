package com.masterPi.resources;

import java.util.List;

public class CategoryInput {
    private String name;
    private String description;
    private List<TextWrapper> texts;

    public CategoryInput() {
    }

    public CategoryInput(String name, String description, List<TextWrapper> texts) {
        this.name = name;
        this.description = description;
        this.texts = texts;
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
