package com.roi.greenberg.michayavlemi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.roi.greenberg.advancefirestorerecycleradapter.AdvanceFirestoreRecyclerAdapter;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.adapters.ExpensesAdapter;
import com.roi.greenberg.michayavlemi.adapters.ItemAdapter;
import com.roi.greenberg.michayavlemi.fragments.AddNewItemFragment;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.models.User;
import com.roi.greenberg.michayavlemi.models.UserInList;
//import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;
import static com.roi.greenberg.michayavlemi.utils.Utils.*;

public class EventActivity extends AppCompatActivity{
    private static final String TAG = "EventActivity";
    private ArrayList<Item> mItems;
//    private DatabaseReference mFirebaseDatabase, mEventRef;
    private DocumentReference mEventRef;
    private FirebaseFirestore mFirestoreDatabase;
    private ItemAdapter mItemAdapter;
    private ExpensesAdapter mExpensesAdapter;
    public String mEventId, mEventName;
    public String mUserId, mUserName;
    public String[] childRef = {"totalexpenses", "average"};
    public HashMap<String, ValueEventListener> childListeners = new HashMap<>();
    private FloatingActionButton fab;
    public double mTotal, mAverage;
    public long numOfUsers;
    private TextView tvEventTotal;
    private TextView tvEventAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        fab = findViewById(R.id.fab_new_item);
        tvEventTotal = findViewById(R.id.rv_event_bottom_sheet_total);
        tvEventAvg = findViewById(R.id.rv_event_bottom_sheet_average);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEventId = extras.getString("EXTRA_REF");
            if (mEventId == null)
                return;

        } else {
            return;
        }

        Log.d(TAG, "onCreate: " + mEventId);

//        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        if(mFirestoreDatabase == null) {
            mFirestoreDatabase = FirebaseFirestore.getInstance();
        }
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            mUserId = fUser.getUid();
            mUserName = fUser.getDisplayName();
        } else {
            mUserId = "";
            mUserName = "Anonymous";
        }

//        mEventRef = mFirebaseDatabase.child(EVENTS).child(mEventId);
        mEventRef = mFirestoreDatabase.collection(EVENTS).document(mEventId);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewItemFragment();
                DateFragment.show(fragmentManager, "addNewItem");
            }
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

