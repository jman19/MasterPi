package com.masterPi.resources;

import java.util.List;

public class CategoryListWrapper {
    private List<CategoryWrapper> categories;

    public CategoryListWrapper() {
    }

    public CategoryListWrapper(List<CategoryWrapper> categories) {
        this.categories = categories;
    }

    public List<CategoryWrapper> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryWrapper> categories) {
        this.categories = categories;
    }
}
