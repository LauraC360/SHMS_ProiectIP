package com.restservice.shoppingListAndInventory.inventory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class InventoryList {
    List<InventoryItem> itemList;
    public InventoryList(){
        itemList=new ArrayList<>();
    }
    public InventoryItem getItemAt(int index){
        return itemList.get(index);
    }
    public void addItem(InventoryItem item){
        itemList.add(item);
    }
    public void addItem(String name, Quantity quantity){
        itemList.add(new InventoryItem(name,quantity));
    }
    public void removeItem(int index){
        itemList.remove(index);
    }
}
