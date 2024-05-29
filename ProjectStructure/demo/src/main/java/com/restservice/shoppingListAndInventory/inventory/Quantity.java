package com.restservice.shoppingListAndInventory.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Embeddable
public class Quantity {
    float value;
    @Enumerated(EnumType.ORDINAL)//to change
    QuantityType type;

    public Quantity() {
        this.value = 0;
        this.type = QuantityType.Pieces;
    }
}
