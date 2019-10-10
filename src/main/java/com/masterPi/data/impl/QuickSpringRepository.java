package com.masterPi.data.impl;
import com.masterPi.data.QuickRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class QuickSpringRepository implements QuickRepository {
    private TextJPA textJPA;
    private SelectedTextJPA selectedTextJPA;

    public QuickSpringRepository(TextJPA textJPA, SelectedTextJPA selectedTextJPA) {
        this.textJPA = textJPA;
        this.selectedTextJPA = selectedTextJPA;
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
        return textJPA.findAll();
    }

    public Text createText(Text text){
        return textJPA.save(text);
    }

    public void deleteText(Long id){
        textJPA.deleteById(id);
    }

    public SelectedText createSelectedText(SelectedText selectedText){
        return selectedTextJPA.save(selectedText);
    }

    public void deleteSelectedText(){
        selectedTextJPA.deleteAll();
    }

    public SelectedText getSelectedText(){
        List<SelectedText> selectedTextList=selectedTextJPA.findAll();
        if (selectedTextList.isEmpty()){
            return null;
        }
        else{
            return selectedTextList.get(0);
        }
    }
}
