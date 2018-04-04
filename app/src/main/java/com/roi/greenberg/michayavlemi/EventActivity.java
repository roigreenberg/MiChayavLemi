package com.roi.greenberg.michayavlemi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;
import static com.roi.greenberg.michayavlemi.utils.Utils.*;

public class EventActivity extends AppCompatActivity{
    private ArrayList<Item> mItems;
    private DatabaseReference mFirebaseDatabase, mEventRef;
    private ItemAdapter mItemAdapter;
    private ExpensesAdapter mExpensesAdapter;
    public String mEventId, mEventName;
    public String mUserId, mUserName;
    public String[] childRef = {"totalexpenses", "average"};
    public HashMap<String, ValueEventListener> childListeners = new HashMap<>();

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
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            mUserId = fUser.getUid();
            mUserName = fUser.getDisplayName();
        } else {
            mUserId = "";
            mUserName = "Anonymous";
        }

        mEventRef = mFirebaseDatabase.child(EVENTS).child(mEventId);

        mEventRef.child("details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EventDetails eventDetails = dataSnapshot.getValue(EventDetails.class);
                mEventName = eventDetails.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        childListeners.put("totalexpenses", addLongListener(eventTotal));

        childListeners.put("average", addLongListener(eventAvg));



        Query itemsQuery = mEventRef.child(ITEMS);
        FirebaseRecyclerOptions<Item> ItemOptions = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemsQuery, Item.class)
                .build();
        mItemAdapter = new ItemAdapter(ItemOptions);
        initRecycleView(this, (RecyclerView) findViewById(R.id.rv_event), mItemAdapter);

        Query expensesQuery = mEventRef.child(USERS);
        FirebaseRecyclerOptions<UserWithExpenses> ExpensesOptions = new FirebaseRecyclerOptions.Builder<UserWithExpenses>()
                .setQuery(expensesQuery, UserWithExpenses.class)
                .build();
        mExpensesAdapter = new ExpensesAdapter(ExpensesOptions);
        initRecycleView(this, (RecyclerView) findViewById(R.id.rv_event_bottom_sheet), mExpensesAdapter);


        final Button transactionsButton = findViewById(R.id.bt_require_transactions);
        transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent transactionsIntent = new Intent(v.getContext(),TransactionsActivity.class);
                transactionsIntent.putExtra("EXTRA_REF", mEventId);
                v.getContext().startActivity(transactionsIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (String ref: childRef) {
            mEventRef.child(ref).addValueEventListener(childListeners.get(ref));
        }

        mItemAdapter.startListening();
        mExpensesAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (String ref: childRef) {
            mEventRef.child(ref).removeEventListener(childListeners.get(ref));
        }

        mItemAdapter.stopListening();
        mExpensesAdapter.stopListening();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case R.id.share_list_menu:
                ShareList(this, mUserName, mEventName, mEventId);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
