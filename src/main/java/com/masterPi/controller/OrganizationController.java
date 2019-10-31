package com.masterPi.controller;

import com.masterPi.data.impl.Category;
import com.masterPi.resources.*;
import com.masterPi.services.CategoryServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationController {
    private CategoryServices categoryServices;

    public OrganizationController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @CrossOrigin("*")
    @GetMapping("/organization/categories")
    @ApiOperation(value="gets all categories in a minimal form to save bandwidth", response = CategoryListWrapper.class)
    public ResponseEntity getAllCategories(){
        return ResponseEntity.ok(categoryServices.getAllCategories());
    }

    @CrossOrigin("*")
    @PostMapping("/organization/create/category")
    @ApiOperation(value="creates a new category it can be created with texts already added or empty (text array is id of texts to add on creation)", response = Category.class)
    public ResponseEntity createCategory(@RequestBody CategoryInput categoryInput){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryServices.createCategory(categoryInput));
    }

    @CrossOrigin("*")
    @GetMapping("/organization/category/{id}")
    @ApiOperation(value="Get the full information for a specific category", response=Category.class)
    public ResponseEntity getCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryServices.getCategory(id));
    }

    @CrossOrigin("*")
    @DeleteMapping("/organization/category/{id}")
    @ApiOperation(value="This deletes a category given its id if deleteContents is true it will delete all text associated if false text will simply go to being unorganized", response = BodyMessage.class)
    public ResponseEntity deleteCategory(@PathVariable Long id,@RequestParam Boolean deleteContents){
        categoryServices.deleteCategory(id,deleteContents);
        return ResponseEntity.ok(new BodyMessage("category deleted",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @PatchMapping("/organization/category/{id}/addText/{textId}")
    @ApiOperation(value="add a single text to a existing category", response = BodyMessage.class)
    public ResponseEntity addTextToCategory(@PathVariable Long id, @PathVariable Long textId){
        categoryServices.addTextToCategory(id,textId);
        return ResponseEntity.ok(new BodyMessage("text added to category",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @PatchMapping("/organization/category/{id}/removeText/{textId}")
    @ApiOperation(value="remove a single text from a existing category", response = BodyMessage.class)
    public ResponseEntity removeTextFromCategory(@PathVariable Long id, @PathVariable Long textId){
        categoryServices.removeTextFromCategory(id,textId);
        return ResponseEntity.ok(new BodyMessage("text removed from category",HttpStatus.OK.value()));
    }

    @CrossOrigin("*")
    @PatchMapping("/organization/category/{id}")
    @ApiOperation(value="edit the category name and description", response = Category.class)
    public ResponseEntity editCategory(@PathVariable Long id, @RequestBody CategoryEditInput editInput){
        return ResponseEntity.ok(categoryServices.editCategory(id,editInput.getName(),editInput.getDescription()));
    }
}
