package com.jarrm5.sqlitespeechtotextapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class GroceryItemEditDialogFragment extends DialogFragment {

    private final String DIALOG_HEADING = "Set Quantity for item\n";
    private final static int MAX_QUANTITY = 99;
    private final static int MIN_QUANTITY = 1;

    private TextView mItemName;
    private NumberPicker mItemQuantity;
    private EditItemDialogListener mEditItemDialogListener;
    private GroceryItem mGroceryItem;
    private int mGroceryItemPosition;

    public interface EditItemDialogListener {
        void onEditItemDialog(int position, int quantity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof EditItemDialogListener) {
            mEditItemDialogListener = (EditItemDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement EditItemDialogListener interface!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View groceryItemDialogView = inflater.inflate(R.layout.dialog_grocery_item_edit, null);

        mItemName = groceryItemDialogView.findViewById(R.id.edit_item_name);
        mItemName.setText(DIALOG_HEADING + mGroceryItem.getName());

        //Set the min/max values on the picker
        //Also display the picker's default selection as the GroceryItem's current quantity
        mItemQuantity = groceryItemDialogView.findViewById(R.id.edit_item_quantity);
        mItemQuantity.setMaxValue(MAX_QUANTITY);
        mItemQuantity.setMinValue(MIN_QUANTITY);
        mItemQuantity.setValue(mGroceryItem.getQuantity());

        builder.setView(groceryItemDialogView)
                .setPositiveButton(R.string.edit_item_dialog_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //When the Update button on the fragment is tapped,
                        //Send the quantity from the number picker back to the main activity
                        //Send the position in the items list
                        mEditItemDialogListener.onEditItemDialog(mGroceryItemPosition,mItemQuantity.getValue());
                    }
                })
                .setNegativeButton(R.string.edit_item_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GroceryItemEditDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    //A workaround for dialog fragment's empty constructor rule.
    //We need to grocery item selected but can't retrieve it from the adapter
    //using the GroceryItemDialogFragment constructor.
    public void SetGroceryItem(GroceryItem groceryItem){
        this.mGroceryItem = groceryItem;
    }
    //We also need to remember the item's position in the list.
    //When the quantity is edited and sent back to the main activity, we
    //have to know which item to update in the arraylist of grocery items.
    public void setmGroceryItemPosition(int groceryItemPosition) {
        mGroceryItemPosition = groceryItemPosition;
    }
}
