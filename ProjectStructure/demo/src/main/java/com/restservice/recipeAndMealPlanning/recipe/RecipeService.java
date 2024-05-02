package com.restservice.recipeAndMealPlanning.recipe;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.Csv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private Recipe recipe;

    @Autowired
    private RecipeRepository recipeRepository;

    protected void importDB() throws Exception{
        Reader in = new FileReader("recipesLaura.csv");
        String[] headers = {"RecipeId", "Name", "AuthorId", "AuthorName", "CookTime", "PrepTime", "TotalTime", "DatePublished", "Description", "ImageList", "RecipeCategory", "Keywords", "RecipeIngredientQuantity", "RecipeIngredientParts", "AggregatedRating", "ReviewCount", "Calories", "FatContent", "SaturatedFatContent", "CholesterolContent", "SodiumContent", "CarbohydrateContent", "FiberContent", "SugarContent", "ProteinContent", "RecipeServings", "RecipeYield", "RecipeInstructions"};
        Reader inIngredients = new FileReader("recipes_ingredients.csv");
        String[] headersIngredients = {"id", "ingredients_raw"};

        CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();

        Iterable<CSVRecord> records = format.parse(in);
        Integer count = 0;
        Integer success = 0;


        sortCsvEntries();
        CSVFormat formatIngredients = CSVFormat.DEFAULT.builder().setHeader(headersIngredients).setSkipHeaderRecord(true).build();
        Iterator<CSVRecord>  printableIngredientsIter = (formatIngredients.parse(inIngredients)).iterator();
        for(CSVRecord record : records){
            count++;
            //if(success >= 100) break;

            try {

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




                List<String> printableIngredients = null;
                try {
                    printableIngredients = getPrintableIngredients(Integer.parseInt(record.get("RecipeId")), printableIngredientsIter);
                } catch (Exception e) {
                    System.out.println("Error getting printable ingredients for recipe: " + record.get("RecipeId"));
                    //e.printStackTrace();
                    continue; // Skip to the next iteration if ingredients are missing
                }

                List<String> imageLinks = null;
                try{
                    imageLinks = getImageLinks(record.get("ImageList"));
                } catch (Exception e) {
                    System.out.println("Error getting image list for recipe: " + record.get("RecipeId"));
                    //e.printStackTrace();
                    continue;
                }



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
                        imageLinks == null ? new ArrayList<>() : new ArrayList<>(imageLinks),
                        record.get("RecipeCategory"),
                        new ArrayList<>(keywordsList),
                        new ArrayList<>(recipeIngredientQuantityList),
                        new ArrayList<>(recipeIngredientPartsList),
                        new ArrayList<>(printableIngredients),
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
                e.printStackTrace();

            };
        }
        System.out.println("Total recipes: " + count);
        System.out.println("Success: " + success);
        System.out.println("Fail: " + (count - success));


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


    /*protected void deleteUnwantedColumns() throws Exception {
        String csvFile = "recipes_ingredients.csv";
        String tempFile = "temp.csv";

        Path path = Paths.get(csvFile);
        try (
                Reader reader = Files.newBufferedReader(path);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ) {
            List<String> headers = new ArrayList<>(csvParser.getHeaderMap().keySet());
            // Remove the headers of the columns you want to delete
            headers.remove("name");
            headers.remove("description");
            headers.remove("ingredients");
            headers.remove("steps");
            headers.remove("servings");
            headers.remove("serving_size");
            headers.remove("steps");
            headers.remove("tags");

            try (
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(tempFile));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))
            ) {
                for (CSVRecord record : csvParser) {
                    Map<String, String> recordToMap = record.toMap();
                    // Remove the columns
                    recordToMap.remove("name");
                    recordToMap.remove("description");
                    recordToMap.remove("ingredients");
                    recordToMap.remove("steps");
                    recordToMap.remove("servings");
                    recordToMap.remove("serving_size");
                    recordToMap.remove("steps");
                    recordToMap.remove("tags");

                    // Create a new map with the correct order of headers
                    Map<String, String> newRecordToMap = new LinkedHashMap<>();
                    for (String header : headers) {
                        newRecordToMap.put(header, recordToMap.get(header));
                    }

                    csvPrinter.printRecord(newRecordToMap.values());
                }

                csvPrinter.flush();
            }

            // Delete the old file
            Files.deleteIfExists(path);
            // Rename the new file to the old one
            Files.move(Paths.get(tempFile), path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Error deleting unwanted columns");
        }
    }*/

    /*
    protected void addNewColumn() {
        // Add a new column to the CSV file
        String csvFile = "recipesOG.csv";

    }*/


    private List<String> getPrintableIngredients(Integer recipeId, Iterator<CSVRecord> printableIngredientsIter) throws Exception {
        List<String> printableIngredients = new ArrayList<>();
        while (printableIngredientsIter.hasNext()) {

            CSVRecord record = printableIngredientsIter.next();

            if (Integer.parseInt(record.get("id")) == recipeId)
                return parsePrintableIngredients(record.get("ingredients_raw"));

            else if(Integer.parseInt(record.get("id")) > recipeId)
                throw new Exception("RecipeId not found in the ingredients CSV file");


        }

        throw new Exception("RecipeId not found in the ingredients CSV file");
    }

    private void sortCsvEntries() throws Exception{
        Reader in = new FileReader("recipes_ingredients.csv");
        CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        List<CSVRecord> records = new ArrayList<>();
        csvParser.forEach(records::add);

        Collections.sort(records, Comparator.comparing(record -> Integer.parseInt(record.get("id"))));

        // Overwrite the original CSV file
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("recipes_ingredients.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(csvParser.getHeaderMap().keySet().toArray(new String[0])));

        for (CSVRecord record : records) {
            csvPrinter.printRecord(record);
        }

        csvPrinter.flush();
        csvPrinter.close();
    }

    private List<String> parsePrintableIngredients(String ingredients_raw) {
        ingredients_raw = ingredients_raw.substring(1, ingredients_raw.length() - 1);
        List<String> ingredientsString = Arrays.stream(ingredients_raw.split(",")).toList();
        ingredientsString.forEach( s -> s = s.replaceAll("\"", ""));
        ingredientsString = ingredientsString.stream().map(String::trim).collect(Collectors.toList());

        //System.out.println("Ingredients: " + ingredientsString);
        return ingredientsString;
    }

    private List<String> getImageLinks(String imageList) throws Exception{
        imageList = imageList.replaceAll("\\s*\\[\\s*", "").replaceAll("\\s*]\\s*", "");
        List<String> imageLinks = new ArrayList<>();

        while(imageList.contains("http")){
            int start = imageList.indexOf("http");
            int end = imageList.indexOf("http", start + 1);
            if(end == -1) end = imageList.length();
            while (imageList.charAt(end - 1) == ' ') end--;
            while(imageList.charAt(end - 1) == ',') end--;

            imageLinks.add(imageList.substring(start, end));
            imageList = imageList.substring(end);
        }

        if(imageLinks.isEmpty()) throw new Exception("No image links found for recipe");
        return imageLinks;
    }

    //DO NOT RUN YET! todo add missing images
    /*protected void removeBadRecipes() throws Exception {
        List<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredientsMap()  == null || recipe.getIngredientsMap().isEmpty() || recipe.getInstructionsList() == null || recipe.getInstructionsList().isEmpty() ){
                recipeRepository.delete(recipe);
            }
        }
    }*/
}

