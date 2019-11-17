package com.masterPi.services;

import com.masterPi.data.QuickRepository;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.BodyMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextServicesTest {

    @Autowired
    private QuickRepository quickSpringRepository;

    @Autowired
    private Environment env;

    @MockBean
    private SlavePIServices slavePIServices;

    @Autowired
    private TextServices textServices;


    @Test
    @Transactional
    public void createText() {
        Text text=textServices.createText("title","test");
        Text createdText=quickSpringRepository.getText(text.getId());
        assertTrue(createdText.getTitle().equals("title"));
        assertTrue(createdText.getText().equals("test"));
    }

    @Test
    @Transactional
    public void editText() {

        Text text = quickSpringRepository.createText(new Text("text", "title", false, null, 0));
        Mockito.when(slavePIServices.translate(any(String.class))).thenReturn(new BodyMessage("success translating text", 200));
        Text editedText = textServices.editText(text.getId(), "edited", "changed");
        assertTrue(editedText.getTitle().equals("edited"));
        assertTrue(editedText.getText().equals("changed"));
    }

    @Test
    @Transactional
    public void selectText() {
        Text text=quickSpringRepository.createText(new Text("text","title",false,null,0));
        Mockito.when(slavePIServices.translate(any(String.class))).thenReturn(new BodyMessage("success translating text",200));
        Mockito.when(slavePIServices.clear()).thenReturn(new BodyMessage("success",200));
        textServices.selectText(text.getId());
        assertTrue(quickSpringRepository.getText(text.getId()).getSelected());
    }

    @Test
    @Transactional
    public void getText(){
        Text text=quickSpringRepository.createText(new Text("text","title",false,null,0));
        Text getText=textServices.getText(text.getId());
        assertTrue(getText.getTitle().equals("title"));
        assertTrue(getText.getText().equals("text"));
        assertFalse(getText.getSelected());
        assertNull(getText.getCategory());
        assertEquals((Integer) 0,getText.getIndex());
    }

    @Test
    @Transactional
    public void deleteText() {
        Text text = quickSpringRepository.createText(new Text("text", "title", false, null, 0));
        textServices.deleteText(text.getId());
        assertNull(quickSpringRepository.getText(text.getId()));
    }

    @Test
    @Transactional
    public void getAllStoredText(){
        quickSpringRepository.createText(new Text("text","title1",false,null,0));
        quickSpringRepository.createText(new Text("text","title2",false,null,0));
        assertEquals(2,textServices.getAllStoredText().size());
    }

    /***
     * this is a stub to simulate next button being pressed
     */
    @Test
    @Transactional
    public void updateIndexNext() {
        Integer parseSize=Integer.parseInt(env.getProperty("parseSize"));
        Text text=quickSpringRepository.createText(new Text("texts","title",true,null,0));
        textServices.updateIndexNext();
        text=quickSpringRepository.getText(text.getId());
        assertEquals(parseSize,text.getIndex());

        //make sure pressing next again will not over increment index
        textServices.updateIndexNext();
        text=quickSpringRepository.getText(text.getId());
        assertEquals(parseSize,text.getIndex());
    }

    /***
     * this is a stub to simulate prev button being pressed
     */
    @Test
    @Transactional
    public void updateIndexPrev() {
        Integer parseSize=Integer.parseInt(env.getProperty("parseSize"));
        Text text=quickSpringRepository.createText(new Text("texts","title",true,null,parseSize));
        textServices.updateIndexPrev();
        text=quickSpringRepository.getText(text.getId());
        assertEquals((Integer)0,text.getIndex());

        //make sure pressing prev again will not increment index into negative
        textServices.updateIndexPrev();
        text=quickSpringRepository.getText(text.getId());
        assertEquals((Integer)0,text.getIndex());

    }

    /***
     * this is a stub to simulate reset button being pressed
     */
    @Test
    @Transactional
    public void reset() {
        Text text=quickSpringRepository.createText(new Text("texts","title",true,null,3));
        textServices.reset();
        text=quickSpringRepository.getText(text.getId());
        assertEquals((Integer) 0,text.getIndex());

    }

    @Test
    @Transactional
    public void getIndexReadText() {
        Integer parseSize=Integer.parseInt(env.getProperty("parseSize"));
        Text text=quickSpringRepository.createText(new Text("texts","title",true,null,0));
        assertTrue(textServices.getIndexReadText().equals("text"));
        //check to ensure if there is not enough characters that they are properly padded
        text.setIndex(parseSize);
        assertTrue(textServices.getIndexReadText().equals("s   "));
    }

    @Test
    @Transactional
    public void getSelect(){
        Text text=quickSpringRepository.createText(new Text("texts","title",true,null,0));
        assertEquals(textServices.getSelect().getTextIdToSelect(),text.getId());
    }
}