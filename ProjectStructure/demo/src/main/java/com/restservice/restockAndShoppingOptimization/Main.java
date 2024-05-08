package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")

public class Main{
    private final ProductsRepository productsRepository;
    private final PriceProductRepository priceProductRepository;
    private final StoreRepository storeRepository;
    private final LocationRepository locationRepository;
    private Double latitude= 47.153566;
    private Double longitude= 27.577771;

    public Main(ProductsRepository productsRepository, PriceProductRepository priceProductRepository, LocationRepository l, StoreRepository storeRepository, LocationRepository locationRepository){
        this.productsRepository = productsRepository;
        this.priceProductRepository = priceProductRepository;
        this.storeRepository = storeRepository;
        this.locationRepository = locationRepository;
    }

    @PostMapping("/endpoint")
    public ResponseEntity<Map<String, Map<String, Double[]>>> handlePostRequest(@RequestBody Map<String, String> data) {
        String messageFromClient = data.get("selectedList");
        System.out.println("Shopping list selected: " + messageFromClient);
        Map<String, Double[]> result = new HashMap<>();
        Map<String, Map<String,Double[]>> response = new HashMap<>();
        if (messageFromClient.equals("Shopping List 1")) {
            result = efficienteRoute();
            response.put("message", result);
        }
        return ResponseEntity.ok(response);
    }
    public Map<String,Double[]> efficienteRoute() {
        String[] products = new String[]{"bath sponge", "broom", "shampoo", "tampons", "Applesauce", "Vegemite"};
        Vector<Integer> idList = new Vector<>();
        for (String product : products) {
            Products foundProduct = productsRepository.findByName(product);
            if (foundProduct != null) {
                Integer id = foundProduct.getId();
                idList.add(id);
            }
        }
        Vector<PriceProductId> priceProduct = new Vector<>();
        for(Integer id : idList) {
            for(int i=1;i<=7;++i) {
                PriceProductId priceProductId = new PriceProductId();
                priceProductId.setIdStore(i);
                priceProductId.setId(id);
                priceProduct.add(priceProductId);
            }
        }
        DistanceCalculator distance = new DistanceCalculator();
        Vector<Integer> idStores= new Vector<>();
        int idPreviousProduct=priceProduct.get(0).getId();
        Double priceMin=Double.MAX_VALUE;
        int idStore=priceProductRepository.findById(priceProduct.get(0)).getId().getIdStore();
        for(PriceProductId priceProductId : priceProduct) {
            PriceProduct foundPriceProduct = priceProductRepository.findById(priceProductId);
            if(foundPriceProduct!=null && foundPriceProduct.getPrice() != null ) {
                PriceProductId ID=foundPriceProduct.getId();
                Integer idCurrentProduct=ID.getId();
                if(idCurrentProduct==idPreviousProduct) {
                        if(foundPriceProduct.getPrice()<priceMin ) {
                            priceMin=foundPriceProduct.getPrice();
                            idStore=foundPriceProduct.getId().getIdStore();
                        }
                }
                else {
                    idStores.add(idStore);
                    idPreviousProduct=idCurrentProduct;
                    priceMin=foundPriceProduct.getPrice();
                }
            }
        }
        idStores.add(idStore);
        Map<String, Double[]> locations = new HashMap<>();
        for (Integer id : idStores) {
            Store foundStore = storeRepository.getById(id);
            if(foundStore!=null)
            {
                Location foundLocation = locationRepository.getById(foundStore.getId());
                if(foundLocation!=null) {
                    Double[] storeLocation = new Double[2];
                    storeLocation[0] = foundLocation.getLatitude();
                    storeLocation[1] = foundLocation.getLongitude();
                    locations.put(foundStore.getName(), storeLocation);
                }
            }
        }
        return locations;
    }
}


