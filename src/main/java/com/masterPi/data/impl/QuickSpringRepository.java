package com.masterPi.data.impl;
import com.masterPi.data.QuickRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class QuickSpringRepository implements QuickRepository {
    private TextJPA textJPA;

    public QuickSpringRepository(TextJPA textJPA) {
        this.textJPA = textJPA;
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

    public Text getSelected(){return textJPA.findFirstBySelectedEquals(true);}
}
