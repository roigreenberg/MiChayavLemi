package com.roi.greenberg.michayavlemi.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.roi.greenberg.michayavlemi.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;

public class Utils {

//    public static ValueEventListener addLongListener(final TextView textView){
//        return new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Long total = dataSnapshot.getValue(Long.class);
//                textView.setText(java.lang.User.valueOf(total));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//    }

    /**
     * RecycleView initial
     */
    public static void initRecycleView(Context context, RecyclerView view, FirestoreRecyclerAdapter adapter) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mLayoutManager.setReverseLayout(true);//list order
        view.setLayoutManager(mLayoutManager);
        view.setAdapter(adapter);
    }

    public static void ShareList(Context context, String userName, String eventName, String eventId){
        Intent sendIntent = new Intent();

        Uri BASE_URI = Uri.parse("https://michayavlemi.roigreenberg.com/add_new_event");

        Uri APP_URI = BASE_URI.buildUpon()
                .appendQueryParameter(USER_NAME, userName)
                .appendQueryParameter(EVENT_NAME, eventName)
                .appendQueryParameter(EVENT_ID, eventId)
                .build();


        String encodedUri = null;
        try {
            encodedUri = URLEncoder.encode(APP_URI.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri deepLink = Uri.parse("https://vy3t6.app.goo.gl/?link="+ encodedUri +"&apn=com.roi.greenberg.michayavlemi");

        String msg = "Hey, check this out: " + deepLink.toString();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static ArrayList<User> getUserNames(Query query) {
        final String TAG = "getUserNames";
        final ArrayList<User> allUsers = new ArrayList<>();

        allUsers.add(new User("Select user...", null, null, null));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userData : task.getResult()) {
                        Log.d(TAG, userData.getId() + " => " + userData.getData());
                        Log.d("ADDITEM", userData.toString());
                        User user = userData.toObject(User.class);
                        Log.d("ADDITEM", user.getUsername());
                        allUsers.add(user);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        Log.d("ADDITEM", "finish");
        return allUsers;
    }

    public static void updateLong(final DocumentReference ref, final String field, final long amount) {
        final String TAG = "updateLong";
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(ref);

                long newValue = amount + (snapshot.getLong(field) != null ? snapshot.getLong(field) : 0);

                transaction.update(ref, field, newValue);

                return newValue;
            }

        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Log.d(TAG, "Transaction success: " + result);
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }
}
