package com.roi.greenberg.michayavlemi.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.User;
import com.roi.greenberg.michayavlemi.models.UserInList;

import static com.roi.greenberg.michayavlemi.utils.Constants.USERS;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ExpensesAdapter extends FirestoreRecyclerAdapter<UserInList, ExpensesAdapter.ExpensesHolder> {

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    //Constructor
    public ExpensesAdapter(FirestoreRecyclerOptions<UserInList> options) {
        super(options);

    }

    @Override @NonNull
    public ExpensesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.expenses, parent, false);
        return new ExpensesHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final ExpensesHolder holder, int position, @NonNull UserInList userInList) {
        String id = getSnapshots().getSnapshot(position).getId();

        FirebaseFirestore.getInstance().collection(USERS).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String username = user != null? user.getUsername() : "--";
                holder.userName.setText(username);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.userName.setText("--");
            }
        });

        holder.userExpenses.setText(String.valueOf(userInList.getExpenses()));
    }


    /**
     * ViewHolder
     */
    public class ExpensesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        TextView userExpenses;

        public ExpensesHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.tv_user_name);
            userExpenses = view.findViewById(R.id.tv_user_expenses);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
        }

    }


}
