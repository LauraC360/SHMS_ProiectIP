package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

public interface RecipeUserPreferencesRepository extends JpaRepository<RecipeUserPreferences, Integer> {
    boolean existsById(Integer id);

    RecipeUserPreferences getById(Integer id);

    void deleteById(Integer id);

}