package com.restservice.recipeAndMealPlanning.recipe;

import com.restservice.shoppingListAndInventory.inventory.Ingredient;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "recipe_user_preferences")
public class RecipeUserPreferences {
    @Id
    @Column(name = "id")
    private int id;

    @ElementCollection
    @Column(name = "ViewedRecipes")
    protected List<Integer> viewedRecipes;

    @ElementCollection
    @Column(name = "LikedRecipes")
    protected List<Integer> likedRecipes;

    @ElementCollection
    @Column(name = "History")
    protected List<Integer> history;

    @ElementCollection
    @Column(name = "OwnedIngredients")
    protected List<String> ownedIngredients;

    public RecipeUserPreferences() {}

    public RecipeUserPreferences(int id, List<Integer> viewedRecipes, List<Integer> likedRecipes, List<Integer> history, List<String> ownedIngredients) {
        this.id = id;
        this.viewedRecipes = viewedRecipes;
        this.likedRecipes = likedRecipes;
        this.history = history;
        this.ownedIngredients = ownedIngredients;
    }
}