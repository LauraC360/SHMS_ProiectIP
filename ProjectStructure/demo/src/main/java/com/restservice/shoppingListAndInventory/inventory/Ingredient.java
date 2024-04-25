package com.restservice.shoppingListAndInventory.inventory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Ingredient {
    private Product product;

    private Ingredient(){}//ingredients can only be created from a product that is eatable
}