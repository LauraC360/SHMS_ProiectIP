package com.restservice.recipeAndMealPlanning.recipe;

import java.sql.*;
import java.util.*;

public class RecommendationSystem {
    private static final double VIEWED_RECIPES_WEIGHT = 0.2;
    private static final double LIKED_RECIPES_WEIGHT = 0.5;
    private static final double HISTORY_WEIGHT = 0.3;

    public static void main(String[] args) {
        // Load data from MySQL database
        Map<Integer, List<Integer>> userInteractions = new HashMap<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://sql11.freesqldatabase.com:3306/sql11703727", "sql11703727", "QDWDAeWbvv")) {
            String query = "SELECT * FROM recipe_user_preferences";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    List<Integer> viewedRecipes = parseRecipeIds(resultSet.getString("ViewedRecipes"));
                    List<Integer> likedRecipes = parseRecipeIds(resultSet.getString("LikedRecipes"));
                    List<Integer> history = parseRecipeIds(resultSet.getString("History"));
                    List<Integer> combinedInteractions = combineInteractions(viewedRecipes, likedRecipes, history);
                    // Use combinedInteractions for further processing or recommendation logic
                    //System.out.println("User ID: " + userId);
                    //System.out.println("Viewed Recipes: " + viewedRecipes);
                    //System.out.println("Liked Recipes: " + likedRecipes);
                    //System.out.println("History: " + history);
                    //System.out.println("Combined Interactions: " + combinedInteractions);
                    //System.out.println();
                    // Make userInteractions map
                    userInteractions.put(userId, combinedInteractions);
                }
            }
            // Call the function
            Map<List<Integer>, Double> allSimilarities = calculateAllSimilarities(userInteractions);

            // Print the results
            for (Map.Entry<List<Integer>, Double> entry : allSimilarities.entrySet()) {
                List<Integer> userPair = entry.getKey();
                Double similarity = entry.getValue();
                //System.out.println("User " + userPair.get(0) + " and User " + userPair.get(1) + ": " + similarity);
            }
            System.out.println();

            // recommend to each user 5 recipes based on the calculations above
            for (Map.Entry<Integer, List<Integer>> entry : userInteractions.entrySet()) {
                int targetUserId = entry.getKey();
                Map<Integer, Double> similarityScores = calculateSimilarityScores(targetUserId, userInteractions);
                List<Integer> recommendations = recommendItems(targetUserId, userInteractions, similarityScores);
                System.out.println("Recommendations for User " + targetUserId + ": " + recommendations);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculate similarity scores
    private static Map<Integer, Double> calculateSimilarityScores(int targetUserId, Map<Integer, List<Integer>> userInteractions) {
        Map<Integer, Double> similarityScores = new HashMap<>();

        for (Map.Entry<Integer, List<Integer>> entry : userInteractions.entrySet()) {
            int userId = entry.getKey();
            List<Integer> interactions = entry.getValue();

            if (userId != targetUserId) {
                double similarity = calculateCosineSimilarity(userInteractions.get(targetUserId), interactions);
                similarityScores.put(userId, similarity);
            }
        }

        return similarityScores;
    }

    // Calculate cosine similarity
    private static double calculateCosineSimilarity(List<Integer> interactions1, List<Integer> interactions2) {
        Set<Integer> set1 = new HashSet<>(interactions1);
        Set<Integer> set2 = new HashSet<>(interactions2);

        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    // Recommend items to a user
    private static List<Integer> recommendItems(int targetUserId, Map<Integer, List<Integer>> userInteractions, Map<Integer, Double> similarityScores) {
        Map<Integer, Double> itemScores = new HashMap<>();
        List<Integer> targetUserInteractions = userInteractions.get(targetUserId);

        for (Map.Entry<Integer, List<Integer>> entry : userInteractions.entrySet()) {
            int userId = entry.getKey();
            List<Integer> interactions = entry.getValue();

            if (userId != targetUserId) {
                double similarityScore = similarityScores.get(userId);

                for (Integer itemId : interactions) {
                    if (!targetUserInteractions.contains(itemId)) { // Only consider items that the target user has not interacted with
                        double oldScore = itemScores.getOrDefault(itemId, 0.0);
                        double newScore = oldScore + similarityScore; // This is a simple sum, you might want to use a different calculation
                        itemScores.put(itemId, newScore);
                    }
                }
            }
        }

        List<Map.Entry<Integer, Double>> itemScoreList = new ArrayList<>(itemScores.entrySet());
        itemScoreList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        List<Integer> recommendations = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : itemScoreList) {
            if (recommendations.size() < 5) { // Only add the top 5 items
                recommendations.add(entry.getKey());
            } else {
                break;
            }
        }

        // If less than 5 recommendations, consider lower rated recipes from combinedInteractions
        if (recommendations.size() < 5) {
            List<Integer> lowerRatedRecipes = new ArrayList<>(targetUserInteractions);
            lowerRatedRecipes.removeAll(itemScores.keySet()); // Remove already recommended items
            Collections.shuffle(lowerRatedRecipes); // Shuffle to add randomness
            for (Integer recipe : lowerRatedRecipes) {
                if (recommendations.size() < 5) {
                    recommendations.add(recipe);
                } else {
                    break;
                }
            }
        }

        return recommendations;
    }

    // Parse recipe IDs from a string
    private static List<Integer> parseRecipeIds(String recipeIdsStr) {
        List<Integer> recipeIds = new ArrayList<>();
        if (recipeIdsStr != null && !recipeIdsStr.isEmpty() && !recipeIdsStr.equals("[]")) {
            // Remove the square brackets and then split the string
            String[] recipeIdsArray = recipeIdsStr.replace("[", "").replace("]", "").trim().split("\\s*,\\s*");
            for (String id : recipeIdsArray) {
                try {
                    recipeIds.add(Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in string: " + id);
                }
            }
        }
        return recipeIds;
    }

    // Combine interactions
    private static List<Integer> combineInteractions(List<Integer> viewedRecipes, List<Integer> likedRecipes, List<Integer> history) {
        List<Integer> combinedInteractions = new ArrayList<>();
        combinedInteractions.addAll(viewedRecipes);
        combinedInteractions.addAll(likedRecipes);
        combinedInteractions.addAll(history);

        Map<Integer, Double> weights = new HashMap<>();
        for (Integer recipeId : viewedRecipes) {
            weights.put(recipeId, weights.getOrDefault(recipeId, 0.0) + VIEWED_RECIPES_WEIGHT);
        }
        for (Integer recipeId : likedRecipes) {
            weights.put(recipeId, weights.getOrDefault(recipeId, 0.0) + LIKED_RECIPES_WEIGHT);
        }
        for (Integer recipeId : history) {
            weights.put(recipeId, weights.getOrDefault(recipeId, 0.0) + HISTORY_WEIGHT);
        }

        Set<Integer> uniqueRecipes = new HashSet<>(combinedInteractions);
        combinedInteractions.clear();
        combinedInteractions.addAll(uniqueRecipes);

        combinedInteractions.sort((recipeId1, recipeId2) -> Double.compare(weights.get(recipeId2), weights.get(recipeId1)));

        return combinedInteractions;
    }

    // Calculate the similarity between two users based on their interactions
    public static Map<List<Integer>, Double> calculateAllSimilarities(Map<Integer, List<Integer>> userInteractions) {
        Map<List<Integer>, Double> allSimilarities = new HashMap<>();

        List<Integer> userIds = new ArrayList<>(userInteractions.keySet());
        for (int i = 0; i < userIds.size(); i++) {
            for (int j = i + 1; j < userIds.size(); j++) {
                int userId1 = userIds.get(i);
                int userId2 = userIds.get(j);
                double similarity = calculateCosineSimilarity(userInteractions.get(userId1), userInteractions.get(userId2));
                allSimilarities.put(Arrays.asList(userId1, userId2), similarity);
            }
        }

        return allSimilarities;
    }
}
