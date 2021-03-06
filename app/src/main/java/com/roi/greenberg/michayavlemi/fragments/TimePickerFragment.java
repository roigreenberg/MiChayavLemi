package com.roi.greenberg.michayavlemi.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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
        int minutes = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();

            hour = c.get(Calendar.HOUR);
            minutes = c.get(Calendar.MINUTE);
        }

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minutes, true);
    }

    public void onTimeSet(TimePicker view, int hour, int minutes) {

//        DateText.setText(User.valueOf(day) + "/"
//                + User.valueOf(month + 1) + "/" + User.valueOf(year));
        // set selected date into datepicker also
        Log.d("TIMEPICKER", hour + ":" + minutes);

        listener.onFinishTimeDialog(hour, minutes);

    }

    public interface TimePickerDialogListener {
        void onFinishTimeDialog(int hour, int minutes);
    }
}
