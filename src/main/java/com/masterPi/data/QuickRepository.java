package com.masterPi.data;

import com.masterPi.data.impl.Text;

import java.util.List;

public interface QuickRepository {
    Text getText(Long id);
    List<Text> getAllText();
    Text createText(Text text);
    void deleteText(Long id);
    Text getSelected();
}
