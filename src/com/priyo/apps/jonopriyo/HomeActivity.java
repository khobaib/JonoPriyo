package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.priyo.apps.jonopriyo.gcm.ServerUtilities;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;
import com.priyo.apps.jonopriyo.utility.WakeLocker;
import com.priyo.apps.lazylist.ImageLoader;


// 06-23 11:35:58.002: D/JsonParser(13431): sb = {"error":"invalid verb"} after 24 minute

public class HomeActivity extends Activity {

    JonopriyoApplication appInstance;
    JsonParser jsonParser;
    Long userId;

    Poll latestPoll;
    ImageView LatestPollImage;
    TextView LatestPollTitle, LatestPollParticipation;
    Button LatestPollVoteNow;
    ProgressBar LatestPollLoading;
    
    Winner lastWinner;
    ImageView LastWinnerPic;
    TextView LastWinnerName, LastWinnerAddress, LastWinnerPoll, WInnerTitle;
    ProgressBar LastWinnerLoading;

    ImageView ProfilePic;    
    RelativeLayout rlLoading;    

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    final String[] menuItems = {"About us"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home);   

        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();

        userId = appInstance.getUserId();

        ProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);

        rlLoading = (RelativeLayout) findViewById(R.id.loading_Panel);

        LatestPollImage = (ImageView) findViewById(R.id.iv_latest_poll_image);
        LatestPollTitle = (TextView) findViewById(R.id.tv_latest_poll_title);
        LatestPollParticipation = (TextView) findViewById(R.id.tv_latest_poll_participation);
        LatestPollVoteNow = (Button) findViewById(R.id.b_vote_now);
        LatestPollLoading = (ProgressBar) findViewById(R.id.pb_progress_poll);
        
        LastWinnerPic = (ImageView) findViewById(R.id.iv_last_winner_pic);
        LastWinnerPoll = (TextView) findViewById(R.id.tv_last_poll_title);
        WInnerTitle = (TextView) findViewById(R.id.tv_last_winner_title);
        LastWinnerName = (TextView) findViewById(R.id.tv_last_winner_name);
        LastWinnerAddress = (TextView) findViewById(R.id.tv_last_winner_address);
        LastWinnerLoading = (ProgressBar) findViewById(R.id.pb_progress_winner);



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

        LatestPollImage.setVisibility(View.GONE);
        LatestPollTitle.setVisibility(View.GONE);
        LatestPollParticipation.setVisibility(View.GONE);
        LatestPollVoteNow.setVisibility(View.GONE);
        LatestPollLoading.setVisibility(View.VISIBLE);
        
        LastWinnerPic.setVisibility(View.GONE);
        LastWinnerPoll.setVisibility(View.GONE);
        WInnerTitle.setVisibility(View.GONE);
        LastWinnerName.setVisibility(View.GONE);
        LastWinnerAddress.setVisibility(View.GONE);
        LastWinnerLoading.setVisibility(View.VISIBLE);

        if(Utility.hasInternet(HomeActivity.this)){   

            new RetrieveLatestPoll().execute();
            new RetrieveLastWinner().execute();

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
    }

    public void onClickVoteNow(View v){
        if(latestPoll != null){
            appInstance.setSelectedPoll(latestPoll);
            Log.d("<<<<<<>>>>>>>>", "poll number = " + latestPoll.getNumber());

            Intent i = null;
            if(latestPoll.getIsCastByMe()){
                i = new Intent(HomeActivity.this, AllPollsDetailsActivity.class);
                i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_MY_POLLS);
            }
            else{
                i = new Intent(HomeActivity.this, NewPollDetailsActivity.class);
            }
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }


    private class RetrieveLatestPoll extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            latestPoll = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_LATEST_POLL));

            String token = appInstance.getAccessToken();

            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET,
                    rootUrl, urlParam, null, token);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject result = response.getjObj();
                try {
                    JSONObject pollObj = result.getJSONObject("latest_poll");
                    latestPoll = Poll.parsePoll(pollObj);
                    if(latestPoll != null)
                        return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }            
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean success) {
            
            if(success){
                LatestPollImage.setVisibility(View.VISIBLE);
                LatestPollTitle.setVisibility(View.VISIBLE);
                LatestPollParticipation.setVisibility(View.VISIBLE);
                LatestPollVoteNow.setVisibility(View.VISIBLE);
                
                ImageLoader imageLoader = new ImageLoader(HomeActivity.this);
                imageLoader.DisplayImage(latestPoll.getImageUrl(), LatestPollImage);
                
                LatestPollTitle.setText(latestPoll.getQuestion());
                LatestPollParticipation.setText("Participation count: " + latestPoll.getParticipationCount());

            }
            else{    
                LatestPollTitle.setVisibility(View.VISIBLE);
                LatestPollTitle.setText("No poll found");
            }
            LatestPollLoading.setVisibility(View.GONE);

        }
    }
    
    
    
    private class RetrieveLastWinner extends AsyncTask<Void, Void, Boolean> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lastWinner = null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_LAST_POLL_WINNER));

            String token = appInstance.getAccessToken();

            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET,
                    rootUrl, urlParam, null, token);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonResponse = response.getjObj();
                try {
                    String responseType = jsonResponse.getString("response");
                    if(responseType.equals("success")){
                        JSONObject winnerObj = jsonResponse.getJSONObject("last_poll_winner");
                        lastWinner = Winner.parseWinner(winnerObj);
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } 

            return false;
        }


        @Override
        protected void onPostExecute(Boolean success) {
            
            if(success){
                LastWinnerPic.setVisibility(View.VISIBLE);
                LastWinnerName.setVisibility(View.VISIBLE);
                LastWinnerAddress.setVisibility(View.VISIBLE);
                LastWinnerPoll.setVisibility(View.VISIBLE);
                WInnerTitle.setVisibility(View.VISIBLE);
                
                
                ImageLoader imageLoader = new ImageLoader(HomeActivity.this);
                imageLoader.DisplayImage(lastWinner.getWinnerPicUrl(), LastWinnerPic);
                
                LastWinnerName.setText(lastWinner.getWinnerName());
                LastWinnerAddress.setText(lastWinner.getAddress());
            }
            else{    
                LastWinnerPoll.setVisibility(View.VISIBLE);
                LastWinnerPoll.setText("No winner found.");
            }
            LastWinnerLoading.setVisibility(View.GONE);

        }
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
