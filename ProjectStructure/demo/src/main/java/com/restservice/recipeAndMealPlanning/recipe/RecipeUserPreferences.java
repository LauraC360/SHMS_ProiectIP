package com.restservice.recipeAndMealPlanning.recipe;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class RecipeUserPreferences {
    @Id
    @Column(name = "id")
    private int id;//user id

    @ElementCollection
    @CollectionTable(name = "recipe_user_preferences_viewed_recipes", joinColumns = @JoinColumn(name = "recipe_user_preferences_id"))
    //@Column(name = "recipe_user_preferences_id")
    protected List<Integer> viewedRecipes;

    @ElementCollection
    @CollectionTable(name = "recipe_user_preferences_liked_recipes", joinColumns = @JoinColumn(name = "recipe_user_preferences_id"))
    //@Column(name = "recipe_user_preferences_id")
    protected List<Integer> likedRecipes;

    @ElementCollection
    @CollectionTable(name = "recipe_user_preferences_history", joinColumns = @JoinColumn(name = "recipe_user_preferences_id"))
    //@Column(name = "recipe_user_preferences_id")
    protected List<Integer> history;

    public RecipeUserPreferences() {}

    public RecipeUserPreferences(int id, List<Integer> viewedRecipes, List<Integer> likedRecipes, List<Integer> history) {
        this.id = id;
        this.viewedRecipes = viewedRecipes == null ? new ArrayList<>() : new ArrayList<>(viewedRecipes);
        this.likedRecipes = likedRecipes == null ? new ArrayList<>() : new ArrayList<>(likedRecipes);
        this.history = history == null ? new ArrayList<>(): new ArrayList<>(history);
    }

    protected void addLikedRecipe(int recipeId) {
        if(!likedRecipes.contains(recipeId))
            likedRecipes.add(recipeId);
    }


    protected void addViewedRecipe(int recipeId) {
        if(!viewedRecipes.contains(recipeId))
            viewedRecipes.add(recipeId);
    }

    protected void addHistory(int recipeId) {
        if(!history.contains(recipeId))
            history.add(recipeId);
    }

    protected void removeLikedRecipe(int recipeId) {
        likedRecipes.remove((Integer) recipeId);
    }

}