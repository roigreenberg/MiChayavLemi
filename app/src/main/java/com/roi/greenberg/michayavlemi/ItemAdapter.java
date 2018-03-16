package com.roi.greenberg.michayavlemi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ItemAdapter extends SelectableAdapter<Item, ItemAdapter.ItemHolder>{

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    //Constructor
    ItemAdapter(FirebaseRecyclerOptions<Item> options) {
        super(options);

    }

/**
 * ViewHolder
 */
public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView productName;
    TextView productUser;
    TextView productPrice;

    public ItemHolder(View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.tv_product);
        productUser = itemView.findViewById(R.id.tv_user_buy);
        productPrice = itemView.findViewById(R.id.tv_pric);
    }

    @Override
    public void onClick(View view) {
        int clickPosition = getAdapterPosition();
    }

}


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ItemHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Item item) {
        holder.productName.setText(item.getProductName());
        holder.productUser.setText(item.getUser());
        holder.productPrice.setText(item.getProductPrice());
    }





}
