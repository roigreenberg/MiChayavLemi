package com.roi.greenberg.michayavlemi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roi.greenberg.michayavlemi.EventActivity;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.User;

import java.util.ArrayList;

/**
 * Created by greenberg on 19/03/2018.
 */

public class AddNewItemFragment extends DialogFragment {

    private EditText mItemName;
    private Spinner mUser;
    private EditText mPrice;
    private ArrayList<User> allUsers;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("ADDNEWEVENT", "onCreateDialog");
        final EventActivity eventActivity = (EventActivity) getActivity();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(eventActivity);
        View mView = LayoutInflater.from(eventActivity).inflate(R.layout.dialog_add_new_item, null);
        mBuilder.setView(mView);
        mItemName = mView.findViewById(R.id.et_new_item_name);
        mUser = mView.findViewById(R.id.sp_new_item_user);
        mPrice = mView.findViewById(R.id.et_new_item_price);
//        Button mButtonOk = mView.findViewById(R.id.btnOk);
//        Button mButtonCancel = mView.findViewById(R.id.btnCancel);

        allUsers = new ArrayList<>();

        allUsers.add(new User("Select user...", null, null, null));
        FirebaseDatabase.getInstance().getReference()
                .child("events")
                .child(eventActivity.mEventId)
                .child("users")
                .orderByChild("details/username").addListenerForSingleValueEvent(new ValueEventListener() {
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

        Log.d("ADDITEM", allUsers.toString());

        ArrayAdapter<User> adapter = new ArrayAdapter<>(eventActivity,
                R.layout.simple_spinner_dropdown_item, allUsers);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUser.setAdapter(adapter);


        mBuilder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                String name = mItemName.getText().toString();


                if (!name.isEmpty()) {
                    User user = (User) mUser.getSelectedItem();
                    String price_As_string = mPrice.getText().toString();
                    float price = 0;
                    if (!(price_As_string.isEmpty())) {
                        price = Float.valueOf(price_As_string);
                    }

                    FirebaseDatabase.getInstance().getReference()
                            .child("events")
                            .child(eventActivity.mEventId)
                            .child("items")
                            .push()
                            .setValue(new Item(name, user, price));
                } else {
                    Toast.makeText(eventActivity, "Please enter event name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final AlertDialog dialog = mBuilder.create();

        return dialog;
    }
}
