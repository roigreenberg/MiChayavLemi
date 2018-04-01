package com.roi.greenberg.michayavlemi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.models.Transaction;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class TransactionsAdapter extends FirebaseRecyclerAdapter<Transaction, TransactionsAdapter.TransactionHolder> {

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    //Constructor
    TransactionsAdapter(FirebaseRecyclerOptions<Transaction> options) {
        super(options);

    }

/**
 * ViewHolder
 */
public class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView from;
    TextView to;
    TextView amount;

    public TransactionHolder(View itemView) {
        super(itemView);
        from = itemView.findViewById(R.id.tv_from_name);
        to = itemView.findViewById(R.id.tv_to_name);
        amount = itemView.findViewById(R.id.tv_trans_amount);
    }

    @Override
    public void onClick(View view) {
        int clickPosition = getAdapterPosition();
    }

}


    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction, parent, false);
        return new TransactionHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull TransactionHolder holder, int position, @NonNull Transaction transaction) {
        holder.from.setText(transaction.getFromName());
        holder.to.setText(transaction.getToName());
        holder.amount.setText(String.valueOf(transaction.getAmount()));
    }





}
