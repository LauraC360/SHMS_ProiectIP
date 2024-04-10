//package com.restservice.recipeAndMealPlanning.recipe;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/recipes")
//public class RecipeController {
//
//    private List<Recipe> recipes = new ArrayList<>();
//    private Recipe recipe = new Recipe(1, "Pasta", "Pasta with tomato sauce", null, 4.7f, null);
//
//    @PostMapping
//    public void createRecipe() {
//        recipes.add(recipe);
//    }
//
//    @GetMapping
//    public List<Recipe> getAllRecipes() {
//        return recipes;
//    }
//}

package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private static final Recipe defaultRecipe = new Recipe(1, "Pasta", "Pasta with tomato sauce", new ArrayList<RecipeStep>(), 4.7f, null);

    @GetMapping("/default")
    public Recipe getDefaultRecipe() {
        return defaultRecipe;
    }
}