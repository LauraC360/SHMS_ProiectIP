package com.restservice.recipeAndMealPlanning.recipe;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.time.Duration;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping("/api/v1/recipes/")
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


    @GetMapping("/getRecipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        return recipeRepository.findById(id).orElse(null);
    }


    @GetMapping("/getAIRecipeRecommendation/{title}")
    public Recipe getRecipeRecommendation(@PathVariable String title) {
        return recipeService.getAIRecipeRecommendation(title);
    }

    /**
     *
     * @param pageDTO : Integer pageNo, Integer pageSize, Sort.Direction sortDirection {Sort.Direction.ASC, Sort.Direction.DESC}, String sortByColumn <nameOfRecipePropriety>
     *                defaults: pageNo = 0, pageSize = 10, sortDirection = Sort.Direction.ASC, sortByColumn = "recipeId"
     *                if you only give some of the parameters, the rest will be set to the default values! <3
     * @return Page<Recipe>
     */
    @GetMapping("/recipePage")
    public Page<Recipe> getRecipePage(@RequestBody PageDTO pageDTO) {
        Pageable page = pageDTO.getPageable(pageDTO);
        return recipeRepository.findAll(page);
    }

    //so i don't accidentally set up again; will b uncommented when ill b working with db population again
    /*
    @GetMapping("/setupDB")
    public String setupDB() {
        try {
            recipeService.importDB();
        } catch (Exception e) {
            e.printStackTrace();
            return "DB setup failed!";
        }

        return "DB setup done!";
    }*/






}
