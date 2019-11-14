package com.masterPi.services;

import com.masterPi.ExceptionHandler.CustomRunTimeException;
import com.masterPi.resources.BodyMessage;
import com.masterPi.resources.TranslateInput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlavePIServices {
    private RestTemplate slavePi;

    public SlavePIServices(@Qualifier("slavePiRestTemplate") RestTemplate slavePi) {
        this.slavePi = slavePi;
    }

    /***
     * This method is used to access the slave PI endpoint translate
     * @param text
     * @return bodyMessage
     */
    public BodyMessage translate(String text){
        try{
            TranslateInput input=new TranslateInput(text);
            return slavePi.postForEntity("/translate",input,BodyMessage.class).getBody();
        }catch (Exception e){
            throw new CustomRunTimeException("Error talking with slave PI: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * This method is used to access the slave PI endpoint clear
     * @return bodyMessage
     */
    public BodyMessage clear(){
        try{
            return slavePi.postForEntity("/clear",null,BodyMessage.class).getBody();
        }catch (Exception e){
            throw new CustomRunTimeException("Error talking with slave PI: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
