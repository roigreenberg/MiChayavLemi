package com.roi.greenberg.michayavlemi.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Created by greenberg on 19/03/2018.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (DatePickerDialogListener) getTargetFragment();
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
        int year = 0;
        int month = 0;
        int day = 0;
        c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

//        DateText.setText(User.valueOf(day) + "/"
//                + User.valueOf(month + 1) + "/" + User.valueOf(year));
        // set selected date into datepicker also
        Log.d("DATEPICKER", year + " " + month + " " + day);
        int dayOfWeek;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            GregorianCalendar date = new GregorianCalendar(year, month, day-1);
        } else {
            java.util.GregorianCalendar date = new java.util.GregorianCalendar(year, month, day-1);
        }

        listener.onFinishDateDialog(year, month, day);

    }

    public interface DatePickerDialogListener {
        void onFinishDateDialog(int year, int month, int day);
    }
}
