package com.masterPi.controller;
import com.masterPi.data.impl.SelectedText;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.BodyMessage;
import com.masterPi.resources.SelectTextInput;
import com.masterPi.resources.TextInput;
import com.masterPi.resources.TextListWrapper;
import com.masterPi.services.TextServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
public class TextController {
    private TextServices textServices;

    public TextController(TextServices textServices) {
        this.textServices = textServices;
    }

    @CrossOrigin("*")
    @PostMapping("/create/text")
    @ApiOperation(value = "this endpoint is used to save text into System", response = BodyMessage.class)
    public ResponseEntity createText(@RequestBody TextInput input){
        textServices.createText(input.getTitle(),input.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).body(new BodyMessage("saved text",HttpStatus.CREATED.value()));
    }

    @CrossOrigin("*")
    @PutMapping("/edit/text/{id}")
    @ApiOperation(value = "this endpoint is used to edit existing text", response = Text.class)
    public ResponseEntity editText(@PathVariable Long id, @RequestBody TextInput input){
        return ResponseEntity.ok(textServices.editText(id,input.getTitle(),input.getBody()));
    }

    @CrossOrigin("*")
    @PutMapping("/selectText")
    @ApiOperation(value = "this endpoint is used select the text for use", response = BodyMessage.class)
    public ResponseEntity selectText(@RequestBody SelectTextInput input){
        textServices.selectText(input.getTextIdToSelect());
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage("selected text successful",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @GetMapping("/selectText")
    @ApiOperation(value = "this endpoint is used to get the selected text id", response = SelectedText.class)
    public ResponseEntity getSelectTextId(){
        return ResponseEntity.ok(textServices.getSelect());
    }

    @CrossOrigin("*")
    @GetMapping("/getText/{id}")
    @ApiOperation(value = "this endpoint is used to get full text content", response = Text.class)
    public ResponseEntity getText(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(textServices.getText(id));
    }

    @CrossOrigin("*")
    @DeleteMapping("/deleteText")
    @ApiOperation(value = "this endpoint is used to delete text content", response = BodyMessage.class)
    public ResponseEntity deleteText(@RequestBody SelectTextInput input){
        textServices.deleteText(input.getTextIdToSelect());
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage("selected text deleted successfully",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @GetMapping("/getAllTexts")
    @ApiOperation(value = "this endpoint is used to basic summary of all stored texts", response = TextListWrapper.class)
    public ResponseEntity getAllTexts(){
        return ResponseEntity.status(HttpStatus.OK).body(new TextListWrapper(textServices.getAllStoredText()));
    }

    @CrossOrigin("*")
    @PutMapping("/next")
    @ApiOperation(value = "this endpoint increments current reading index", response = BodyMessage.class)
    public ResponseEntity next(){
        textServices.updateIndexNext();
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage("index updated",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @PutMapping("/prev")
    @ApiOperation(value = "this endpoint decrements current reading index", response = BodyMessage.class)
    public ResponseEntity prev(){
        textServices.updateIndexPrev();
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage("index updated",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @PutMapping("/reset")
    @ApiOperation(value = "this endpoint resets reading index to start", response = BodyMessage.class)
    public ResponseEntity reset(){
        textServices.reset();
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage("index reset",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @GetMapping("/getCurrentReading")
    @ApiOperation(value = "this endpoint gets the current text slice index is reading", response = BodyMessage.class)
    public ResponseEntity getIndexReadText(){
        return ResponseEntity.status(HttpStatus.OK).body(new BodyMessage(textServices.getIndexReadText(),HttpStatus.OK.value()));
    }
}
