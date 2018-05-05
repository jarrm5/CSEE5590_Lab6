package com.jarrm5.sqlitespeechtotextapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements GroceryItemAdapter.ListViewListener, GroceryItemEditDialogFragment.EditItemDialogListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String TEXT_TO_SPEECH_PROMPT = "Please say an item to add to the list";

    private TextToSpeech mTextToSpeech;
    private ImageButton mSpeak;
    private ImageButton mListen;
    private ArrayList<GroceryItem> items;
    private GroceryItemAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        items.add(new GroceryItem("Waffle Sticks", 4));
        items.add(new GroceryItem("Pizza Rolls", 1));
        items.add(new GroceryItem("32 oz Ground Beef", 1));
        items.add(new GroceryItem("Digiorno Big Sausage Pizza", 6));
        items.add(new GroceryItem("Skittlez", 20));
        items.add(new GroceryItem("Doritoz", 20));
        items.add(new GroceryItem("Freeze Pops", 13));

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
                if(status != TextToSpeech.ERROR) {
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
    public void onDeleteGroceryItem(int position){
        String deletedItem = items.get(position).getName();
        items.remove(position);
        mTextToSpeech.speak(deletedItem + " removed from list",TextToSpeech.QUEUE_FLUSH,null,null);
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
        mTextToSpeech.speak("New quantity for " + editedItem.getName() + " is " + Integer.toString(editedItem.getQuantity()),TextToSpeech.QUEUE_FLUSH, null,null);
        mAdapter.notifyDataSetChanged();
    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, TEXT_TO_SPEECH_PROMPT);
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
                    GroceryItem newGroceryItem = new GroceryItem(result.get(0).substring(0,1).toUpperCase() + result.get(0).substring(1),1);
                    items.add(newGroceryItem);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
    private void readGroceryList(){
        for (GroceryItem groceryItem : items){
            String toRead = groceryItem.getQuantity() + " " + groceryItem.getName();
            mTextToSpeech.speak(toRead,TextToSpeech.QUEUE_ADD, null,null);
        }
    }
}
