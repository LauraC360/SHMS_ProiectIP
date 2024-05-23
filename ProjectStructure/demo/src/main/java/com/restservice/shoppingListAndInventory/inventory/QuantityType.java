package com.restservice.shoppingListAndInventory.inventory;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;

import java.util.Arrays;

public enum QuantityType {
    Kilograms, Grams, Pieces, Liters, Milliliters, Cups, Tablespoons, Teaspoons, Pinch, Dash, Gallons, Ounces, Pounds,
    Pints, Lbs, Cloves, Slices, Leaves, Amount, ToTaste;

    public static QuantityType fromString(String text) {
        for (QuantityType b : QuantityType.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }


    public static QuantityType fromPhrase(String text) {
        QuantityType result = Pieces;
        for (String word : text.split(" ")) {
            for (QuantityType b : QuantityType.values()) {
                if (QuantityType.fromString(word) != null) {
                    result = b;
                    break;
                }
            }
        }

        return result;
    }


}
