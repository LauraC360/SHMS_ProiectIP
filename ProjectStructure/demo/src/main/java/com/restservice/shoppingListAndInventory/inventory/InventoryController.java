package com.restservice.shoppingListAndInventory.inventory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class InventoryController {

    InventoryList itemList=new InventoryList();
    public InventoryController(){
    }

    @RequestMapping("/addItem")
    public void addItem(@RequestParam(value = "name", defaultValue = "____") String name,
                        @RequestParam(value = "quantity", defaultValue = "1") String quantityString){
        float quantity;
        try {
            quantity = Float.parseFloat(quantityString);
        } catch (NumberFormatException e) {
            quantity = 1;
        }
        for(int i=0;i<itemList.getItemList().size();i++){
            if(Objects.equals(itemList.getItemList().get(i).getItem().getName(), name)){
                itemList.getItemList().get(i).getItem().addQuantity(quantity);
                System.out.println(itemList);
                return;
            }
        }
        //itemList.addItem(name,new Quantity(quantity, QuantityType.Amount));
        System.out.println(itemList);
    }
    @RequestMapping("/removeItem")
    public void removeItem(@RequestParam(value="id", defaultValue = "-1") String idString){
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            id = -1;
        }
        if(id>=0&&id<itemList.getItemList().size())
            itemList.removeItem(id);
        System.out.println(itemList);
    }
    @RequestMapping("/changeQuantity")
            public void changeQuantity(@RequestParam(value="id", defaultValue = "-1") String idString,
                                       @RequestParam(value="quantity", defaultValue = "0") String quantityString){
        float quantity;

        try {
            quantity = Float.parseFloat(quantityString);
        } catch (NumberFormatException e) {
            quantity = 1;
        }
        int  id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            id = -1;
        }
        float currentQuantity = itemList.getItemList().get(id).getItem().getQuantity().getValue();
        if(currentQuantity + quantity > 0)
            itemList.getItemList().get(id).getItem().getQuantity().setValue(currentQuantity + quantity);
        else itemList.getItemList().get(id).getItem().getQuantity().setValue(0);
        System.out.println(itemList);

    }
    Map<Product, Integer> averageConsumptionForAll;
    public void computeAverageConsumptionForAll(){
    }
    public void suggestItems(){
        //getter din lista de cumparaturi si setter daca utilizatorul confirma ca vrea un item adaugat
    }
}