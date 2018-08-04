package com.roi.greenberg.michayavlemi.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.activities.MainActivity;
import com.roi.greenberg.michayavlemi.adapters.EventAdapter;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.models.User;
import com.roi.greenberg.michayavlemi.models.UserInList;
import com.roi.greenberg.michayavlemi.utils.UserHandler;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.roi.greenberg.michayavlemi.utils.Constants.EVENTS;
import static com.roi.greenberg.michayavlemi.utils.Constants.EVENT_ID;
import static com.roi.greenberg.michayavlemi.utils.Constants.EVENT_NAME;
import static com.roi.greenberg.michayavlemi.utils.Constants.USERS;
import static com.roi.greenberg.michayavlemi.utils.Constants.USER_NAME;
import static com.roi.greenberg.michayavlemi.utils.Utils.updateLong;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "MainFragment";
    private static final String ANONYMOUS = "anonymous";

    private EventAdapter mEventAdapter;

    private UserHandler userHandler;

    private FirebaseFirestore mFirestoreDatabase;

//    private Context mContext;
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseAuth.AuthStateListener mAuthStateListener;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    private static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(mFirestoreDatabase == null) {
            mFirestoreDatabase = FirebaseFirestore.getInstance();
        }

//        mContext = this;
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
                Toast.makeText(getContext(), "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(getContext(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
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
        User mUser = new User(userName, email, photoUrl, userUid);

        userHandler = UserHandler.getInstance();
        userHandler.setUser(mUser);

        mFirestoreDatabase.collection(USERS).document(userUid).set(mUser, SetOptions.merge());
        FloatingActionButton fab = getView().findViewById(R.id.fab_new_event);


        Query eventQuery = mFirestoreDatabase.collection(EVENTS).whereEqualTo(USERS + "." + mUser.getUid(), true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView mRecyclerView = getView().findViewById(R.id.rv_main);

        FirestoreRecyclerOptions<EventDetails> options = new FirestoreRecyclerOptions.Builder<EventDetails>()
                .setLifecycleOwner(this)
                .setQuery(eventQuery, EventDetails.class)
                .build();

        mEventAdapter = new EventAdapter(options, (MainActivity)getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mEventAdapter);
        mEventAdapter.startListening();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewEventFragment();
                DateFragment.show(fragmentManager, "addNewEvent");

            }
        });

        FirebaseDynamicLinks.getInstance().getDynamicLink(getActivity().getIntent())
                .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
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
                            final String eventName = deepLink.getQueryParameter(EVENT_NAME);
                            final String eventId = deepLink.getQueryParameter(EVENT_ID);
                            if (userName == null || eventId == null) {
                                return;
                            }
                            //Toast.makeText(MainActivity.this, "UserID= " +userID + "ListID= " + listID, Toast.LENGTH_LONG).show();

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Are you sure you want to add a new event?")
                                    .setMessage(userName + " invite you to join " + eventName)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Toast.makeText(getContext(), "Yaay", Toast.LENGTH_SHORT).show();
                                            //                            mDatabaseReference.child(EVENTS).child(eventId).child(USERS).child(MainActivity.mUser.getUid()).setValue(new UserInList(MainActivity.mUser, 0));
                                            mFirestoreDatabase.collection(EVENTS).document(eventId).collection(USERS).document(userHandler.getUser().getUid())
                                                    .set(new UserInList("user", 0), SetOptions.merge());
                                            updateLong(FirebaseFirestore.getInstance().document(EVENTS + "/" + eventId), "numOfUsers", 1);
                                        }
                                    }).setNegativeButton(android.R.string.no, null).show();
                        }
                    }
                });

    }

    private void onSignedOutCleanup() {
        if (userHandler != null) {
            userHandler.getUser().setUsername(ANONYMOUS);
        }
//        if (mOwnListAdapter != null)
//            mOwnListAdapter.cleanup();
        //detachDatabaseReadListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mEventAdapter != null) {
            mEventAdapter.startListening();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mEventAdapter != null) {
            mEventAdapter.stopListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
