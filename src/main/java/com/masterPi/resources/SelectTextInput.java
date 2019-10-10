package com.masterPi.resources;

public class SelectTextInput {
    private Long textIdToSelect;

    public SelectTextInput() {
    }

    public SelectTextInput(Long textIdToSelect) {
        this.textIdToSelect = textIdToSelect;
    }

    public Long getTextIdToSelect() {
        return textIdToSelect;
    }

    public void setTextIdToSelect(Long textIdToSelect) {
        this.textIdToSelect = textIdToSelect;
    }
}
