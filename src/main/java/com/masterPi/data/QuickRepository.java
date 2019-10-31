package com.masterPi.data;

import com.masterPi.data.impl.Category;
import com.masterPi.data.impl.Text;

import java.util.List;

public interface QuickRepository {
    Text getText(Long id);
    List<Text> getAllText();
    Text createText(Text text);
    void deleteText(Long id);
    Text getSelected();

    Category createCategory(Category category);
    Category getCategory(Long id);
    void deleteCategory(Long id);
    List<Category> getAllCategory();
}
