package com.priyo.apps.jonopriyo.utility;

import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

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
    
    
    public static boolean hasInternet(Context context) {
        NetworkInfo networkInfo = (NetworkInfo) ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (networkInfo != null
                && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

}
