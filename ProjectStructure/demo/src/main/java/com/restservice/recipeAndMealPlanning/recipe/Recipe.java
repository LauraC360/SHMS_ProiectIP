package com.restservice.recipeAndMealPlanning.recipe;

import com.restservice.shoppingListAndInventory.inventory.Ingredient;
import com.restservice.shoppingListAndInventory.inventory.Quantity;
import com.restservice.shoppingListAndInventory.inventory.QuantityType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @Column(name = "id")
    protected Integer recipeId;

    @Column(name = "Name")
    protected String recipeTitle;

    @Column(name = "AuthorId")
    protected Integer authorId;

    @Column(name = "AuthorName")
    protected String authorName;

    @Column(name = "CookTime")
    protected Duration cookTime;

    @Column(name = "PrepTime")
    protected Duration prepTime;

    @Column(name = "TotalTime")
    protected Duration totalTime;

    @Column(name = "DatePublished")
    protected String datePublished;

    @Lob
    @Column(name = "Description")
    protected String description;

    @ElementCollection
    @Column(name = "ImageList")
    protected List<String> imageList;

    @Column(name = "RecipeCategory")
    protected String category;

    @ElementCollection
    @Column(name = "Keywords")
    protected List<String> keywords;


    //this will be the better quantities, brought from another db
    @Transient
    protected Map<String, Float> ingredientsMap = new HashMap<String, Float>() ;

    @ElementCollection
    @Column(name = "PrintableIngredients")//text to b printed in the recipe
    protected List<String> printableIngredients;

    @Column(name = "ReviewCount")
    protected Float reviewCount;

    @Column(name = "Calories")
    protected Float calories;

    @Column(name = "FatContent")
    protected Float fatContent;

    @Column(name = "SaturatedFatContent")
    protected Float saturatedFatContent;

    @Column(name = "CholesterolContent")
    protected Float cholesterolContent;

    @Column(name = "SodiumContent")
    protected Float sodiumContent;

    @Column(name = "CarbohydrateContent")
    protected Float carbohydrateContent;

    @Column(name = "FiberContent")
    protected Float fiberContent;

    @Column(name = "SugarContent")
    protected Float sugarContent;

    @Column(name = "ProteinContent")
    protected Float proteinContent;

    @Column(name = "Servings")
    protected Float recipeServings;

    @Column(name = "Yield")
    protected String recipeYield;

    @ElementCollection
    @Column(name = "Instructions")
    protected Map<Integer, String> instructionsList;

    @ElementCollection
    List<Ingredient> ingredients = new ArrayList<>();
    public Recipe() {}

    public Recipe(Integer recipeId, String recipeTitle, Integer authorId, String authorName, Duration cookTime, Duration prepTime, Duration totalTime, String datePublished, String description, List<String> imageList,String recipeCategory, List<String> keywords, List<Float> recipeIngredientQuantityList, List<String> recipeIngredientPartsList, List<String> printableIngredients, Float reviewCount, Float calories, Float fatContent, Float saturatedFatContent, Float cholesterolContent, Float sodiumContent, Float carbohydrateContent, Float fiberContent, Float sugarContent, Float proteinContent, Float recipeServings, String recipeYield, Map<Integer, String> instructionsList){
        if(recipeIngredientQuantityList.size() != recipeIngredientPartsList.size())
        {recipeId = -1; return;}

        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.authorId = authorId;
        this.authorName = authorName;
        this.cookTime = cookTime;
        this.prepTime = prepTime;
        this.totalTime = totalTime;
        this.datePublished = datePublished;
        this.imageList = imageList;
        this.description = description;
        this.category = recipeCategory;
        this.keywords = keywords;


        for(int i = 0; i < recipeIngredientQuantityList.size(); i++){
            if(recipeIngredientQuantityList.get(i) == -1.0f)
                continue;
            ingredientsMap.put(recipeIngredientPartsList.get(i), recipeIngredientQuantityList.get(i));
        }

        this.printableIngredients = printableIngredients;
        this.reviewCount = reviewCount;
        this.calories = calories;
        this.fatContent = fatContent;
        this.saturatedFatContent = saturatedFatContent;
        this.cholesterolContent = cholesterolContent;
        this.sodiumContent = sodiumContent;
        this.carbohydrateContent = carbohydrateContent;
        this.fiberContent = fiberContent;
        this.sugarContent = sugarContent;
        this.proteinContent = proteinContent;
        this.recipeServings = recipeServings;
        this.recipeYield = recipeYield;
        this.instructionsList = instructionsList;



        //Iterator<Map.Entry<String, Float>> it = ingredientsMap.entrySet().iterator();
        for(int i = 0; i < recipeIngredientQuantityList.size(); i++){
            //Map.Entry<String, Float> entry = it.next();
            ingredients.add(new Ingredient(
                    recipeIngredientPartsList.get(i),
                    new Quantity(recipeIngredientQuantityList.get(i),
                            QuantityType.fromPhrase(
                                    printableIngredients.get(i)
                            ))));
        }
    }

    protected static boolean isValidFieldName(String fieldName){
        try{
            Recipe.class.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
