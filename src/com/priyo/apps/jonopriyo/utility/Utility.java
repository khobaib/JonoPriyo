package com.priyo.apps.jonopriyo.utility;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class Utility {

    public static final String[] month_name = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    public static final String[] profession_array = {
        "student", "teacher", "govt employee", "private service"
    };

    public static final String[] education_array = {
        "student", "S.Sc.", "H.Sc.", "B.Sc", "M.Sc.", "PhD"
    };

    public static final String[] sex_array = {"male", "female"};

    //    public static final String[] country_list = {
    //      "USA", "Bangladesh", "Belgium", "Barmuda", "Barbados", "Belarus"  
    //    };
    //    
    //    public static final String[] city_list = {
    //        "Abuja", "Abu dhabi", "Abrams", "Ace", "Acland", "Dhaka"
    //    };
    //    
    //    public static final String[] area_list = {
    //        "Gulshan", "New-market", "BUET", "Dhanmondi"
    //    };


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
            dateToShow = day + " " + month_name[month-1] + " " + year;
        return dateToShow;
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

    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Constants.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
