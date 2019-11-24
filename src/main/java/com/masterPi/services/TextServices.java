package com.masterPi.services;
import com.masterPi.ExceptionHandler.CustomRunTimeException;
import com.masterPi.data.QuickRepository;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.SelectTextInput;
import com.masterPi.resources.TextWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TextServices {
    private QuickRepository quickRepository;
    private Environment env;
    private SlavePIServices slavePIServices;

    public TextServices(QuickRepository quickRepository, Environment env, SlavePIServices slavePIServices) {
        this.quickRepository = quickRepository;
        this.env = env;
        this.slavePIServices = slavePIServices;
    }

    /***
     * This method saves a new text onto master pi database
     * @param title
     * @param content
     * @return the createdText
     */
    public Text createText(String title,String content){
        if((title==null || title.isEmpty()) || (content==null || content.isEmpty())){
            throw new CustomRunTimeException("title/content cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Text text=new Text();
        text.setIndex(0);
        text.setText(content);
        text.setTitle(title);
        text.setSelected(false);
        return quickRepository.createText(text);
    }

    /***
     * this endPoint allows you to edit an existing text
     * @param id
     * @param title
     * @param content
     * @return edited text
     */
    public Text editText(Long id, String title, String content){
        if(quickRepository.getText(id)==null){
            throw new CustomRunTimeException("the text was not found", HttpStatus.NOT_FOUND);
        }
        if((title==null || title.isEmpty()) || (content==null || content.isEmpty())){
            throw new CustomRunTimeException("title/content cannot be empty", HttpStatus.BAD_REQUEST);
        }
        else{
            Text text=quickRepository.getText(id);
            text.setTitle(title);
            text.setText(content);
            text.setIndex(0);

            text.setTimeStamp(new Date());
            Text updatedText=quickRepository.createText(text);

            //update the text on braille board as well if the edited text is the one currently selected
            if(updatedText.getSelected()) {
                //send the first characters to slave PI
                slavePIServices.translate(getIndexReadText());
            }
            return updatedText;
        }
    }

    /***
     * This method saves the selected text id so if system is powered off it will still
     * remember the last selected text
     * @param id
     */
    public void selectText(Long id){
        //clear slave PI
        slavePIServices.clear();

        Text text=quickRepository.getText(id);
        if(text==null){
            throw new CustomRunTimeException("invalid text id, text not found in system",HttpStatus.BAD_REQUEST);
        }
        //deselect the old text
        Text sel=quickRepository.getSelected();
        if(sel!=null){
            sel.setSelected(false);
            quickRepository.createText(sel);
        }
        //mark new text as selected
        text.setSelected(true);
        quickRepository.createText(text);

        //send the first characters to slave PI
        slavePIServices.translate(getIndexReadText());

    }

    /***
     * This method gets the text database entry
     * @param id
     * @return Text
     */
    public Text getText(Long id){
        Text text=quickRepository.getText(id);
        if(text==null){
            throw new CustomRunTimeException("text was not found", HttpStatus.NOT_FOUND);
        }
        else{
            return text;
        }
    }

    /***
     * This method removes the text database entry
     * @param id
     */
    public void deleteText(Long id){
        if(quickRepository.getText(id)==null){
            throw new CustomRunTimeException("text to delete was not found/already deleted", HttpStatus.NOT_FOUND);
        }

        Text sel=quickRepository.getSelected();
        //if deleted text is the one currently selected clear the braille module
        if(sel!=null && sel.getId()==id){
            //clear slave PI
            slavePIServices.clear();
        }
        quickRepository.deleteText(id);
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
            String category=null;
            if(t.getCategory()!=null){
                category=t.getCategory().getName();
            }
            titles.add(new TextWrapper(t.getId(),t.getTitle(),t.getTimeStamp(),category));
        }
        return titles;
    }

    /***
     * This method will increment the current reader index forwards
     */
    public void updateIndexNext(){
        Text text=getCurrentSelectedText();

        //check we are not at end of content
        if(text.getIndex()+Integer.parseInt(env.getProperty("parseSize"))<text.getText().length()){
            text.setIndex(text.getIndex()+Integer.parseInt(env.getProperty("parseSize")));
        }

        //update the index of the text
        quickRepository.createText(text);

        //send the next characters to slave PI
        slavePIServices.translate(getIndexReadText());
    }

    /***
     * This method will increment the current reader index backwards
     */
    public void updateIndexPrev(){
        Text text=getCurrentSelectedText();

        //check we are not at beginning of content
        if(text.getIndex()-Integer.parseInt(env.getProperty("parseSize"))>=0){
            text.setIndex(text.getIndex()-Integer.parseInt(env.getProperty("parseSize")));
        }

        //update the index of the text
        quickRepository.createText(text);

        //send the prev characters to slave PI
        slavePIServices.translate(getIndexReadText());
    }

    /***
     * This method will reset the index to the start
     */
    public void reset(){
        Text text=getCurrentSelectedText();
        text.setIndex(0);
        //update the index of the text
        quickRepository.createText(text);

        //send the first characters to slave PI
        slavePIServices.translate(getIndexReadText());
    }

    /***
     * This method will return the current sub text being read by the index
     * @return subString
     */
    public String getIndexReadText(){
        Text text=getCurrentSelectedText();
        Integer parseSize=Integer.parseInt(env.getProperty("parseSize"));

        //if the remaining string is less then full parse size just return remaining characters
        //also pad it with spaces at the end to maintain same substring size
        if(text.getIndex()+parseSize> text.getText().length()){

            return String.format("%-" + parseSize + "s", text.getText().substring(text.getIndex()));
        }
        else{
            return text.getText().substring(text.getIndex(),text.getIndex()+parseSize);
        }
    }

    /***
     * get the id of the currently selected text
     * @return select id
     */
    public SelectTextInput getSelect(){
        Text sel=getCurrentSelectedText();
        return new SelectTextInput(sel.getId());

    }

    /***
     * This helper function checks if a selection was made and if the selected text is still in system
     * @return selected text
     */
    private Text getCurrentSelectedText(){
        Text sel=quickRepository.getSelected();
        if (sel==null){
            throw new CustomRunTimeException("no text was selected before hand or selected text deleted", HttpStatus.BAD_REQUEST);
        }
        return sel;
    }
}
