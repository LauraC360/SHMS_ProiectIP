package com.restservice.recipeAndMealPlanning.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationSystem {


    private final RecipeRepository recipeRepository;

    @Autowired
    public RecommendationSystem(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public Map<Integer, Float> getCollaborativeRecommendations(int userId) {
        // Implement collaborative filtering here
        // Do collaborative filtering by finding the top 5 recipes that are most similar to the recipes the user has liked
        // Calculate similarity score between the recipes and return the top 5 recipes with the highest similarity score




        return new HashMap<>();
    }


    public List<Recipe> getContentBasedRecommendations(int recipeId) {
        // Implement content-based filtering here
        // Do content-based filtering by finding top 5 recipes with similar attributes for each recipe
        // Calculate similarity score between the recipes and return the top 5 recipes with the highest similarity score for every recipe
        // Use the RecipeRepository to fetch the recipes

        // Get all recipes
        List<Recipe> recipes = recipeRepository.findAll();
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null) {
            return new ArrayList<>();
        }

        // Store all similar recipes in a map
        List<Recipe> recs = new ArrayList<>();


            // Calculate the similarity score between the current recipe and all other recipes
            Map<Recipe, Float> similarityScores = new HashMap<>();
            for (Recipe otherRecipe : recipes) {
                if (!otherRecipe.equals(recipe)) {
                    float similarityScore = calculateSimilarity(recipe, otherRecipe);
                    similarityScores.put(otherRecipe, similarityScore);
                }
            }

            // Sort the other recipes by their similarity scores in descending order
            List<Recipe> sortedRecipes = similarityScores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Get the top 5 similar recipes
            List<Recipe> topRecipes = sortedRecipes.stream().limit(5).collect(Collectors.toList());
            recs.addAll(topRecipes);

        return recs;
    }

    private float calculateSimilarity(Recipe recipe1, Recipe recipe2) {
        // Implement your similarity metric here
        // This is a placeholder that calculates the similarity based on the number of shared ingredients

        // TODO see how to properly calculate similarity between recipes : based on keywords, ingredients, etc.
        List<String> ingredients1 = recipe1.getIngredientsMap().keySet().stream().toList();
        List<String> ingredients2 = recipe2.getIngredientsMap().keySet().stream().toList();

        long sharedIngredients = ingredients1.stream().filter(ingredients2::contains).count();

        return (float) sharedIngredients / (ingredients1.size() + ingredients2.size() - sharedIngredients);
    }

    public Map<Integer, Float> hybridRecommendation(int userId) {
        // Weighted hybrid approach
        // Combine content-based and collaborative filtering recommendations
        // Assign weights to each recommendation type
        // Return the final hybrid recommendations

        List<Recipe> contentBasedRec = getContentBasedRecommendations(userId);
        Map<Integer, Float> collaborativeRec = getCollaborativeRecommendations(userId);

        // Assign weights
        float contentWeight = 0.7f;
        float collaborativeWeight = 0.3f;

        // Weighted hybrid approach
        Map<Integer, Float> hybridRec = new HashMap<>();
//        for (Map.Entry<Integer, List<Recipe>> entry : contentBasedRec.entrySet()) {
//            float score = 1.0f; // Start score
//            for (Recipe recipe : entry.getValue()) {
//                float weightedScore = contentWeight * score;
//                hybridRec.merge(recipe.getRecipeId(), weightedScore, Float::sum);
//                score -= 0.1f; // Decrease score for next recipe
//            }
//        }

        for(Recipe recipe : contentBasedRec){
            float score = 1.0f;
            float weightedScore = contentWeight * score;
            hybridRec.merge(recipe.getRecipeId(), weightedScore, Float::sum);
            score -= 0.1f;
        }


        for (Map.Entry<Integer, Float> entry : collaborativeRec.entrySet()) {
            float weightedScore = collaborativeWeight * entry.getValue();
            hybridRec.merge(entry.getKey(), weightedScore, Float::sum);
        }

        // Switching hybrid approach
        int threshold = 10; // Set your threshold
        if (contentBasedRec.size() < threshold) {
            hybridRec = collaborativeRec;
        }

        return hybridRec;
    }
}