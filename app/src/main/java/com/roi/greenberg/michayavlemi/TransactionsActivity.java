package com.roi.greenberg.michayavlemi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.models.Transaction;

import static com.roi.greenberg.michayavlemi.utils.Utils.initRecycleView;

public class TransactionsActivity extends AppCompatActivity {


    private DatabaseReference mFirebaseDatabase;
    public String mEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventId = extras.getString("EXTRA_REF");
            if (mEventId == null)
                return;

        } else {
            return;
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Query transactionQuery = mFirebaseDatabase.child("events").child(mEventId).child("require_transactions");
        FirebaseRecyclerOptions<Transaction> transactionOptions = new FirebaseRecyclerOptions.Builder<Transaction>()
                .setQuery(transactionQuery, Transaction.class)
                .build();
        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(transactionOptions);
        initRecycleView(this, (RecyclerView) findViewById(R.id.rv_transaction), transactionsAdapter);



    }


}
