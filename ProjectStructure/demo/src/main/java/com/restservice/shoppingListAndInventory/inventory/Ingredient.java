package com.restservice.shoppingListAndInventory.inventory;


import com.restservice.recipeAndMealPlanning.recipe.Recipe;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class Ingredient implements Eatable{// we  might remove eatable

    String name;
    @Embedded
    Quantity quantity;

    public Ingredient(){
    }

    public Ingredient(String name, Quantity quantity){
        this.name=name;
        this.quantity=quantity;
    }

}
