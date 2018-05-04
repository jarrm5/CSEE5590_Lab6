package com.jarrm5.sqlitespeechtotextapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class GroceryItemAdapter extends ArrayAdapter<GroceryItem> {

    public interface ListViewListener{
        public void onDeleteGroceryItem(int position);
    }

    public GroceryItemAdapter(Context context, List<GroceryItem> groceryItems) {
        super(context, 0, groceryItems);
    }

    private ListViewListener mListViewListener;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grocery_list_item, parent, false);
        }

        GroceryItem current = getItem(position);

        TextView mItemQuantity = listItemView.findViewById(R.id.item_quantity);
        TextView mItemName = listItemView.findViewById(R.id.item_name);
        ImageButton mItemEdit = listItemView.findViewById(R.id.btnEdit);
        ImageButton mItemDelete = listItemView.findViewById(R.id.btnDelete);

        GradientDrawable mItemQuantityCircle = (GradientDrawable) mItemQuantity.getBackground();
        GradientDrawable mItemEditCircle = (GradientDrawable) mItemEdit.getBackground();
        GradientDrawable mItemDeleteCircle = (GradientDrawable) mItemDelete.getBackground();

        int backgroundColor = getBackgroundColor(position);

        mItemQuantityCircle.setColor(backgroundColor);
        mItemEditCircle.setColor(ContextCompat.getColor(getContext(),R.color.edit_background));
        mItemDeleteCircle.setColor(ContextCompat.getColor(getContext(),R.color.edit_background));

        mItemQuantity.setText(Integer.toString(current.getQuantity()));
        mItemName.setText(current.getName());

        mItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListViewListener.onDeleteGroceryItem(position);
            }
        });

        return listItemView;
    }

    public void setItemDeleteListener(ListViewListener listener){
        this.mListViewListener = listener;
    }

    private int getBackgroundColor(int position) {
        int colorResourceId;

        int colorCode = (position % 4);

        switch (colorCode) {
            case 0:
                colorResourceId = R.color.red;
                break;
            case 1:
                colorResourceId = R.color.blue;
                break;
            case 2:
                colorResourceId = R.color.yellow;
                break;
            case 3:
                colorResourceId = R.color.green;
                break;
            default:
                colorResourceId = R.color.red;
                break;
        }
        return ContextCompat.getColor(getContext(),colorResourceId);
    }
}
