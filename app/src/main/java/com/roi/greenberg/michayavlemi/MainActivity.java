package com.roi.greenberg.michayavlemi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private List<EventDetails> mEventDetails;
    private DatabaseReference mDatabaseReference;
    private EventAdapter mEventAdapter;
    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static final String USERS = "Users";

    public static User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "before auth");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    onSignedInInitialize(user);
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build())
                                    )
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        Log.d(TAG, "after auth");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.valueOf(requestCode));
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedInInitialize(FirebaseUser user) {
//        showLoading();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        String userName = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        String userUid = user.getUid();
        Log.d(TAG, userName + " " + email + " " + photoUrl + " " + userUid);
        final User mUser = new User(userName, email, photoUrl, userUid);
        mDatabaseReference.child("users").child(userUid).child("details").setValue(mUser);
        FloatingActionButton fab = findViewById(R.id.fab);

        DatabaseReference eventRef = mDatabaseReference.child("users").child(mUser.getUid()).child("events");
        Query eventQuery = eventRef.orderByKey();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseRecyclerOptions<EventDetails> options = new FirebaseRecyclerOptions.Builder<EventDetails>()
                .setQuery(eventRef, EventDetails.class)
                .build();

        mEventAdapter = new EventAdapter(this, options);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(layoutManager);
        mEventAdapter.startListening();
        mRecyclerView.setAdapter(mEventAdapter);

        //Button btnToListOfProducts = findViewById(R.id.btn_item_list_of_products);
        //btnToListOfProducts.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent toListOfItemsIntent = new Intent(MainActivity.this,ItemListActivity.class);
        //        startActivity(toListOfItemsIntent);
        //    }
        //});

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_event, null);
                final EditText mName =  mView.findViewById(R.id.etEventName);
                final EditText mDate = mView.findViewById(R.id.etDate);
                final EditText mLocation = mView.findViewById(R.id.etLocation);
                Button mButtonOk = mView.findViewById(R.id.btnOk);
                Button mButtonCancel = mView.findViewById(R.id.btnCancel);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = mName.getText().toString();
                        Long date = Long.parseLong(mDate.getText().toString());
                        String location = mLocation.getText().toString();
                        if (!name.isEmpty()) {
                            Toast.makeText(MainActivity.this,"Add event: " + name, Toast.LENGTH_SHORT).show();
                            String key = mDatabaseReference.child("events").push().getKey();
                            EventDetails eventDetails = new EventDetails(name, date, location);
                            Map<String, Object> eventDetailsValues = eventDetails.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();

                            childUpdates.put("/events/" + key + "/details", eventDetailsValues);
                            childUpdates.put("/events/" + key + "/users/" + mUser.getUid(), mUser);
                            mDatabaseReference.updateChildren(childUpdates);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,"Please enter event name", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                mButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void onSignedOutCleanup() {
        mUser.setUsername(ANONYMOUS);
//        if (mOwnListAdapter != null)
//            mOwnListAdapter.cleanup();
        //detachDatabaseReadListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
//                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData data) {
//                        if (data == null) {
//                            Log.d(TAG, "getInvitation: no data");
//                            return;
//                        }
//
//                        // Get the deep link
//                        Uri deepLink = data.getLink();
//
//                        // Extract invite
//                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
//                        if (invite != null) {
//                            String invitationId = invite.getInvitationId();
//                        }
//
//                        // Handle the deep link
//                        // [START_EXCLUDE]
//                        Log.d(TAG, "deepLink:" + deepLink);
//                        if (deepLink != null) {
//
//                            String userID = deepLink.getQueryParameter("UserID"); //TODO is it needed?
//                            final String listID = deepLink.getQueryParameter("ListID"); //TODO change name
//                            if (userID == null || listID == null)
//                                return;
//                            //Toast.makeText(MainActivity.this, "UserID= " +userID + "ListID= " + listID, Toast.LENGTH_LONG).show();
//
//                            mDatabaseReference.child(LISTS).orderByChild("listID").equalTo(listID).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.getValue() == null) {
//                                        Toast.makeText(MainActivity.this, "List not exists!", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        //add new item name to SList
//                                        final DatabaseReference userRef = mDatabaseReference.child(USERS).child(mUserID).child(LISTS);
//                                        userRef.orderByChild("listID").equalTo(listID).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                if(dataSnapshot.getValue() != null) {
//                                                    Toast.makeText(MainActivity.this, "List already exists!", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    userRef.push().setValue(new ListForUser(listID));
//
//                                                    DatabaseReference ref = mDatabaseReference.child(LISTS).child(listID);
//                                                    ref.child(USERS).child(mUserID).setValue("user");
//
//                                                    //update ListView adapter
//                                                    mOwnListAdapter.notifyDataSetChanged();
//                                                    Toast.makeText(MainActivity.this, "Adding sucessful!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//                });

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //mOwnListAdapter.cleanup();
        //detachDatabaseReadListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
//            startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//
//    /**
//     * Callback invoked when a menu item was selected from this Activity's menu.
//     *
//     * @param item The menu item that was selected by the user
//     *
//     * @return true if you handle the menu click here, false otherwise
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//
//        switch (item.getItemId()) {
//            case R.id.add_new_list_menu_menu:
//                showAddingDialog();
//                return true;
//            case R.id.action_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
//                return true;
//            case R.id.sign_out_menu:
//                AuthUI.getInstance().signOut(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
