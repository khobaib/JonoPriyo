package com.priyo.apps.jonopriyo.utility;

import java.io.File;
import java.util.Collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utility {

    public static final String[] month_name = {
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };
    
    public static final String[] month_name_bangla = {
        "জানুয়ারী", "ফেব্রুয়ারী", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };
    
    
    public static final String[] PHONE_NUMBER_PREFIX= {
      "017", "015", "016", "018", "019", "011"  
    };

//    public static final String[] profession_array = {
//        "student", "teacher", "govt employee", "private service"
//    };
//
//    public static final String[] education_array = {
//        "student", "S.Sc.", "H.Sc.", "B.Sc", "M.Sc.", "PhD"
//    };

    public static final String[] sex_array = {"male", "female"};



    public static boolean createDirectory() {
        if (!SdIsPresent())
            return false;

        File directory = Constants.APP_DIRECTORY;
        if (!directory.exists())
            directory.mkdir();
        return true;
    }

    /** Returns whether an SD card is present and writable **/
    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    
    /*
     * dateFromDb -> yyyy-mm-dd
     * we want to show in the format dd (month_name) year
     */
    public static String parseDate(String dateFromDb){
        String year = dateFromDb.substring(0, 4);
        int month = Integer.parseInt(dateFromDb.substring(5, 7));
        String day = dateFromDb.substring(8, 10);
        Log.d(">>>>>>>>", "year = " + year + " month = " + month + " day = " + day);
        String dateToShow = dateFromDb;
        if(month > 0 && month <=12)
            dateToShow = day + " " + month_name_bangla[month-1] + " " + year;
        return dateToShow;
    }
    
    
    public static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }


    //    public static boolean hasInternet(Context context) {
    //        NetworkInfo networkInfo = (NetworkInfo) ((ConnectivityManager) context
    //                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    //
    //        if (networkInfo != null
    //                && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
    //                && networkInfo.isConnected()) {
    //            return true;
    //        }
    //
    //        return false;
    //    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    public static void displayMessage(Context context, String message) {
//        Intent intent = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
//        intent.putExtra(Constants.EXTRA_MESSAGE, message);
//        context.sendBroadcast(intent);
//    }
    
    
   public static void HandlingHintsInEditText(final Context context, final EditText et, final String hintText) {
        
        final Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/suttony.ttf");
        et.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et.setHint("");
                et.setTypeface(Typeface.SANS_SERIF); 

                return false;
            }
        });

        et.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et.setHint("");
                    ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    et.requestFocus();
                }
                else{
                    if(et.getText().toString().equals("")){
                        et.setTypeface(tf);
                        et.setHint(hintText);
                        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                }

            }
        });                
    }

}
