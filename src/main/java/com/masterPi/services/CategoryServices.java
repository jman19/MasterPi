package com.masterPi.services;

import com.masterPi.ExceptionHandler.CustomRunTimeException;
import com.masterPi.data.QuickRepository;
import com.masterPi.data.impl.Category;
import com.masterPi.data.impl.Text;
import com.masterPi.resources.CategoryInput;
import com.masterPi.resources.CategoryListWrapper;
import com.masterPi.resources.CategoryWrapper;
import com.masterPi.resources.TextWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServices {
    private QuickRepository quickRepository;
    public CategoryServices(QuickRepository quickRepository){
        this.quickRepository=quickRepository;
    }

    /***
     * This method gets all categories in a minimal form to save bandwidth
     * @return list of categories
     */
    public CategoryListWrapper getAllCategories(){
        List<CategoryWrapper> categories=new ArrayList<>();

        for (Category c:quickRepository.getAllCategory()){
            List<TextWrapper> texts=new ArrayList<>();
            for (Text t:c.getTexts()){
                texts.add(new TextWrapper(t.getId(),t.getTitle(),t.getTimeStamp(),c.getName()));
            }
            categories.add(new CategoryWrapper(c.getId(),c.getName(),c.getDescription(),texts));
        }
        return new CategoryListWrapper(categories);
    }

    /***
     * This method creates a new category it can be created with texts already added or empty
     * @param categoryInput
     * @return the category that was created
     */
    public Category createCategory(CategoryInput categoryInput){
        Category category=new Category();
        category.setDescription(categoryInput.getDescription());
        category.setName(categoryInput.getName());
        List<Text> textsToAdd=new ArrayList<>();
        for (Long textId:categoryInput.getTexts()){
            Text text=quickRepository.getText(textId);
            if(text==null){
                throw new CustomRunTimeException("text to add id: "+textId+" not found", HttpStatus.BAD_REQUEST);
            }
            else{
                text.setCategory(category);
                textsToAdd.add(text);
            }
        }

        category.setTexts(textsToAdd);
        Category createdCategory=quickRepository.createCategory(category);
        //save text that have category set
        for (Text text:textsToAdd){
            quickRepository.createText(text);
        }
        return createdCategory;

    }

    /***
     * Get the full information for a specific category
     * @param id of the category
     * @return category
     */
    public Category getCategory(Long id){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }
        return category;
    }

    /***
     * This deletes a category given its id if deleteContents is true delete the text under the category as well
     * @param id
     * @param deleteContents
     */
    public void deleteCategory(Long id,Boolean deleteContents){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }
        List<Text> texts=category.getTexts();
        category.setTexts(new ArrayList<>());

        if(deleteContents==false) {
            for (Text t : texts) {
                t.setCategory(null);
                quickRepository.createText(t);
            }
        }else {
            for(Text t:texts){
                quickRepository.deleteText(t.getId());
            }
        }
        quickRepository.createCategory(category);
        quickRepository.deleteCategory(id);
    }

    /***
     * add a single text to a existing category
     * @param id
     * @param textToAddId
     */
    public void addTextToCategory(Long id,Long textToAddId){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }

        Text text=quickRepository.getText(textToAddId);
        if(text==null){
            throw new CustomRunTimeException("text to add id: "+textToAddId+" not found", HttpStatus.BAD_REQUEST);
        }

        category.getTexts().add(text);
        text.setCategory(category);
        quickRepository.createCategory(category);
        quickRepository.createText(text);

    }

    /***
     * remove a single text from a existing category
     * @param id
     * @param textToRemoveId
     */
    public void removeTextFromCategory(Long id,Long textToRemoveId){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }

        for (int i=0;i<category.getTexts().size();i++){
            if(category.getTexts().get(i).getId()==textToRemoveId){
                category.getTexts().get(i).setCategory(null);
                quickRepository.createText(category.getTexts().get(i));
            }
        }
        category.getTexts().removeIf(t->t.getId()==textToRemoveId);
        quickRepository.createCategory(category);
    }

    /***
     * edit the category name and description
     * @param id (the id of the category to edit)
     * @param name
     * @param description
     * @return updated category
     */
    public Category editCategory(Long id, String name, String description){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }
        category.setName(name);
        category.setDescription(description);
        return quickRepository.createCategory(category);
    }
}
