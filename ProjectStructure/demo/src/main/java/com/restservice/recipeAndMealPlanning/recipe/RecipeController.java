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


//    @GetMapping("/setupSetpsColumn")
//    public String setupSetpsColumn() {
//         try {
//           loadStepsColumn();
//         }
//            catch (Exception e) {
//                e.printStackTrace();
//                return "DB setup failed!";
//            }
//    }

    private void loadDB() throws Exception{
        Reader in = new FileReader("recipes.csv");
        String[] headers = {"RecipeId", "Name", "AuthorId", "AuthorName", "CookTime", "PrepTime", "TotalTime", "DatePublished", "Description", "ImageList", "RecipeCategory", "Keywords", "RecipeIngredientQuantity", "RecipeIngredientParts", "RecipeIngredientPrintString", "AggregatedRating", "ReviewCount", "Calories", "FatContent", "SaturatedFatContent", "CholesterolContent", "SodiumContent", "CarbohydrateContent", "FiberContent", "SugarContent", "ProteinContent", "RecipeServings", "RecipeYield", "RecipeInstructions"};

        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();

        Iterable<CSVRecord> records = format.parse(in);
        Integer count = 0;
        Integer success = 0;
        Integer fail = 0;

        for(CSVRecord record : records){
            count++;
            if(count >= 10) break;

            try {
                //if( Integer.parseInt(record.get("RecipeId")) > 2 ) return; //for testing purposes, only load first 5 recipes
                //set description to LONGTEXT, clear and reset the db

                ArrayList<String> keywordsList = new ArrayList<>(Arrays.asList(record.get("Keywords").trim().replace("[", "").replace("]", "").split(",")));
                ArrayList<String> temp = new ArrayList<>(Arrays.stream(record.get("RecipeIngredientQuantity").trim().replace("[", "").replace("]", "").split(",")).toList());
                ArrayList<Float> recipeIngredientQuantityList = new ArrayList<>();
                for (String s : temp) {
                    recipeIngredientQuantityList.add(parseFraction(s));
                }

                ArrayList<String> recipeIngredientPartsList = new ArrayList<>(Arrays.asList(record.get("RecipeIngredientParts").trim().replace("[", "").replace("]", "").split(",")));
                System.out.println("RecipeId: " + record.get("RecipeId"));
                //System.out.println("Ingredients: " + recipeIngredientPartsList);
                //System.out.println("Quantities: " + recipeIngredientQuantityList);

                ArrayList<String> recipeInstructions = new ArrayList<>(Arrays.asList(record.get("RecipeInstructions").trim().replace("[", "").replace("]", "").split("\\.")));
                HashMap<Integer, String> recipeInstructionsMap = new HashMap<>();
                for (int i = 0; i < recipeInstructions.size(); i++) {
                    String instruction = recipeInstructions.get(i).trim();
                    if (instruction.startsWith(",")) {
                        instruction = instruction.substring(1);
                        instruction = instruction.trim();
                        recipeInstructions.set(i, instruction); // Update the list
                    }
                    recipeInstructionsMap.put(i, instruction);
                }
                //TODO: add recipeInstructions to the recipe object

                Recipe recipe = new Recipe(
                        Integer.parseInt(record.get("RecipeId")),
                        record.get("Name"),
                        Integer.parseInt(record.get("AuthorId")),
                        record.get("AuthorName"),
                        (Objects.equals(record.get("CookTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("CookTime"))),
                        (Objects.equals(record.get("PrepTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("PrepTime"))),
                        (Objects.equals(record.get("TotalTime"), "") ? Duration.ofMinutes(-1) : Duration.parse(record.get("TotalTime"))),
                        record.get("DatePublished"),
                        record.get("Description"),
                        record.get("RecipeCategory"),
                        new ArrayList<>(keywordsList),
                        new ArrayList<>(recipeIngredientQuantityList),
                        new ArrayList<>(recipeIngredientPartsList),
                        null,
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
                        new HashMap<Integer, String>(recipeInstructionsMap)
                );

                if( recipe.getRecipeId() == null) throw new Exception("Something went wrong building the recipe object for :" + record.get("RecipeId") );


                recipeRepository.save(recipe);
                System.out.println("Recipe added: " + recipe.getRecipeId());
                success++;
            } catch (Exception e) {
                System.out.println("Error adding recipe: " + record.get("RecipeId") + " ");
                fail++;

            };
        }
        System.out.println("Total recipes: " + count);
        System.out.println("Success: " + success);
        System.out.println("Fail: " + fail);


    }


    private static float parseFraction(String fraction) throws Exception {
        if(fraction == null || fraction.contains("NULL") || fraction.isBlank() ) return -1.0f;

        String[] parts = fraction.split(" ");
        Float sum = 0.0f;
        for (String part : parts) {
            if(part == null || part.isBlank()) continue;
            if(parseFractionalPart(part) != -1.0f)
                sum += parseFractionalPart(part);
            else return -1.0f;

        }
        return sum;
    }

    private static float parseFractionalPart(String fraction) throws Exception {
        String[] parts = null;
        if(fraction.contains("⁄"))
            parts = fraction.split("⁄");
        else if(fraction.contains("/"))
            parts = fraction.split("/");
        else
            return Float.parseFloat(fraction);

        if (parts.length != 1) {
            float numerator = Float.parseFloat(parts[0]);
            float denominator = Float.parseFloat(parts[1]);
            return numerator / denominator;
        } else {
            // If the string is not a fraction, try to parse it as a regular float
            return Float.parseFloat(fraction);
        }
    }


//    private void loadStepsColumn() throws Exception {
//        Reader in = new FileReader("recipesWithSteps.csv");
//        String[] headers = {"RecipeId", "Name", "AuthorId", "AuthorName", "CookTime", "PrepTime", "TotalTime", "DatePublished", "Description", "ImageList", "RecipeCategory", "Keywords", "RecipeIngredientQuantity", "RecipeIngredientParts", "RecipeIngredientPrintString", "ReviewCount", "Calories", "FatContent", "SaturatedFatContent", "CholesterolContent", "SodiumContent", "CarbohydrateContent", "FiberContent", "SugarContent", "ProteinContent", "RecipeServings", "RecipeYield", "RecipeInstructions"};
//
//        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();
//
//        Iterable<CSVRecord> records = format.parse(in);
//    }


}
