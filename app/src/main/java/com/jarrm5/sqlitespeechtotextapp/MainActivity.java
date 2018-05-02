package com.jarrm5.sqlitespeechtotextapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<GroceryItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<GroceryItem>();
        items.add(new GroceryItem("Waffle Sticks", 4));
        items.add(new GroceryItem("Pizza Rolls", 1));
        items.add(new GroceryItem("Condoms (Magnum size)", 12));
        items.add(new GroceryItem("Digiorno Big Sausage Pizza", 6));

    }
}
