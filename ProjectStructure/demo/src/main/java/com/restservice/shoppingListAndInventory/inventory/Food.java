package com.restservice.shoppingListAndInventory.inventory;

public class Food extends Product implements Eatable{
    public Food(String name, Quantity quantity){
        super(name,quantity);
    }
}
