package com.masterPi.resources;

import java.util.List;

public class TextListWrapper {
    private List<TextWrapper> textList;

    public TextListWrapper() {
    }

    public TextListWrapper(List<TextWrapper> textList) {
        this.textList = textList;
    }

    public List<TextWrapper> getTextList() {
        return textList;
    }

    public void setTextList(List<TextWrapper> textList) {
        this.textList = textList;
    }
}
