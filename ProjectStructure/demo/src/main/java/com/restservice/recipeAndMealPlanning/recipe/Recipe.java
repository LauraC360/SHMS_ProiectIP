package com.restservice.recipeAndMealPlanning.recipe;

import com.restservice.shoppingListAndInventory.inventory.Ingredient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Recipe {
    protected Integer recipeId;
    protected String recipeTitle;
    protected List<RecipeStep> recipeStepsList = new ArrayList<>();
    protected Float rating;
    protected File recipeImage;
    protected String recipeDescription;

    public Recipe(Integer recipeId, String recipeTitle, String recipeDescription, List<RecipeStep> recipeStepsList, Float rating, File recipeImage) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeDescription = recipeDescription;
        this.recipeStepsList = recipeStepsList;
        this.rating = rating;
        this.recipeImage = recipeImage;
    }


    public void addRecipeStep(RecipeStep recipeStep) {
        this.recipeStepsList.add(recipeStep);
    }

    public void addRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeStepsList.addAll(recipeSteps);
    }

    public void addRecipeSteps(RecipeStep... recipeSteps) {
        for (RecipeStep recipeStep : recipeSteps) {
            this.recipeStepsList.add(recipeStep);
        }
    }

    public void removeRecipeStep(RecipeStep recipeStep) {
        this.recipeStepsList.remove(recipeStep);
    }

    public void removeRecipeStep(Integer index) {
        this.recipeStepsList.remove(index);
    }

    public void changeOrderOfRecipeStep(Integer index, Integer newIndex) {
        RecipeStep recipeStep = this.recipeStepsList.get(index);
        this.recipeStepsList.remove(index);
        this.recipeStepsList.add(newIndex, recipeStep);
    }

    public void clearRecipeSteps() {
        this.recipeStepsList.clear();
    }

    public List<Ingredient> getAllNecesaryIngredients() {
        List<Ingredient> allNecessaryIngredients = new ArrayList<>();
        recipeStepsList.forEach(recipeStep ->
                allNecessaryIngredients.forEach( ingredient -> {
                    if(!allNecessaryIngredients.contains(ingredient))
                        allNecessaryIngredients.add(ingredient);

                    else allNecessaryIngredients.stream().filter(ingredient1 -> ingredient1.equals(ingredient)).forEach(ingredient1 -> ingredient1.getProduct().addQuantity(ingredient.getProduct().getQuantity().getValue()));
                })
        );
        return allNecessaryIngredients;
    }

    public Duration getTotalDuration() {
        Duration totalDuration = Duration.ZERO;
        for (RecipeStep recipeStep : recipeStepsList) {
            totalDuration = totalDuration.plus(recipeStep.getStepDuration());
        }
        return totalDuration;
    }

};