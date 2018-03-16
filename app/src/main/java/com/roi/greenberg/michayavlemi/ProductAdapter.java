package com.roi.greenberg.michayavlemi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    final private ItemClickListener mOnClickListener;

    //Interface ClickListener
    public interface ItemClickListener {
        void onListItemClick(int clickItemIndex);
    }

    //Constructor
    public ProductAdapter(ArrayList<Product> products, ItemClickListener listener) {
        mOnClickListener = listener;
        mProducts = products;
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        TextView productUser;
        TextView productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_product);
            productUser = itemView.findViewById(R.id.tv_user_buy);
            productPrice = itemView.findViewById(R.id.tv_pric);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.iv_list_of_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        holder.productName.setText(product.getProductName());
        holder.productUser.setText(product.getUser());
        holder.productPrice.setText(product.getProductPrice());


    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }




}
