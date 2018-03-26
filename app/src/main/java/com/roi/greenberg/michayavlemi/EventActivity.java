package com.roi.greenberg.michayavlemi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roi.greenberg.michayavlemi.fragments.AddNewEventFragment;
import com.roi.greenberg.michayavlemi.fragments.AddNewItemFragment;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity{
    private ArrayList<Item> mItems;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mFirebaseDatabaseItem;
    public String mEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        FloatingActionButton fab = findViewById(R.id.fab_new_item);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventId = extras.getString("EXTRA_REF");
            if (mEventId == null)
                return;

            mFirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference().child("events").child(mEventId).child("items");
            //The key argument here must match that used in the other activity

        } else {
            return;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewItemFragment();
                DateFragment.show(fragmentManager, "addNewItem");      }
        });

        initRecycleView(mFirebaseDatabaseItem);
    }

    /**
     * RecycleView initial
     */
    private void initRecycleView(Query query) {
        Log.d("EVENT", "start recyclerview: " + query.toString());
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_event);
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        mAdapter = new ItemAdapter(options);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
    }


}
