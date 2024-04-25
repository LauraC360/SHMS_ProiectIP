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
    boolean isEatable;
    int averageConsumption = 0;
    public Product(String name, Quantity quantity, boolean isEatable){
        this.name=name;
        this.quantity=quantity;
        this.isEatable = isEatable;
    }
    public void addQuantity(float quantity){
        this.quantity.setValue(this.quantity.getValue() + quantity);
    }
    public void computeAverageConsumption(){ //apelata cand un item e scos din inventory
        if(averageConsumption == 0) {
            //default comsumption value
        }
        else {
            //in functie de cat timp a stat in inventar in trecut
        }
    }
}