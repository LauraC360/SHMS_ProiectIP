package com.restservice.restockAndShoppingOptimization;

import com.restservice.shoppingListAndInventory.inventory.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RestController
@Configuration
public class RouteService {

    private final EfficientRoute efficientRoute;
    private final String apiKey;

    public RouteService(EfficientRoute efficientRoute, String apiKey) {
        this.efficientRoute = efficientRoute;
        this.apiKey = apiKey;
    }
    @RequestMapping("/EfficientRoute")
    public String generateRouteUrlWithWaypoints(Location origin, List<Product> products) {
        StringBuilder waypointsBuilder = new StringBuilder();
        List<Store> bestWaypoints = efficientRoute.bestWaypoints();

        for (Store waypoint : bestWaypoints) {
            waypointsBuilder.append(waypoint.getLocation().getLatitude())
                    .append(",")
                    .append(waypoint.getLocation().getLongitude())
                    .append("|");
        }

        waypointsBuilder.deleteCharAt(waypointsBuilder.length() - 1);
        String originCoordinates = origin.getLatitude() + "," + origin.getLongitude();

        String url = UriComponentsBuilder.fromHttpUrl("https://www.google.com/maps/dir/")
                .queryParam("api", "1")
                .queryParam("origin", originCoordinates) // Casa utilizatorului
                .queryParam("destination", originCoordinates) // Dus-intors
                .queryParam("waypoints", waypointsBuilder.toString())
                .queryParam("key", apiKey)
                .toUriString();

        return url;
    }

}
