package com.roi.greenberg.michayavlemi.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

/**
 * Created by greenberg on 19/03/2018.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (TimePickerDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        final Calendar c;
        int hour = 0;
        int minuts = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();

            hour = c.get(Calendar.HOUR);
            minuts = c.get(Calendar.MINUTE);
        }

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minuts, true);
    }

    public void onTimeSet(TimePicker view, int hour, int minuts) {

//        DateText.setText(String.valueOf(day) + "/"
//                + String.valueOf(month + 1) + "/" + String.valueOf(year));
        // set selected date into datepicker also
        Log.d("TIMEPICKER", hour + ":" + minuts);

        listener.onFinishTimeDialog(hour, minuts);

    }

    public interface TimePickerDialogListener {
        void onFinishTimeDialog(int hour, int minuts);
    }
}
