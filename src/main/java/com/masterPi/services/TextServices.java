package com.masterPi.services;
import com.masterPi.ExceptionHandler.CustomRunTimeException;
import com.masterPi.data.QuickRepository;
import com.masterPi.data.impl.Index;
import com.masterPi.data.impl.SelectedText;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.TextWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextServices {
    private QuickRepository quickRepository;
    private Environment env;

    public TextServices(QuickRepository quickRepository, Environment env) {
        this.quickRepository = quickRepository;
        this.env = env;
    }

    /***
     * This method saves a new text onto master pi database
     * @param title
     * @param content
     * @return the createdText
     */
    public Text createText(String title,String content){
        Index index=new Index();
        index.setIndex(0);

        Text text=new Text();
        text.setIndex(index);
        text.setText(content);
        text.setTitle(title);

        return quickRepository.createText(text);
    }

    /***
     * This method saves the selected text id so if system is powered off it will still
     * remember the last selected text
     * @param id
     */
    public void selectText(Long id){
        if(quickRepository.getSelectedText()==null){
            quickRepository.createSelectedText(new SelectedText(id));
        }
        else{
            SelectedText select=quickRepository.getSelectedText();
            select.setTextIdSelected(id);
            quickRepository.createSelectedText(select);
        }
    }

    /***
     * This method gets the text database entry
     * @param id
     * @return Text
     */
    public Text getText(Long id){
        return quickRepository.getText(id);
    }

    /***
     * This method removes the text database entry
     * @param id
     */
    public void deleteText(Long id){
        quickRepository.deleteText(id);

        //if text to be delete is also currently selected remove selection
        if(quickRepository.getSelectedText().getTextIdSelected()==id){
            quickRepository.deleteSelectedText();
        }
    }

    /***
     * This method gets all the text ids and their corresponding titles
     * but doesnt include the text content this is to save bandwidth if one needs the content use the getText method
     * @return
     */
    public List<TextWrapper> getAllStoredText(){
        List<TextWrapper> titles=new ArrayList<>();
        List<Text> texts=quickRepository.getAllText();
        for (Text t:texts){
            titles.add(new TextWrapper(t.getId(),t.getTitle()));
        }
        return titles;
    }

    /***
     * This method will increment the current reader index forwards
     */
    public void updateIndexNext(){
        Text text=getCurrentSelectedText();
        Index index=text.getIndex();

        //check we are not at end of content
        if(index.getIndex()+Integer.parseInt(env.getProperty("parseSize"))<=text.getText().length()){
            index.setIndex(index.getIndex()+Integer.parseInt(env.getProperty("parseSize")));
        }

        //update the index of the text
        quickRepository.createText(text);
    }

    /***
     * This method will increment the current reader index backwards
     */
    public void updateIndexPrev(){
        Text text=getCurrentSelectedText();
        Index index=text.getIndex();

        //check we are not at beginning of content
        if(index.getIndex()-Integer.parseInt(env.getProperty("parseSize"))>=0){
            index.setIndex(index.getIndex()-Integer.parseInt(env.getProperty("parseSize")));
        }

        //update the index of the text
        quickRepository.createText(text);
    }

    /***
     * This method will reset the index to the start
     */
    public void reset(){
        Text text=getCurrentSelectedText();
        Index index=text.getIndex();
        index.setIndex(0);

        //update the index of the text
        quickRepository.createText(text);
    }

    /***
     * This method will return the current sub text being read by the index
     * @return subString
     */
    public String getIndexReadText(){
        Text text=getCurrentSelectedText();
        Index index=text.getIndex();

        //if the remaining string is less then full parse size just return remaining characters
        if(index.getIndex()+Integer.parseInt(env.getProperty("parseSize"))> text.getText().length()){
            return text.getText().substring(index.getIndex());
        }
        else{
            return text.getText().substring(index.getIndex(),index.getIndex()+Integer.parseInt(env.getProperty("parseSize")));
        }
    }

    /***
     * This helper function checks if a selection was made and if the selected text is still in system
     * @return selected text
     */
    private Text getCurrentSelectedText(){
        SelectedText sel=quickRepository.getSelectedText();
        if (sel==null){
            throw new CustomRunTimeException("no text was selected before hand", HttpStatus.BAD_REQUEST);
        }
        if(quickRepository.getText(sel.getTextIdSelected())==null){
            throw new CustomRunTimeException("selected text not found was likely deleted", HttpStatus.BAD_REQUEST);
        }
        Text text=quickRepository.getText(sel.getTextIdSelected());
        return text;
    }
}
