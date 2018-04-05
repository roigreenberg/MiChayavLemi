package com.roi.greenberg.michayavlemi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

//import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ItemAdapter extends SelectableFirebaseRecyclerAdapter<Item, ItemAdapter.ItemHolder> {

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    private Query query;

    //Constructor
    ItemAdapter(FirebaseRecyclerOptions<Item> options, AppCompatActivity activity) {
        super(options, activity);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ItemHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Item item) {
        super.onBindViewHolder(holder, position, item);
        holder.productName.setText(item.getName());
        if (item.getUser() != null && item.getBuyerName() != null && !item.getBuyerName().isEmpty())
            holder.productUser.setText(item.getBuyerName());
        else
            holder.productUser.setText("--");
        if (item.getPrice() != 0)
            holder.productPrice.setText(String.valueOf(item.getPrice()));
        else
            holder.productPrice.setText("--");
    }


    /**
     * ViewHolder
     */
    public class ItemHolder extends SelectableFirebaseRecyclerAdapter.SelectableHolder{
        TextView productName;
        TextView productUser;
        TextView productPrice;

        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            productName = itemView.findViewById(R.id.tv_item_name);
            productUser = itemView.findViewById(R.id.tv_user);
            productPrice = itemView.findViewById(R.id.tv_price);
        }

        @Override
        public void onClick(View view)
        {
            super.onClick(view);
            int clickPosition = getAdapterPosition();
        }

        @Override
        public boolean onLongClick(View view)
        {
            super.onLongClick(view);
            int clickPosition = getAdapterPosition();
            return true;
        }

    }

}
