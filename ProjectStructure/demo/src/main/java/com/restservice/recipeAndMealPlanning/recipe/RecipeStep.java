package com.restservice.recipeAndMealPlanning.recipe;

import com.restservice.shoppingListAndInventory.inventory.Ingredient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RecipeStep {
    private Duration stepDuration;
    private String stepDescription;
    private List<Ingredient> ingredientsNecessaryList = new ArrayList<>();

    public RecipeStep(Duration stepDuration, String stepDescription, List<Ingredient> ingredientsNecessaryList) {
        this.stepDuration = stepDuration;
        this.stepDescription = stepDescription;
        this.ingredientsNecessaryList = ingredientsNecessaryList;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredientsNecessaryList.add(ingredient);
    }

    public void addIngredients(List<Ingredient> ingredients) {
        this.ingredientsNecessaryList.addAll(ingredients);
    }

    public void addIngredients(Ingredient... ingredients) {
        for (Ingredient ingredient : ingredients) {
            this.ingredientsNecessaryList.add(ingredient);
        }
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredientsNecessaryList.remove(ingredient);
    }

    public void removeIngredients(Integer index) {
        this.ingredientsNecessaryList.remove(index);
    }

    public void clearIngredients() {
        this.ingredientsNecessaryList.clear();
    }

    public Ingredient getIngredient(Integer index) {
        return this.ingredientsNecessaryList.get(index);
    }

    public void setIngredient(Integer index, Ingredient ingredient) {
        this.ingredientsNecessaryList.set(index, ingredient);
    }

}