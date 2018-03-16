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
        mFirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference().child("events").child("items");

        mItems = new ArrayList<>();
        mItems = Item.generateDummyProductList();
        mFirebaseDatabaseItem.push().setValue(mItems.get(0));



        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .child("eventid1")
                .child("items");

        initRecycleView(query);
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
        mRecyclerView.setAdapter(mAdapter);
    }


}
