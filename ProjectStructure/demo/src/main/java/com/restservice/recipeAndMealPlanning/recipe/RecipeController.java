package com.restservice.recipeAndMealPlanning.recipe;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.time.Duration;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private static final Recipe defaultRecipe = new Recipe(
            1,
            "Pasta",
            1,
            "John Doe",
            Duration.ofMinutes(30),
            Duration.ofMinutes( 10),
            Duration.ofMinutes( 40),
            "2021-09-01",
            "A simple pasta recipe",
            new ArrayList<String>(),
            "Main Course",
            new ArrayList<String>(),
            new ArrayList<Float>(),
            new ArrayList<String>(),
            new ArrayList<String>(),
            0.0f,
            1000.0f,
            10.0f,
            5.0f,
            0.0f,
            0.0f,
            100.0f,
            10.0f,
            0.0f,
            0.0f,
            4f,
            "4 servings",
            new HashMap<>()

    );

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/default")
    public Recipe getDefaultRecipe() {
        recipeRepository.save(defaultRecipe);//saving in the db test! works!
        return defaultRecipe;
    }

    //so i dont accidentally set up again; will b uncommented when ill b working with db population again
    @GetMapping("/setupDB")
    public String setupDB() {
        try {
            recipeService.importDB();
        } catch (Exception e) {
            e.printStackTrace();
            return "DB setup failed!";
        }

        return "DB setup done!";
    }


    @GetMapping("/getRecipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        return recipeRepository.findById(id).orElse(null);
    }


    /*@GetMapping("/removeBadRecipes")
    public String removeBadRecipes() {
        try {
            recipeService.removeBadRecipes();
        } catch (Exception e) {
            e.printStackTrace();
            return "Bad recipes removed failed!";
        }

        return "Bad recipes removed done!";
    }*/


    /*@GetMapping("/removeColumns")
    public String removeColumns() {
        try {
            recipeService.deleteUnwantedColumns();
        } catch (Exception e) {
            e.printStackTrace();
            return "Columns removed failed!";
        }

        return "Columns removed done!";
    }*/



}
