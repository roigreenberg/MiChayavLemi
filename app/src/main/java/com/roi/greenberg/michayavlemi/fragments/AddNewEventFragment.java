package com.roi.greenberg.michayavlemi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.roi.greenberg.michayavlemi.EventDetails;
import com.roi.greenberg.michayavlemi.MainActivity;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.UserWithExpenses;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("ADDNEWEVENT", "onCreateDialog");
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
                android.support.v4.app.FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                DialogFragment DateFragment = new DatePickerFragment();
                DateFragment.setTargetFragment(fragment, 0);
                DateFragment.show(fragmentManager, "datePicker");
            }

        });

        mTime.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                DialogFragment DateFragment = new TimePickerFragment();
                DateFragment.setTargetFragment(fragment, 0);
                DateFragment.show(fragmentManager, "datePicker");
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

                            Toast.makeText(mainActivity,"Add event: " + name, Toast.LENGTH_SHORT).show();
                            String eventKey = FirebaseDatabase.getInstance().getReference().child("events").push().getKey();
                            EventDetails eventDetails = new EventDetails(name, date, location); //TODO change date
                            Map<String, Object> eventDetailsValues = eventDetails.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();

                            childUpdates.put("/events/" + eventKey + "/details", eventDetailsValues);
                            childUpdates.put("/events/" + eventKey + "/users/" + MainActivity.mUser.getUid(), new UserWithExpenses(MainActivity.mUser, 0));
                            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
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
