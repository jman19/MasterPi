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
                texts.add(new TextWrapper(t.getId(),t.getTitle()));
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
        for (TextWrapper t:categoryInput.getTexts()){
            Text text=quickRepository.getText(t.getId());
            if(text==null){
                throw new CustomRunTimeException("text to add id: "+t.getId()+" not found", HttpStatus.BAD_REQUEST);
            }
            else{
                textsToAdd.add(text);
            }
        }

        category.setTexts(textsToAdd);

        return quickRepository.createCategory(category);

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
     * This deletes a category given its id if deleteContents is true it will delete all text associated with the
     * category as well if it is false the text will simply go to being unorganized
     * @param id
     * @param deleteContents
     */
    void deleteCategory(Long id,Boolean deleteContents){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }

        if(deleteContents==true){
            for (Text t:category.getTexts()){
                quickRepository.deleteText(t.getId());
            }
        }
        quickRepository.deleteCategory(id);
    }

    /***
     * add a single text to a existing category
     * @param id
     * @param textToAdd
     */
    public void addTextToCategory(Long id,TextWrapper textToAdd){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }

        Text text=quickRepository.getText(textToAdd.getId());
        if(text==null){
            throw new CustomRunTimeException("text to add id: "+textToAdd.getId()+" not found", HttpStatus.BAD_REQUEST);
        }

        category.getTexts().add(text);
        quickRepository.createCategory(category);

    }

    /***
     * remove a single text from a existing category
     * @param id
     * @param textToRemove
     */
    public void removeTextFromCategory(Long id,TextWrapper textToRemove){
        Category category=quickRepository.getCategory(id);
        if(category==null){
            throw new CustomRunTimeException("category not found",HttpStatus.NOT_FOUND);
        }

        for (Text t:category.getTexts()){
            if(t.getId()==textToRemove.getId()){
                quickRepository.deleteText(t.getId());
            }
        }

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
