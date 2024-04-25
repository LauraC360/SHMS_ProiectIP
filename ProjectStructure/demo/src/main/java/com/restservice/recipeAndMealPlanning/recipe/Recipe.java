package com.restservice.recipeAndMealPlanning.recipe;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private static final Recipe defaultRecipe = new Recipe(
            "Pasta",
            1,
            "John Doe",
            Duration.ofMinutes(30),
            Duration.ofMinutes( 10),
            Duration.ofMinutes( 40),
            "2021-09-01",
            "A simple pasta recipe",
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
            new ArrayList<String>()

    );

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/default")
    public Recipe getDefaultRecipe() {
        recipeRepository.save(defaultRecipe);//saving in the db test! works!
        return defaultRecipe;
    }

    @GetMapping("/setupDB")
    public String setupDB() {

        try {
            loadDB();
        } catch (Exception e) {
            e.printStackTrace();
            return "DB setup failed!";
        }

        return "DB setup done!";
    }


    private void loadDB() throws Exception{
        Reader in = new FileReader("recipes.csv");
        String[] headers = {"RecipeId", "Name", "AuthorId", "AuthorName", "CookTime", "PrepTime", "TotalTime", "DatePublished", "Description", "ImageList", "RecipeCategory", "Keywords", "RecipeIngredientQuantity", "RecipeIngredientParts", "RecipeIngredientPrintString", "ReviewCount", "Calories", "FatContent", "SaturatedFatContent", "CholesterolContent", "SodiumContent", "CarbohydrateContent", "FiberContent", "SugarContent", "ProteinContent", "RecipeServings", "RecipeYield", "RecipeInstructions"};

        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();

        Iterable<CSVRecord> records = format.parse(in);

        for(CSVRecord record : records){

            /*
            if(record.get("RecipeId").equals("146") || record.get("RecipeId").equals("182")
                || record.get("RecipeId").equals("242") || record.get("RecipeId").equals("301")
                || record.get("RecipeId").equals("447") || record.get("RecipeId").equals("605")
                || record.get("RecipeId").equals("643") || record.get("RecipeId").equals("1127")
                || record.get("RecipeId").equals("1355") || record.get("RecipeId").equals("
            ) continue;*/
            try {
                if( recipeRepository.existsById(Integer.parseInt(record.get("RecipeId"))) ) continue;
                //set description to LONGTEXT, clear and reset the db

                Recipe recipe = new Recipe(
                        record.get("Name"),
                        Integer.parseInt(record.get("AuthorId")),
                        record.get("AuthorName"),
                        (Objects.equals(record.get("CookTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("CookTime"))),
                        (Objects.equals(record.get("PrepTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("PrepTime"))),
                        (Objects.equals(record.get("TotalTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("TotalTime"))),
                        record.get("DatePublished"),
                        record.get("Description"),
                        record.get("RecipeCategory"),
                        new ArrayList<String>(),
                        new ArrayList<Float>(),
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        (Objects.equals(record.get("ReviewCount"), "") ? -1.0f : Float.parseFloat(record.get("ReviewCount"))),
                        (Objects.equals(record.get("Calories"), "") ? -1.0f : Float.parseFloat(record.get("Calories"))),
                        (Objects.equals(record.get("FatContent"), "") ? -1.0f : Float.parseFloat(record.get("FatContent"))),
                        (Objects.equals(record.get("SaturatedFatContent"), "") ? -1.0f : Float.parseFloat(record.get("SaturatedFatContent"))),
                        (Objects.equals(record.get("CholesterolContent"), "") ? -1.0f : Float.parseFloat(record.get("CholesterolContent"))),
                        (Objects.equals(record.get("SodiumContent"), "") ? -1.0f : Float.parseFloat(record.get("SodiumContent"))),
                        (Objects.equals(record.get("CarbohydrateContent"), "") ? -1.0f : Float.parseFloat(record.get("CarbohydrateContent"))),
                        (Objects.equals(record.get("FiberContent"), "") ? -1.0f : Float.parseFloat(record.get("FiberContent"))),
                        (Objects.equals(record.get("SugarContent"), "") ? -1.0f : Float.parseFloat(record.get("SugarContent"))),
                        (Objects.equals(record.get("ProteinContent"), "") ? -1.0f : Float.parseFloat(record.get("ProteinContent"))),
                        (Objects.equals(record.get("RecipeServings"), "") ? -1.0f : Float.parseFloat(record.get("RecipeServings"))),
                        record.get("RecipeYield"),
                        new ArrayList<String>()
                );
                recipeRepository.save(recipe);
                System.out.println("Recipe added: " + recipe.getRecipeId());
            } catch (Exception e) {
                System.out.println("Error adding recipe: " + record.get("RecipeId"));
            };
        }


    }
}
