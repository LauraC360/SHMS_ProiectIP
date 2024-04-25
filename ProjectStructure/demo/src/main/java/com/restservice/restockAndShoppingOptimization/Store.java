package com.restservice.restockAndShoppingOptimization;

import com.restservice.shoppingListAndInventory.inventory.Product;

import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Store {
    private Location location;
    private String name;
    private Map<Product, Double> priceProduct;
    public Store(Location location, String name, Map<Product, Double> products) {
        this.location = location;
        this.name = name;
        this.priceProduct = products;
    }
    public double getPrice(Product product) {
        return priceProduct.get(product);
    }
    public Location getLocation() {
        return location;
    }
}
