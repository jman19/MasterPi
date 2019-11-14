package com.masterPi.resources;

public class TranslateInput {
    private String text;

    public TranslateInput() {
    }

    public TranslateInput(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
