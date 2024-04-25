package com.restservice.restockAndShoppingOptimization;

import com.restservice.shoppingListAndInventory.inventory.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.*;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EfficientRoute {
    private List<Product> productsList;
    private List<Store> preferedStores;
    private List<Store> stores;
    public EfficientRoute (List<Product> productsList, List<Store> preferedStores, List<Store> stores) {
        this.productsList = productsList;
        this.preferedStores = preferedStores;
        this.stores = stores;
    }

    public List<Store> findCheapestStores(Product product) {
        List<Store> cheapestStores = new ArrayList<>(stores);
        Collections.sort(cheapestStores, Comparator.comparingDouble(store -> store.getPrice(product)));
        return cheapestStores;
    }
    public List<Store> bestWaypoints() {
        List<Store> waypoints = new ArrayList<>();
        for(Product product : productsList) {
    //folosim preferedStores si findCheapestStore in functie de ce produse avem pt a gasi waypoint-urile
        }
        return waypoints;
    }
}
