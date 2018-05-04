package com.jarrm5.sqlitespeechtotextapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements GroceryItemAdapter.ListViewListener, GroceryItemEditDialogFragment.EditItemDialogListener {

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
        mAdapter.setItemDeleteListener(MainActivity.this);
        mGroceryItemsListView.setAdapter(mAdapter);

    }
    /*
        Fires when the delete button on a listview item is tapped
        Removes the grocery Item from the list and refreshes the view.
     */
    @Override
    public void onDeleteGroceryItem(int position){
        String deletedItem = items.get(position).getName();
        items.remove(position);
        Toast.makeText(this,deletedItem + " succesfully removed from list",Toast.LENGTH_LONG).show();
        mAdapter.notifyDataSetChanged();
    }

    /*
        Fires when the edit button on a listview item is tapped.
        Retrieves the value from the edit dialog fragment and updates it in the list of items
     */
    @Override
    public void onEditItemDialog(int position,int quantity){
        items.get(position).setQuantity(quantity);
        GroceryItem editedItem = items.get(position);
        Toast.makeText(this,"New quantity for " + editedItem.getName() + " is " + Integer.toString(editedItem.getQuantity()),Toast.LENGTH_LONG).show();
        mAdapter.notifyDataSetChanged();
    }


}
