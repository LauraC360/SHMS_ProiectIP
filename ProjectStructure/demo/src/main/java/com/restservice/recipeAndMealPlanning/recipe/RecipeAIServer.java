package com.restservice.recipeAndMealPlanning.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restservice.shoppingListAndInventory.inventory.Ingredient;
import com.restservice.shoppingListAndInventory.inventory.Quantity;
import com.restservice.shoppingListAndInventory.inventory.QuantityType;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class RecipeAIServer {


    public Recipe generateRecipe(String recipeFormat, String input, String mainIngredients) {
        // Define the path to the Python script
        String scriptPath = "recipe_script.py"; // Update this path

        // Path to the Python interpreter in the virtual environment
        String pythonExecutable = "venv/Scripts/python.exe"; // Update this path

        // Define the arguments to pass to the script
//        recipeFormat = "youtube";
//        input = "apple pie recipe";
//        mainIngredients = "apple";

        // Split mainIngredients into separate ingredients
        String[] ingredients = mainIngredients.split(", ");

        // Build the command
        String[] command = new String[4 + ingredients.length];
        command[0] = pythonExecutable; // Use virtual environment's Python interpreter
        command[1] = scriptPath;
        command[2] = recipeFormat;
        command[3] = input;

        // Add each ingredient as a separate argument
        for (int i = 0; i < ingredients.length; i++) {
            command[4 + i] = ingredients[i];
        }

        try {
            // Create a ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append(System.lineSeparator());
            }

            // Wait for the process to complete
            //int exitCode = process.waitFor();
            //if (exitCode != 0) {
            //System.out.println("Error! RecipeAI wasn't able to generate a recipe for you...");
            //return null;
            //} else {
            // Here's your output
            System.out.println(output.toString());

            if (output.toString().equals("Error at generating recipe...")) {
                System.out.println("Error! RecipeAI wasn't able to generate a recipe for you...");
                return null;
            }
            // TODO Parse the output and create the generated recipe object : Then handle what to do with object
            // for text format type of recipe
            if (recipeFormat.equals("text")) {
                // Parse the JSON output and create the generated recipe object
                Recipe recipe = parseRecipeOutput(output.toString(), input);
                displayRecipe(recipe.toString());
                return recipe;
            } else if (recipeFormat.equals("youtube")) {
                // Parse the youtube generated recipe
                // System.out.println("Youtube recipe generated: " + output.toString());
                Recipe recipe = parseRecipeOutput(output.toString(), input);
                displayRecipe(recipe.toString());
                return recipe;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
            //return null;
        }
//    } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    // TODO: Parse the output and generate recipe
    static Recipe parseRecipeOutput(String output, String input) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(output);

        Recipe recipe = new Recipe();
        recipe.setRecipeTitle(input); // Use the input string as the recipe title
        recipe.setAuthorId(1); // Set a default author ID or get it from JSON if available
        recipe.setAuthorName("Default Author"); // Set a default author name or get it from JSON if available
        recipe.setCookTime(Duration.ofMinutes(10)); // Set a default cook time or calculate it
        recipe.setPrepTime(Duration.ofMinutes(5)); // Set a default prep time or calculate it
        recipe.setTotalTime(recipe.getCookTime().plus(recipe.getPrepTime()));
        recipe.setDatePublished("2024-05-22"); // Set a default date or get it from JSON if available
        recipe.setDescription("Default Description"); // Set a default description or get it from JSON if available
        recipe.setImageList(new ArrayList<>()); // Initialize with an empty list or get it from JSON if available
        recipe.setCategory("Main Course"); // Set a default category or get it from JSON if available
        recipe.setKeywords(new ArrayList<>()); // Initialize with an empty list or get it from JSON if available

        List<Ingredient> ingredients = new ArrayList<>();

        JsonNode ingredientsNode = root.get("ingredients");
        for (JsonNode ingredientNode : ingredientsNode) {
            String name = ingredientNode.get("name").asText();

            String quantityStr = ingredientNode.get("quantity").asText();
            if (quantityStr.isEmpty()) {
                continue;
            } else if (quantityStr.equals("unknown")) {
                quantityStr = "1"; // or whatever default value you want to use
            } else if (quantityStr.equals("to taste")) {
                quantityStr = "1"; // or whatever default value you want to use
            }

            String[] parts = quantityStr.split(" ");
            float amount;
            String unit;

            if (parts.length == 2) {
                unit = parts[1];
                if (parts[0].equalsIgnoreCase("unknown")) {
                    amount = 1; // or whatever default value you want to use
                } else if (parts[0].contains("/")) {
                    String[] fractionParts = parts[0].split("/");
                    float numerator = Float.parseFloat(fractionParts[0]);
                    float denominator = Float.parseFloat(fractionParts[1]);
                    amount = numerator / denominator;
                } else {
                    amount = Float.parseFloat(parts[0]);
                }
            } else {
                unit = "";
                if (parts[0].equalsIgnoreCase("unknown")) {
                    amount = 1; // or whatever default value you want to use
                } else if (parts[0].contains("/")) {
                    String[] fractionParts = parts[0].split("/");
                    float numerator = Float.parseFloat(fractionParts[0]);
                    float denominator = Float.parseFloat(fractionParts[1]);
                    amount = numerator / denominator;
                } else {
                    amount = Float.parseFloat(parts[0]);
                }
            }

            QuantityType quantityType = QuantityType.fromPhrase(unit);
            Quantity quantity = new Quantity(amount, quantityType);
            Ingredient ingredient = new Ingredient(name, quantity);

            ingredients.add(ingredient);
        }

        recipe.setIngredients(ingredients);

        StringBuilder description = new StringBuilder();
        JsonNode instructionsNode = root.get("instructions");
        for (JsonNode instructionNode : instructionsNode) {
            description.append(instructionNode.asText()).append("\n");
        }
        recipe.setDescription(description.toString().trim());

        // Set remaining fields as needed, e.g., nutritional information, review count, etc.
        recipe.setReviewCount(0f); // Default value
        recipe.setCalories(0f); // Default value
        recipe.setFatContent(0f); // Default value
        recipe.setSaturatedFatContent(0f); // Default value
        recipe.setCholesterolContent(0f); // Default value
        recipe.setSodiumContent(0f); // Default value
        recipe.setCarbohydrateContent(0f); // Default value
        recipe.setFiberContent(0f); // Default value
        recipe.setSugarContent(0f); // Default value
        recipe.setProteinContent(0f); // Default value
        recipe.setRecipeServings(1f); // Based on 1 serving from the instructions
        recipe.setRecipeYield("1 serving"); // Default value

        return recipe;
    }

    static void displayRecipe(String recipe) {
        // Display the generated recipe
        System.out.println("Generated Recipe: ");
        System.out.println(recipe);
    }
}