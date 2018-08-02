package com.roi.greenberg.michayavlemi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.roi.greenberg.michayavlemi.MainFragment;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.activities.MainActivity;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.UserInList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.roi.greenberg.michayavlemi.utils.Constants.EVENTS;
import static com.roi.greenberg.michayavlemi.utils.Constants.USERS;

/**
 * Created by greenberg on 19/03/2018.
 */

public class AddNewEventFragment extends DialogFragment implements
        DatePickerFragment.DatePickerDialogListener,
        TimePickerFragment.TimePickerDialogListener{

    private EditText mName;
    private TextView mDate;
    private TextView mTime;
    private EditText mLocation;
    private int year;
    private int month;
    private int day;
    private int dayOfWeek;
    private int hour;
    private int minutes;

    private static final String TAG = "AddNewEventFragment";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog");
        final MainActivity mainActivity = (MainActivity)getActivity();
        final Fragment fragment = this;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mainActivity);
        View mView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_add_new_event, null);
        mBuilder.setView(mView);
        mName =  mView.findViewById(R.id.etEventName);
        mDate = mView.findViewById(R.id.tvDate);
        mTime = mView.findViewById(R.id.tvTime);
        mLocation = mView.findViewById(R.id.etLocation);
//        Button mButtonOk = mView.findViewById(R.id.btnOk);
//        Button mButtonCancel = mView.findViewById(R.id.btnCancel);

        mDate.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                DialogFragment DateFragment = new DatePickerFragment();
                DateFragment.setTargetFragment(fragment, 0);
                DateFragment.show(fragmentManager, "datePicker");
            }

        });

        mTime.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                DialogFragment DateFragment = new TimePickerFragment();
                DateFragment.setTargetFragment(fragment, 0);
                DateFragment.show(fragmentManager, "timePicker");
            }

        });



        mBuilder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                String name = mName.getText().toString();

                if (!name.isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minutes);

                    HashMap<String, Object> date = new HashMap<>();
                    date.put("date", calendar.getTimeInMillis());
                    String location = mLocation.getText().toString();

                    Log.d(TAG, "Add event: " + name);
//                    User eventKey = FirebaseDatabase.getInstance().getReference().child("events").push().getKey();

                    EventDetails eventDetails = new EventDetails(name, date, location, 1); //TODO change date
                    Map<String, Object> eventDetailsValues = eventDetails.toMap();
//
                    Map<String, Object> user = new HashMap<>();
                    user.put(MainFragment.mUser.getUid(), true);
                    eventDetailsValues.put("users", user);

//                    childUpdates.put("/events/" + eventKey + "/details", eventDetailsValues);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Get a new write batch
                    WriteBatch batch = db.batch();

                    DocumentReference eventRef = db.collection(EVENTS).document();
                    batch.set(eventRef, eventDetailsValues);

                    DocumentReference userRef = eventRef.collection(USERS).document(MainFragment.mUser.getUid());
//                    childUpdates.put("type", "owner");
//                    childUpdates.put("expenses", 0);
                    batch.set(userRef, new UserInList("owner", 0));

                    // Commit the batch
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

                    dialogInterface.dismiss();
                } else {
                    Toast.makeText(mainActivity,"Please enter event name", Toast.LENGTH_SHORT).show();
                }
            }
        });



        final AlertDialog dialog = mBuilder.create();

        return dialog;
    }

    @Override
    public void onFinishDateDialog(int year, int month, int day, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        String date = day + "/" + month + "/" + year;
        mDate.setText(date);
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public void onFinishTimeDialog(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
        String time = hour + ":" + minutes;
        mTime.setText(time);
    }
}
