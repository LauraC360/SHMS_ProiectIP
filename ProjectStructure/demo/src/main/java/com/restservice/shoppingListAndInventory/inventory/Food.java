package com.restservice.shoppingListAndInventory.inventory;

public class Food extends Product implements Eatable{
    public Food(String name, Quantity quantity, boolean isEatable){
        super(name,quantity,isEatable);
    }
}
