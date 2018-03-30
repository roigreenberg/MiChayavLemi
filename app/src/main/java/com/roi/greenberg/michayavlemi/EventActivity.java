package com.roi.greenberg.michayavlemi;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roi.greenberg.michayavlemi.fragments.AddNewItemFragment;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.models.UserWithExpenses;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity{
    private ArrayList<Item> mItems;
    private DatabaseReference mFirebaseDatabase;
    public String mEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        final FloatingActionButton fab = findViewById(R.id.fab_new_item);
        final TextView eventTotal = findViewById(R.id.rv_event_bottom_sheet_total);
        final TextView eventAvg = findViewById(R.id.rv_event_bottom_sheet_average);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventId = extras.getString("EXTRA_REF");
            if (mEventId == null)
                return;

        } else {
            return;
        }


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewItemFragment();
                DateFragment.show(fragmentManager, "addNewItem");      }
        });

        View llBottomSheet = findViewById(R.id.content_event_bottom_sheet);

        // init the bottom sheet behavior
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        mFirebaseDatabase.child("events").child(mEventId).child("totalexpenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long total = dataSnapshot.getValue(Long.class);
                eventTotal.setText(String.valueOf(total));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabase.child("events").child(mEventId).child("average").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long avg = dataSnapshot.getValue(Long.class);
                eventAvg.setText(String.valueOf(avg));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query itemsQuery = mFirebaseDatabase.child("events").child(mEventId).child("items");
        FirebaseRecyclerOptions<Item> ItemOptions = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemsQuery, Item.class)
                .build();
        ItemAdapter itemAdapter = new ItemAdapter(ItemOptions);
        initRecycleView(R.id.rv_event, itemAdapter);

        Query expensesQuery = mFirebaseDatabase.child("events").child(mEventId).child("users");
        FirebaseRecyclerOptions<UserWithExpenses> ExpensesOptions = new FirebaseRecyclerOptions.Builder<UserWithExpenses>()
                .setQuery(expensesQuery, UserWithExpenses.class)
                .build();
        ExpensesAdapter expensesAdapter = new ExpensesAdapter(ExpensesOptions);
        initRecycleView(R.id.rv_event_bottom_sheet, expensesAdapter);

    }

    /**
     * RecycleView initial
     */
    private <A extends FirebaseRecyclerAdapter, C> void initRecycleView(@IdRes int id, A adapter) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(id);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }


}
