package com.restservice.shoppingListAndInventory.inventory;

import java.time.LocalDate;

public class Product {
    String name;
    LocalDate expiryDate;
    boolean eatable;
    float quantity;
    int averageConsumption;
    public void computeAverageConsumption(){
    }
}