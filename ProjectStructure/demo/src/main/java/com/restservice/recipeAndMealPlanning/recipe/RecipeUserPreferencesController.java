package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipeUserPreferences")
public class RecipeUserPreferencesController {

    private final RecipeUserPreferencesService service;

    @Autowired
    public RecipeUserPreferencesController(RecipeUserPreferencesService service) {
        this.service = service;
    }

    // testing the connection to the database on the browser
    @GetMapping
    public List<RecipeUserPreferences> getAllPreferences() {
        return service.getAllPreferences();
    }
}