package com.restservice.shoppingListAndInventory.inventory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class Product {

    private Long id;

    String name;
    LocalDate expiryDate;
    Quantity quantity;
    int averageConsumption;


    public Product(String name, Quantity quantity){
        this.name=name;
        this.quantity=quantity;
    }

    public Product() {

    }

    public void addQuantity(float quantity){
        this.quantity.setValue(this.quantity.getValue() + quantity);
    }
    public void computeAverageConsumption(){
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}