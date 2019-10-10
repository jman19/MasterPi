package com.masterPi.ExceptionHandler;

import com.masterPi.resources.BodyMessage;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice(annotations = RestController.class)
public class CustomExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity generalException(final Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BodyMessage("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler({CustomRunTimeException.class})
    public ResponseEntity runTimeException(final CustomRunTimeException e){
        e.printStackTrace();
        return ResponseEntity.status(e.getError()).body(e.getMessage());
    }
}
