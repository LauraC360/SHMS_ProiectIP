package com.restservice.shoppingListAndInventory.inventory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Product {
    String name;
    LocalDate expiryDate;
    Quantity quantity;
    int averageConsumption;
    public Product(String name, Quantity quantity){
        this.name=name;
        this.quantity=quantity;
    }
    public void addQuantity(float quantity){
        this.quantity.setValue(this.quantity.getValue() + quantity);
    }
    public void computeAverageConsumption(){
    }


}