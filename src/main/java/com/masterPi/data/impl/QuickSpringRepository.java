package com.masterPi.data.impl;
import com.masterPi.data.QuickRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class QuickSpringRepository implements QuickRepository {
    private TextJPA textJPA;
    private CategoryJPA categoryJPA;

    public QuickSpringRepository(TextJPA textJPA, CategoryJPA categoryJPA) {
        this.textJPA = textJPA;
        this.categoryJPA=categoryJPA;
    }

    public Text getText(Long id){
        try{
            Optional byId=textJPA.findById(id);
            return (Text) byId.get();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    public List<Text> getAllText(){
        return textJPA.findAllByOrderByTimeStampDesc();
    }

    public Text createText(Text text){
        return textJPA.save(text);
    }

    public void deleteText(Long id){
        textJPA.deleteById(id);
    }

    public Text getSelected(){return textJPA.findFirstBySelectedEquals(true);}

    public Category createCategory(Category category){
        return categoryJPA.save(category);
    }

    public Category getCategory(Long id){
        try{
            Optional byId=categoryJPA.findById(id);
            return (Category) byId.get();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    public void deleteCategory(Long id){
        categoryJPA.deleteById(id);
    }

    public List<Category> getAllCategory(){
        return categoryJPA.findAll();
    }
}
