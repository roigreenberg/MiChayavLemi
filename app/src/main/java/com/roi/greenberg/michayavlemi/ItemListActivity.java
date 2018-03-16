package com.roi.greenberg.michayavlemi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity{
    private ArrayList<Item> mItems;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mFirebaseDatabaseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("EXTRA_REF");
            if (value == null)
                return;

            mFirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference().child("events").child(value).child("items");
            //The key argument here must match that used in the other activity
        } else {
            return;
        }

//        mFirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference().child("events").child("items");




//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("events")
//                .child(value)
//                .child("items");

        initRecycleView(mFirebaseDatabaseItem);
    }

    /**
     * RecycleView initial
     */
    private void initRecycleView(Query query) {

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_of_items);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        mAdapter = new ItemAdapter(options);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
    }


}
