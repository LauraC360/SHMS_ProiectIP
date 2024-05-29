package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipeUserPreferences")
public class RecipeUserPreferencesController {

    @Autowired
    private final RecipeUserPreferencesService service;

    @Autowired
    private final RecipeUserPreferencesRepository repository;


    public RecipeUserPreferencesController(RecipeUserPreferencesService service, RecipeUserPreferencesRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // testing the connection to the database on the browser
    // Tested with Postman (GET Request: http://localhost:8080/recipeUserPreferences/getAllPreferences)
    @GetMapping(value="/getAllPreferences", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecipeUserPreferences> getAllPreferences() {
        return service.getAllPreferences();
    }

}