//        childListeners.put("totalexpenses", addLongListener(tvEventTotal));
//
//        childListeners.put("average", addLongListener(tvEventAvg));





    }

    @Override
    protected void onStart() {
        super.onStart();


        mEventRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent");
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    EventDetails eventDetails = documentSnapshot.toObject(EventDetails.class);
                    if (eventDetails == null) {
                        return;
                    }
                    mEventName = eventDetails.getName();
                    numOfUsers = eventDetails.getNumOfUsers();
                    mTotal = eventDetails.getTotalexpenses();
                    mAverage = eventDetails.getAverage();
                    tvEventTotal.setText(String.valueOf(mTotal));
                    tvEventAvg.setText(String.valueOf(mAverage));

                }
            }
        });

        Query itemsQuery = mEventRef.collection(ITEMS)
                .orderBy("type", Query.Direction.DESCENDING)
                .orderBy(TIMESTAMP, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> ItemOptions = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(itemsQuery, Item.class)
                .setLifecycleOwner(this)
                .build();
        mItemAdapter = new ItemAdapter(ItemOptions, this);
        initRecycleView(this, (RecyclerView) findViewById(R.id.rv_event), mItemAdapter);

        Query expensesQuery = mEventRef.collection(USERS);
        FirestoreRecyclerOptions<UserInList> ExpensesOptions = new FirestoreRecyclerOptions.Builder<UserInList>()
                .setQuery(expensesQuery, UserInList.class)
                .setLifecycleOwner(this)
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

        EventActionModeCallback eventActionModeCallback = new EventActionModeCallback(this, mItemAdapter, R.menu.selected_menu);
        mItemAdapter.setActionMode(eventActionModeCallback);

    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//
////        for (User ref: childRef) {
////            mEventRef.child(ref).addValueEventListener(childListeners.get(ref));
////        }
//
//        mItemAdapter.startListening();
//        mExpensesAdapter.startListening();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
////        for (User ref: childRef) {
////            mEventRef.child(ref).removeEventListener(childListeners.get(ref));
////        }
//
//        mItemAdapter.stopListening();
//        mExpensesAdapter.stopListening();
//
//
//
//
//    }

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


    private class EventActionModeCallback extends AdvanceFirestoreRecyclerAdapter.SelectableActionModeCallback
    {
        private static final String TAG = "EventActionModeCallback";
        private AdvanceFirestoreRecyclerAdapter adapter;
        EventActionModeCallback(Context context, AdvanceFirestoreRecyclerAdapter adapter, int menu_layout) {
            super(context, adapter, menu_layout);
            this.adapter = adapter;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    // Get a new write batch
                    WriteBatch batch = mFirestoreDatabase.batch();
                    double price = 0;
                    for (int i = adapter.getItemCount() - 1; i >= 0; i--) {
                        if (adapter.isSelected(i)){
                            final QueryDocumentSnapshot doc = (QueryDocumentSnapshot) adapter.getSnapshots().getSnapshot(i);
                            String id = doc.getId();
                            batch.delete(mEventRef.collection(ITEMS).document(id));
                            price += doc.contains("price") ? doc.getDouble("price") : 0;
                        }
                    }
                    // Commit the batch
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Delete success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Delete failure.", e);
                        }
                    });
                    if (price > 0) {
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + mEventId).update(TOTAL_EXTENSES, mTotal - price).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + mEventId).update(AVERAGE, mAverage - (price / numOfUsers)).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                    }


                    break;
                case R.id.menu_users_picker:
    //                    showAssigningDialog(mode);
                    break;

                case R.id.menu_edit_item:
    //                    int pos;
    //                    final DatabaseReference itemRef;
    //
    //                    pos = (int) adapter.getSelectedItems().get(0);
    //                    itemRef = adapter.getRef(pos).child("itemID");
    //                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
    //                        @Override
    //                        public void onDataChange(DataSnapshot dataSnapshot) {
    //                            User itemID = (User) dataSnapshot.getValue();
    //                            Intent editItemIntent = new Intent(ListActivity.this, EditItemActivity.class);
    //                            editItemIntent.putExtra("EXTRA_REF", itemID);
    //                            startActivityForResult(editItemIntent, EDIT_REQUEST);
    //                        }
    //
    //                        @Override
    //                        public void onCancelled(DatabaseError databaseError) {
    //
    //                        }
    //                  });
                    break;

                default:
                    return false;
            }
            mode.finish();
            return true;
        }

//        private void showAssigningDialog(final ActionMode mode) {
//
//            final CharSequence[] charSequenceItems = users.toArray(new CharSequence[users.size()]);
//            Log.d("RROI", charSequenceItems[0].toString());
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Assign items to:");
//            builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.e("RROI", "on picker");
//                    for (int i = adapter.getItemCount() - 1; i >= 0; i--) {
//                        ItemAdapter.ItemHolder itemHolder = (ItemAdapter.ItemHolder) adapter.getItem(i);
//                        if (adapter.isSelected(i)){
//                            Log.e("RROI", "Before " + adapter.getRef(i).child("assignee").toString());
//                            adapter.getRef(i).child("assignee").setValue(charSequenceItems[which].toString());
//                            adapter.notifyItemChanged(i);
//                            Log.e("RROI", "After " + adapter.getRef(i).child("assignee").toString());
//                        }
//                    }
//
//                    Log.d(TAG, "menu_users_picker");
//                    //itemAdapter.notifyDataSetChanged();
//                    //doneItemAdapter.notifyDataSetChanged();
//                    mode.finish();
//                }
//            });
//            AlertDialog b = builder.create();
//            b.show();
//        }
    }

}
