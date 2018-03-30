package com.roi.greenberg.michayavlemi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.roi.greenberg.michayavlemi.models.UserWithExpenses;

/**
 * Created by moti5321 on 13/03/2018.
 */

public class ExpensesAdapter extends FirebaseRecyclerAdapter<UserWithExpenses, ExpensesAdapter.ExpensesHolder> {

//private ArrayList<Item> mProducts;
//final private ItemClickListener mOnClickListener;

//Interface ClickListener
//public interface ItemClickListener {
//    void onListItemClick(int clickItemIndex);
//}

    //Constructor
    ExpensesAdapter(FirebaseRecyclerOptions<UserWithExpenses> options) {
        super(options);

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


    @Override
    public ExpensesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.expenses, parent, false);
        return new ExpensesHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ExpensesHolder holder, int position, @NonNull UserWithExpenses userWithExpenses) {
        if (userWithExpenses.getDetails() != null)
            holder.userName.setText(userWithExpenses.getDetails().getUsername());
        else
            holder.userName.setText("--");

        holder.userExpenses.setText(String.valueOf(userWithExpenses.getExpenses()));
    }





}
