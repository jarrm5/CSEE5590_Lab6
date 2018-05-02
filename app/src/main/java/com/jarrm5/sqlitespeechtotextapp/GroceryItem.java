package com.jarrm5.sqlitespeechtotextapp;

public class GroceryItem {

    private String name;
    private int quantity;

    public GroceryItem(String name){
        this.name = name;
        this.quantity = 1;
    }

    public GroceryItem(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
