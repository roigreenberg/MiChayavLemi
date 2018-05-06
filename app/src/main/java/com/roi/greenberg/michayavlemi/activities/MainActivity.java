package com.roi.greenberg.michayavlemi.activities;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.adapters.EventAdapter;
import com.roi.greenberg.michayavlemi.fragments.AddNewEventFragment;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.models.User;

import java.util.Arrays;
import java.util.List;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.roi.greenberg.michayavlemi.models.UserInList;
//import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;

public class MainActivity extends AppCompatActivity{

    private static final int RC_SIGN_IN = 1;
    private static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private List<EventDetails> mEventDetails;
//    private static FirebaseDatabase mFirebaseDatabase;
//    private static DatabaseReference mDatabaseReference;

    private FirebaseFirestore mFirestoreDatabase;

    private EventAdapter mEventAdapter;
    private Context mContext;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseAuth.AuthStateListener mAuthStateListener;

    public static User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (mFirebaseDatabase == null) {
//            mFirebaseDatabase = FirebaseDatabase.getInstance();
//            mFirebaseDatabase.setPersistenceEnabled(true);
//            mDatabaseReference = mFirebaseDatabase.getReference();
//        }

        if(mFirestoreDatabase == null) {
            mFirestoreDatabase = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
                    .setPersistenceEnabled(true)
                    .build();
            mFirestoreDatabase.setFirestoreSettings(settings);
        }

        mContext = this;
        mFirebaseAuth = FirebaseAuth.getInstance();

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

        String userName = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        String userUid = user.getUid();
        Log.d(TAG, userName + " " + email + " " + photoUrl + " " + userUid);
        mUser = new User(userName, email, photoUrl, userUid);
//        mDatabaseReference.child(USERS).child(userUid).child(DETAILS).setValue(mUser);
//        HashMap<User, Object> userDetails = new HashMap<>();
//        userDetails.put(DETAILS, mUser);
        mFirestoreDatabase.collection(USERS).document(userUid).set(mUser, SetOptions.merge());
        FloatingActionButton fab = findViewById(R.id.fab_new_event);

//        DatabaseReference eventRef = mDatabaseReference.child(USERS).child(mUser.getUid()).child(EVENTS);
//        Query eventQuery = eventRef.orderByKey();
        Query eventQuery = mFirestoreDatabase.collection(EVENTS).whereEqualTo(USERS + "." + mUser.getUid(), true);
//
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.rv_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirestoreRecyclerOptions<EventDetails> options = new FirestoreRecyclerOptions.Builder<EventDetails>()
                .setLifecycleOwner(this)
                .setQuery(eventQuery, EventDetails.class)
                .build();

        mEventAdapter = new EventAdapter(options, this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mEventAdapter);
        mEventAdapter.startListening();

        //Button btnToListOfProducts = findViewById(R.id.btn_item_list_of_products);
        //btnToListOfProducts.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent toListOfItemsIntent = new Intent(MainActivity.this,EventActivity.class);
        //        startActivity(toListOfItemsIntent);
        //    }
        //});

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewEventFragment();
                DateFragment.show(fragmentManager, "addNewEvent");

//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialog_add_new_event, null);
//                final EditText mName =  mView.findViewById(R.id.etEventName);
//                final EditText mDate = mView.findViewById(R.id.etDate);
//                final EditText mLocation = mView.findViewById(R.id.etLocation);
//                Button mButtonOk = mView.findViewById(R.id.btnOk);
//                Button mButtonCancel = mView.findViewById(R.id.btnCancel);
//
//                mDate.setOnClickListener( new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//                        DialogFragment DateFragment = new DatePickerFragment();
//                        DateFragment.show(getFragmentManager(), "datePicker");
//                    }
//
//                });
//
//
//                mBuilder.setView(mView);
//                final AlertDialog dialog = mBuilder.create();
//                dialog.show();
//                mButtonOk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        User name = mName.getText().toString();
//                        HashMap<User, Object> date = new HashMap<>();
//                        date.put("Date", mDate);
//                        User location = mLocation.getText().toString();
//                        if (!name.isEmpty()) {
//                            Toast.makeText(MainActivity.this,"Add event: " + name, Toast.LENGTH_SHORT).show();
//                            User key = mDatabaseReference.child("events").push().getKey();
//                            EventDetails eventDetails = new EventDetails(name, date, location);
//                            Map<User, Object> eventDetailsValues = eventDetails.toMap();
//
//                            Map<User, Object> childUpdates = new HashMap<>();
//
//                            childUpdates.put("/events/" + key + "/details", eventDetailsValues);
//                            childUpdates.put("/events/" + key + "/users/" + mUser.getUid(), mUser);
//                            mDatabaseReference.updateChildren(childUpdates);
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(MainActivity.this,"Please enter event name", Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//                    }
//                });
//                mButtonCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
            }
        });

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
            .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                @Override
                public void onSuccess(PendingDynamicLinkData data) {
                if (data == null) {
                    Log.d(TAG, "getInvitation: no data");
                    return;
                }

                // Get the deep link
                Uri deepLink = data.getLink();

                // Extract invite
                FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                if (invite != null) {
                    String invitationId = invite.getInvitationId();
                }

                // Handle the deep link
                // [START_EXCLUDE]
                Log.d(TAG, "deepLink:" + deepLink);
                if (deepLink != null) {

                    String userName = deepLink.getQueryParameter(USER_NAME); //TODO is it needed?
                    final String eventName = deepLink.getQueryParameter(EVENT_NAME); //TODO change name
                    final String eventId = deepLink.getQueryParameter(EVENT_ID); //TODO change name
                    if (userName == null || eventId == null) {
                        return;
                    }
                    //Toast.makeText(MainActivity.this, "UserID= " +userID + "ListID= " + listID, Toast.LENGTH_LONG).show();

                    new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure you want to add a new event?")
                        .setMessage(userName + " invite you to join " + eventName)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
//                            mDatabaseReference.child(EVENTS).child(eventId).child(USERS).child(MainActivity.mUser.getUid()).setValue(new UserInList(MainActivity.mUser, 0));
                                mFirestoreDatabase.collection(EVENTS).document(eventId).collection(USERS).document(MainActivity.mUser.getUid())
                                        .set(new UserInList("user", 0), SetOptions.merge());
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                }
                }
            });

    }

    private void onSignedOutCleanup() {
        if (mUser != null) {
            mUser.setUsername(ANONYMOUS);
        }
//        if (mOwnListAdapter != null)
//            mOwnListAdapter.cleanup();
        //detachDatabaseReadListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();



        if (mEventAdapter != null) {
            mEventAdapter.startListening();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mEventAdapter != null) {
            mEventAdapter.stopListening();
        }

        //mOwnListAdapter.cleanup();
        //detachDatabaseReadListener();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
