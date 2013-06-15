package com.priyo.apps.jonopriyo.fragment;

import java.util.Calendar;

import com.priyo.apps.jonopriyo.RegisterActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment{
    
    static int mYear;
    static int mMonth;
    static int mDay;
    
    public static DatePickerFragment newInstance(Calendar calTime) {
        DatePickerFragment dialog = new DatePickerFragment();

        mYear = calTime.get(Calendar.YEAR);
        mMonth = calTime.get(Calendar.MONTH);
        mDay = calTime.get(Calendar.DAY_OF_MONTH);
        /*I dont really see the purpose of the below*/
        Bundle args = new Bundle();
        args.putString("title", "Set Date");
        dialog.setArguments(args);//setArguments can only be called before fragment is attached to an activity, so right after the fragment is created


        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (RegisterActivity)getActivity(), mYear, mMonth, mDay);
    }


}
