package com.masterPi.services;

import com.masterPi.data.impl.QuickSpringRepository;
import com.masterPi.data.impl.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TextServicesTest {

    @Autowired
    private QuickSpringRepository quickSpringRepository;
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
    public void editText() {
    }

    @Test
    public void selectText() {
    }

    @Test
    public void getText() {
    }

    @Test
    public void deleteText() {
    }

    @Test
    public void getAllStoredText() {
    }

    @Test
    public void updateIndexNext() {
    }

    @Test
    public void updateIndexPrev() {
    }

    @Test
    public void reset() {
    }

    @Test
    public void getIndexReadText() {
    }

    @Test
    public void getSelect() {
    }
}