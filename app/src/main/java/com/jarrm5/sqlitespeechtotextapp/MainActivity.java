package com.jarrm5.sqlitespeechtotextapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements GroceryItemAdapter.ListViewListener, GroceryItemEditDialogFragment.EditItemDialogListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String SPEECH_TO_TEXT_PROMPT = "Please say an item to add to the list";

    private TextToSpeech mTextToSpeech;
    private ImageButton mSpeak;
    private ImageButton mListen;
    private ArrayList<GroceryItem> items;
    private GroceryItemAdapter mAdapter;
    private DatabaseReference theDB;
    private DatabaseReference groceryDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gets the FB DB
        theDB = FirebaseDatabase.getInstance().getReference();
        groceryDatabase = theDB.child("groceries");

        items = new ArrayList<>();

        setInitialData();

        groceryDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items.clear();

                for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                    String key = grocerySnapshot.getKey();
                    Number tempvalue = (Number) grocerySnapshot.getValue();
                    Integer value = tempvalue.intValue();
                    GroceryItem groceryItem = new GroceryItem(key, value);

                    items.add(groceryItem);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                new Log(databaseError.getMessage());
            }
        });


        mSpeak = findViewById(R.id.btnSpeak);
        mSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        mListen = findViewById(R.id.btnListen);
        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readGroceryList();
            }
        });

        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.US);
                }
            }
        });

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
    public void onDeleteGroceryItem(int position) {
        final String deletedItem = items.get(position).getName();
        items.remove(position);

        groceryDatabase.child(deletedItem).removeValue();

        mTextToSpeech.speak(deletedItem + " removed from list", TextToSpeech.QUEUE_FLUSH, null, null);
        mAdapter.notifyDataSetChanged();
    }

    /*
        Fires when the edit button on a listview item is tapped.
        Retrieves the value from the edit dialog fragment and updates it in the list of items
     */
    @Override
    public void onEditItemDialog(int position, int quantity) {
        items.get(position).setQuantity(quantity);
        GroceryItem editedItem = items.get(position);

        String key = editedItem.getName();
        groceryDatabase.child(key).setValue(quantity);

        mTextToSpeech.speak("New quantity for " + editedItem.getName() + " is " + Integer.toString(editedItem.getQuantity()), TextToSpeech.QUEUE_FLUSH, null, null);
        mAdapter.notifyDataSetChanged();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, SPEECH_TO_TEXT_PROMPT);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    GroceryItem newGroceryItem = new GroceryItem(result.get(0).substring(0, 1).toUpperCase() + result.get(0).substring(1), 1);

                    String key = newGroceryItem.getName();
                    Integer value = newGroceryItem.getQuantity();

                    groceryDatabase.child(key).setValue(value);

                    //items.add(newGroceryItem);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    private void readGroceryList() {
        for (GroceryItem groceryItem : items) {
            String toRead = groceryItem.getQuantity() + " " + groceryItem.getName();
            mTextToSpeech.speak(toRead, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    private void setInitialData() {

        //clears out the database
        groceryDatabase.setValue(null);

        List<GroceryItem> temp_items = new ArrayList<>();

        //initial data set
        temp_items.add(new GroceryItem("Eggs", 13));
        temp_items.add(new GroceryItem("Milk", 4));
        temp_items.add(new GroceryItem("Bananas", 1));
        temp_items.add(new GroceryItem("Chicken", 1));
        temp_items.add(new GroceryItem("Deodorant", 6));
        temp_items.add(new GroceryItem("Apples", 20));
        temp_items.add(new GroceryItem("Bacon", 20));

        for (GroceryItem gi : temp_items) {
            String key = gi.getName();
            Integer value = gi.getQuantity();
            groceryDatabase.child(key).setValue(value);
        }
    }
}
