package com.roi.greenberg.michayavlemi.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.GregorianCalendar;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

//        DateText.setText(String.valueOf(day) + "/"
//                + String.valueOf(month + 1) + "/" + String.valueOf(year));
        // set selected date into datepicker also
        Log.d("DATEPICKER", year + " " + month + " " + day);
        int dayOfWeek;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            GregorianCalendar date = new GregorianCalendar(year, month, day-1);
            dayOfWeek=date.get(GregorianCalendar.DAY_OF_WEEK);
        } else {
            java.util.GregorianCalendar date = new java.util.GregorianCalendar(year, month, day-1);
            dayOfWeek=date.get(java.util.GregorianCalendar.DAY_OF_WEEK);
        }

        listener.onFinishDateDialog(year, month, day, dayOfWeek);

    }

    public interface DatePickerDialogListener {
        void onFinishDateDialog(int year, int month, int day, int dayOfWeek);
    }
}
