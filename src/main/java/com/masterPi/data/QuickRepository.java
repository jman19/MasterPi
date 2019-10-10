package com.masterPi.data;

import com.masterPi.data.impl.SelectedText;
import com.masterPi.data.impl.Text;

import java.util.List;

public interface QuickRepository {
    Text getText(Long id);
    List<Text> getAllText();
    Text createText(Text text);
    void deleteText(Long id);

    SelectedText createSelectedText(SelectedText selectedText);
    void deleteSelectedText();
    SelectedText getSelectedText();
}
