package com.roi.greenberg.michayavlemi.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roi.greenberg.michayavlemi.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;

public class Utils {

    public static ValueEventListener addLongListener(final TextView textView){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long total = dataSnapshot.getValue(Long.class);
                textView.setText(String.valueOf(total));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    /**
     * RecycleView initial
     */
    public static <A extends FirebaseRecyclerAdapter, C> void initRecycleView(Context context, RecyclerView view, A adapter) {
        RecyclerView mRecyclerView = view;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
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
        final ArrayList<User> allUsers = new ArrayList<>();

        allUsers.add(new User("Select user...", null, null, null));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userData: dataSnapshot.getChildren()) {
                    Log.d("ADDITEM", userData.toString());
                    User user = userData.child("details").getValue(User.class);
                    if (user != null) {
                        Log.d("ADDITEM", user.getUsername());
                        allUsers.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return allUsers;
    }

}
