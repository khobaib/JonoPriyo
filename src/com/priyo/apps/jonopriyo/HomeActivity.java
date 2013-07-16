package com.priyo.apps.jonopriyo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.priyo.apps.jonopriyo.adapter.NothingSelectedSpinnerAdapter;
import com.priyo.apps.jonopriyo.gcm.ServerUtilities;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;
import com.priyo.apps.jonopriyo.utility.WakeLocker;
import com.priyo.apps.lazylist.ImageLoader;


// 06-23 11:35:58.002: D/JsonParser(13431): sb = {"error":"invalid verb"} after 24 minute

public class HomeActivity extends Activity {

    JonopriyoApplication appInstance;
//    ImageLoader imageLoader;
    Long userId;

    ImageView ProfilePic;    
    RelativeLayout rlLoading;
//    public static int PROFILE_PIC_LOADING;
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;


    final String[] menuItems = {"About us"};
//    Spinner sMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);   

        appInstance = (JonopriyoApplication) getApplication();
        
        userId = appInstance.getUserId();

        ProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        
        rlLoading = (RelativeLayout) findViewById(R.id.loading_Panel);


//        sMenu = (Spinner) findViewById(R.id.s_menu);
//        ArrayAdapter<String> sAdapter= new ArrayAdapter<String>(this, R.layout.my_simple_dialog_item, menuItems);
//        sMenu.setAdapter(new NothingSelectedSpinnerAdapter(sAdapter, R.layout.spinner_row_nothing_selected,                         
//                HomeActivity.this));
//        //        sMenu.setAdapter(sAdapter);
//        //        sAdapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
//
//        sMenu.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
//                switch(pos){
//                    case 1:
//                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
//                        break;
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
        
        
        if(Utility.hasInternet(HomeActivity.this)){
            
            // Make sure the device has the proper dependencies.
               GCMRegistrar.checkDevice(this);
        
               // Make sure the manifest was properly set - comment out this line
               // while developing the app, then uncomment it when it's ready.
               GCMRegistrar.checkManifest(this);
               
               registerReceiver(mHandleMessageReceiver, new IntentFilter(Constants.DISPLAY_MESSAGE_ACTION));
               
               // Get GCM registration id
               final String regId = GCMRegistrar.getRegistrationId(HomeActivity.this);
               Log.d(">>>>><<<<<", "regId = " + regId);
        
               // Check if regid already presents
               if (regId.equals("")) {
                   // Registration is not present, register now with GCM          
                   GCMRegistrar.register(this, Constants.SENDER_ID);
                   Log.d(">>>>><<<<<", "regId = " + GCMRegistrar.getRegistrationId(HomeActivity.this));
               } else {
                   // Device is already registered on GCM
                   if (GCMRegistrar.isRegisteredOnServer(HomeActivity.this)) {
                       // Skips registration.             
//                       Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                   } else {
                       // Try to register again, but not in the UI thread.
                       // It's also necessary to cancel the thread onDestroy(),
                       // hence the use of AsyncTask instead of a raw thread.
                       final Context context = this;
                       mRegisterTask = new AsyncTask<Void, Void, Void>() {
        
                           @Override
                           protected Void doInBackground(Void... params) {
                               // Register on our server
                               // On server creates a new user
                               boolean registered = ServerUtilities.register(context, userId, regId);
                               if (!registered) {
                                   GCMRegistrar.unregister(context);
                               }
                               return null;
                           }
        
                           @Override
                           protected void onPostExecute(Void result) {
                               mRegisterTask = null;
                           }
        
                       };
                       mRegisterTask.execute(null, null, null);
                   }
               }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        String imageUrl = appInstance.getProfileImageUrl();
        Log.d(".......>>>>>", "image url = " + imageUrl);

        if(Utility.hasInternet(HomeActivity.this)){            
            if(imageUrl != null && !imageUrl.equals("")){
                rlLoading.setVisibility(View.VISIBLE);
                Log.d("<<<<<>>>>", "rlLoading visible now");
                ImageLoader imageLoader = new ImageLoader(HomeActivity.this, rlLoading);
                imageLoader.DisplayImage(imageUrl, ProfilePic);
                
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
    }


    public void onClickNewPolls(View v){
        startActivity(new Intent(HomeActivity.this, NewPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void onClickAllPolls(View v){
        startActivity(new Intent(HomeActivity.this, AllPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickMyPolls(View v){
        startActivity(new Intent(HomeActivity.this, MyPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickWinners(View v){
        startActivity(new Intent(HomeActivity.this, WinnerListActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClickProfile(View v){
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClickShare(View v){

    }

    public void onClickProfilePic(View v){
        startActivity(new Intent(HomeActivity.this, UploadPicActivity.class));
        overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
    }

    public void onClickAboutUs(View v){
        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//        sMenu.performClick();
        //        ArrayAdapter<String> sAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        //        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //        
        //        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(HomeActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar));
        //        builder.setAdapter(sAdapter, new DialogInterface.OnClickListener() {
        //            
        //            @Override
        //            public void onClick(DialogInterface dialog, int which) {
        //                Toast.makeText(getApplicationContext(), menuItems[which], Toast.LENGTH_SHORT).show();
        //                
        //            }
        //        });
        //        builder.show();
    }
    
    
    
    
    
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(Constants.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
//            lblMessage.append(newMessage + "\n");          
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            WakeLocker.release();
        }
    };
    
    
    // NEED TO CHECK THIS
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }



}
