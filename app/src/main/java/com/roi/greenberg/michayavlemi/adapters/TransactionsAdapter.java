package com.roi.greenberg.michayavlemi.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.Transaction;
import com.roi.greenberg.michayavlemi.models.User;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class TransactionsAdapter extends FirestoreRecyclerAdapter<Transaction, TransactionsAdapter.TransactionHolder> {


    //Constructor
    public TransactionsAdapter(FirestoreRecyclerOptions<Transaction> options) {
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


    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction, parent, false);
        return new TransactionHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final TransactionHolder holder, int position, @NonNull final Transaction transaction) {
        FirebaseFirestore.getInstance().document(USERS + "/" + transaction.getFrom()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    holder.from.setText((String) task.getResult().getData().get(USER_NAME));
                } catch (NullPointerException e) {
                    Log.e("TransactionHolder", e.getMessage());
                    holder.from.setText(R.string.Unknown_user);
                }

            }
        });

        FirebaseFirestore.getInstance().document(USERS + "/" + transaction.getTo()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    holder.to.setText((String) task.getResult().getData().get(USER_NAME));
                } catch (NullPointerException e) {
                    Log.e("TransactionHolder", e.getMessage());
                    holder.to.setText(R.string.Unknown_user);
                }
            }
        });

        holder.amount.setText(String.valueOf(transaction.getAmount()));
    }





}
