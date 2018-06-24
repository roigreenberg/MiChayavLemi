package com.roi.greenberg.michayavlemi.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roi.greenberg.advancefirestorerecycleradapter.AdvanceFirestoreRecyclerAdapter;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.Item;

import static com.roi.greenberg.michayavlemi.utils.Constants.USERS;
import static com.roi.greenberg.michayavlemi.utils.Constants.USER_NAME;

//import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ItemAdapter extends AdvanceFirestoreRecyclerAdapter<Item, ItemAdapter.ItemHolder> {

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    private Query query;

    //Constructor
    public ItemAdapter(FirestoreRecyclerOptions<Item> options, AppCompatActivity activity) {
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
    protected void onBindViewHolder(@NonNull final ItemHolder holder, int position, @NonNull final Item item) {
        super.onBindViewHolder(holder, position, item);
        holder.productName.setText(item.getName());
        holder.productUser.setText("--");
        if (item.getAssignTo() != null) {
            FirebaseFirestore.getInstance().collection(USERS).document(item.getAssignTo()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String name = (String) documentSnapshot.get(USER_NAME);
                    holder.productUser.setText(name);
                }
            });
        }

        if (item.getPrice() != 0)
            holder.productPrice.setText(String.valueOf(item.getPrice()));
        else
            holder.productPrice.setText("--");
    }


    /**
     * ViewHolder
     */
    public class ItemHolder extends AdvanceFirestoreRecyclerAdapter.SelectableHolder{
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
