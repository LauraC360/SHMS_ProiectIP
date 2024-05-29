package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeUserPreferencesService {

    @Autowired
    private final RecipeUserPreferencesRepository repository;

    //@Autowired
    public RecipeUserPreferencesService(RecipeUserPreferencesRepository repository) {
        this.repository = repository;
    }

    public List<RecipeUserPreferences> getAllPreferences() {
        return repository.findAll();
    }

    public RecipeUserPreferences getPreferencesById(int id) {

        Optional<RecipeUserPreferences> preferences = repository.findById(id);
        if (preferences.isEmpty()) {
            return null;
        }
        return repository.findById(id).get();
    }

    public RecipeUserPreferences savePreferences(RecipeUserPreferences preferences) {
        return repository.save(preferences);
    }

    /// method to add a recipe id to the Liked Recipes list
    public RecipeUserPreferences addLikedRecipe(int id, int recipeId) {
        RecipeUserPreferences preferences = getPreferencesById(id);
        preferences.getLikedRecipes().add(recipeId);
        return savePreferences(preferences);
    }

    public void deletePreferences(int id) {
        repository.deleteById(id);
    }

    public void printPreferences() {
        List<RecipeUserPreferences> preferences = getAllPreferences();
        for (RecipeUserPreferences preference : preferences) {
            System.out.println(preference);
        }
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }
}