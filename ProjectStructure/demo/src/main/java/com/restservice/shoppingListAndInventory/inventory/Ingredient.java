package com.restservice.shoppingListAndInventory.inventory;


import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)

public class Ingredient extends Product implements Eatable{

    public Ingredient(String name, Quantity quantity){
        super(name,quantity);
    }

}
