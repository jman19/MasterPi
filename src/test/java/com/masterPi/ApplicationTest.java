package com.masterPi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.masterPi.resources.TextInput;
import com.masterPi.services.SlavePIServices;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlavePIServices slavePIServices;

    @Test
    public void init(){
    }

    //Distributed systems test below

    @Test
    @Transactional
    public void createTextEndpointTest() throws Exception{
        mockMvc.perform(post("/create/text").contentType(MediaType.APPLICATION_JSON).
                content(convertToJson(new TextInput("title","body"))))
                .andExpect(status().isCreated());
    }


    private String convertToJson(Object o) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}