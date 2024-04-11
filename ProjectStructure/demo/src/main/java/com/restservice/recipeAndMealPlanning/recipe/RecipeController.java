package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private static final Recipe defaultRecipe = new Recipe(1, "Pasta", "Pasta with tomato sauce", new ArrayList<RecipeStep>(), 4.7f, null);

    @GetMapping("/default")
    public Recipe getDefaultRecipe() {
        return defaultRecipe;
    }
}
