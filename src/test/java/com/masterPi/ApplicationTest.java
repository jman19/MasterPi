package com.masterPi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.masterPi.data.QuickRepository;
import com.masterPi.data.impl.Category;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.*;
import com.masterPi.services.SlavePIServices;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuickRepository quickSpringRepository;

    @MockBean
    private SlavePIServices slavePIServices;

    @Test
    public void init(){
    }

    //Distributed systems test below

    //create text basic flow
    @Test
    @Transactional
    public void createTextFlow() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/create/text").contentType(MediaType.APPLICATION_JSON).
                content(convertToJson(new TextInput("title","body"))))
                .andExpect(status().isCreated()).andReturn();

        MvcResult result=mockMvc.perform(get("/getAllTexts")).andExpect(status().isOk()).andReturn();
        TextListWrapper texts = mapper.readValue(result.getResponse().getContentAsString(), TextListWrapper.class);
        assertTrue(texts.getTextList().get(0).getTitle().equals("title"));

        Mockito.when(slavePIServices.translate(any(String.class))).thenReturn(new BodyMessage("success translating text", 200));
        mockMvc.perform(put("/selectText").contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(new SelectTextInput(texts.getTextList().get(0).getId())))).andExpect(status().isOk());

    }

    //organization basic flow
    @Test
    @Transactional
    public void organizationFlow() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        Text text = quickSpringRepository.createText(new Text("text", "title", false, null, 0));
        MvcResult createdCategory=mockMvc.perform(post("/organization/create/category").contentType(MediaType.APPLICATION_JSON).
                content(convertToJson(new CategoryInput("testCategory","this is a test",new ArrayList<>()))))
                .andExpect(status().isCreated()).andReturn();

        Category category=mapper.readValue(createdCategory.getResponse().getContentAsString(),Category.class);
        assertTrue(category.getName().equals("testCategory"));
        assertTrue(category.getDescription().equals("this is a test"));
        assertEquals(0,category.getTexts().size());

        mockMvc.perform(patch("/organization/category/"+category.getId()+"/addText/"+text.getId())).andExpect(status().isOk());

        MvcResult categories=mockMvc.perform(get("/organization/categories")).andExpect(status().isOk()).andReturn();
        CategoryListWrapper categoryListWrapper=mapper.readValue(categories.getResponse().getContentAsString(),CategoryListWrapper.class);
        assertTrue(categoryListWrapper.getCategories().get(0).getName().equals("testCategory"));
        assertTrue(categoryListWrapper.getCategories().get(0).getDescription().equals("this is a test"));
        assertTrue(categoryListWrapper.getCategories().get(0).getTexts().get(0).getTitle().equals("title"));

    }

    //invalid input error test
    @Test
    @Transactional
    public void invalidDeleteTextDoesntExist() throws Exception{
        mockMvc.perform(delete("/deleteText/0")).andExpect(status().isNotFound());
    }

    //invalid edit text request
    @Test
    @Transactional
    public void editTextDoesntExist() throws Exception{
        mockMvc.perform(put("/edit/text/0").contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(new TextInput("title","body")))).andExpect(status().isNotFound());
    }

    private String convertToJson(Object o) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}