package com.restservice.shoppingListAndInventory.inventory;


public class Ingredient extends Product implements Eatable{
    public Ingredient(String name, Quantity quantity){
        super(name,quantity);
    }
}
