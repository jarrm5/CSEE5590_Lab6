package com.jarrm5.sqlitespeechtotextapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<GroceryItem> items;
    private GroceryItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        items.add(new GroceryItem("Waffle Sticks", 4));
        items.add(new GroceryItem("Pizza Rolls", 1));
        items.add(new GroceryItem("Bushmaster semi-auto", 1));
        items.add(new GroceryItem("Digiorno Big Sausage Pizza", 6));
        items.add(new GroceryItem("Skittlez", 20));
        items.add(new GroceryItem("Doritoz", 20));
        items.add(new GroceryItem("Freeze Pop", 13));

        ListView mGroceryItemsListView = findViewById(R.id.grocery_list);
        mAdapter = new GroceryItemAdapter(this, items);
        mGroceryItemsListView.setAdapter(mAdapter);

    }
}
