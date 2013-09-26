package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.SessionTracker;
import com.facebook.model.GraphObject;
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

    private static final String TAG = HomeActivity.class.getSimpleName();

    JonopriyoApplication appInstance;
    JsonParser jsonParser;
    ProgressDialog pDialog;
    Long userId;

    Session mCurrentSession;

    Typeface tf;
//    BijoyFontUtil tfUtil;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;

    Button AllPolls, MyPolls, Feedback;
    TextView Title, NewPollHeader, LatestWinnerHeader;

    Poll latestPoll;
    ImageView LatestPollImage;
    TextView LatestPollTitle, LatestPollParticipation;
    Button LatestPollVoteNow;
    ProgressBar LatestPollLoading;

    RelativeLayout rlLastWinner;
    Winner lastWinner;
    ImageView LastWinnerPic;
    TextView LastWinnerName, LastWinnerAddress, LastWinnerPoll, WInnerTitle;
    ProgressBar LastWinnerLoading;

    ImageView ProfilePic;    
    ProgressBar ppLoding;
    //    RelativeLayout rlLoading;    

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    //    final String[] menuItems = {"About us"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(HomeActivity.this, "e8ecd3f1");
        setContentView(R.layout.home);   


        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();
        pDialog = new ProgressDialog(HomeActivity.this);

        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
//        tfUtil = new BijoyFontUtil();

        Log.d(">>>>????????", "first time logged in? =" + appInstance.isFirstTimeLoggedIn());

        userId = appInstance.getUserId();

        ProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        ppLoding = (ProgressBar) findViewById(R.id.pb_progress_pp);

        //        rlLoading = (RelativeLayout) findViewById(R.id.loading_Panel);

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
        
        rlLastWinner = (RelativeLayout) findViewById(R.id.rl_last_winner);
        rlLastWinner.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new RetrievePollById().execute(lastWinner.getPollId());
                
            }
        });

        changeStaticTextToBangla();
        registerGCM();                   


    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(HomeActivity.this);
    }


    private void changeStaticTextToBangla() {
        AllPolls = (Button) findViewById(R.id.b_all_polls);
        MyPolls = (Button) findViewById(R.id.b_my_polls);
        Feedback = (Button) findViewById(R.id.b_feedback);
        AllPolls.setTypeface(tf);
        MyPolls.setTypeface(tf);
        Feedback.setTypeface(tf);

        AllPolls.setText(getResources().getString(R.string.all_polls));
        MyPolls.setText(getResources().getString(R.string.my_polls));
        Feedback.setText(getResources().getString(R.string.feedback));

        Title = (TextView) findViewById(R.id.tv_title);
        NewPollHeader = (TextView) findViewById(R.id.tv_latest_poll_heading);
        LatestWinnerHeader = (TextView) findViewById(R.id.tv_last_winner_heading);
        Title.setTypeface(tf);
        NewPollHeader.setTypeface(tf);
        LatestWinnerHeader.setTypeface(tf);

        Title.setText(getResources().getString(R.string.jonopriyo));
        NewPollHeader.setText(getResources().getString(R.string.new_poll));
        LatestWinnerHeader.setText(getResources().getString(R.string.last_winner));

        WInnerTitle.setTypeface(tf);
        WInnerTitle.setText(getResources().getString(R.string.winner));

        LatestPollVoteNow.setTypeface(tf);
        LatestPollVoteNow.setText(getResources().getString(R.string.cast_vote));

        LatestPollParticipation.setTypeface(tf);
        //        LatestPollParticipation.setText(tfUtil.convertUnicode2BijoyString(""));

    }


    private void registerGCM(){
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
                // if the device is already registered in our server?? this flag becomes true when we get success response
                // while connecting to our server earlier.
                if (GCMRegistrar.isRegisteredOnServer(HomeActivity.this)) {
                 // Skips registration in the server             
                    Log.d(TAG, "Is already registered in server");
                    //                       Toast.makeText(getApplicationContext(), "Already registered with server", Toast.LENGTH_LONG).show();
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
                ppLoding.setVisibility(View.VISIBLE);
                Log.d("<<<<<>>>>", "rlLoading visible now");
                ImageLoader imageLoader = new ImageLoader(HomeActivity.this, ppLoding);
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

    public void onClickFeedback(View v){
        startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickWinners(View v){
        startActivity(new Intent(HomeActivity.this, WinnerListActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickProfile(View v){
        startActivity(new Intent(HomeActivity.this, ProfileNewActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickShare(View v){
        publishStory();
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
                LatestPollParticipation.setText(latestPoll.getParticipationCount() + " জন অংশগ্রহন করেছে");
                
                if(latestPoll.getIsCastByMe())
                    LatestPollVoteNow.setVisibility(View.GONE);

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
                LastWinnerPoll.setText(lastWinner.getPollQuestion());
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



    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(HomeActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("ঠিক আছে", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!success)
                    finish();

            }
        });
        bld.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Session activeSession = Session.getActiveSession();
        activeSession.onActivityResult(HomeActivity.this, requestCode, resultCode, data);
        //        Log.d(TAG, "requestCode " + requestCode);
        //        Log.d(TAG, "resultCode " + resultCode);
        //
        //        if (data != null) {
        //            Log.d(TAG, "data " + data.getDataString());
        //        }
        //        Log.d(TAG, "activeSession " + activeSession.getAccessToken());
        //        Log.d(TAG, "activeSession " + activeSession.getState());
        //
        Log.d(TAG, "IN onActivityResult");
        //        publishStory();
        if(mCurrentSession.isOpened()){
            Log.d(TAG, "mCurrentSession opened");

            pDialog.setMessage("Sharing the app in FB");
            pDialog.show();

            Bundle postParams = new Bundle();
            //            postParams.putString("name", "জনপ্রিয়");
            postParams.putString("name", "jonpPriyo");
            postParams.putString("caption", "Download & vote.");
            postParams.putString("description", "First polling app in BGD.");
            postParams.putString("link", "http://apps.priyo.com/jonopriyo/index.php");
            //            postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {                    
                    String postId = null;

                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    if(response != null){
                        Log.d(TAG, "response not null");
                        GraphObject graphObject = response.getGraphObject();
                        if(graphObject != null){
                            Log.d(TAG, "graphObject not null");
                            JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();

                            try {
                                alert("Successfully Posted to facebook.", true);
                                postId = graphResponse.getString("id");
                            } catch (JSONException e) {
                                Log.i(">>>>><<", "JSON error "+ e.getMessage());
                            }
                        }
                    }

                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Log.d(TAG, error.getErrorMessage());
                        Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, postId);
                    }
                }
            };

            Request request = new Request(mCurrentSession, "me/feed", postParams, 
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
    }

    private void publishStory() {

        final SessionTracker mSessionTracker = new SessionTracker(HomeActivity.this, new StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.d(">>>>>>>>>>", "inside StatusCallback");
            }
        }, null, false);

        final String applicationId = com.facebook.internal.Utility.getMetadataApplicationId(HomeActivity.this);
        mCurrentSession = mSessionTracker.getSession();

        if ((mCurrentSession == null) || mCurrentSession.getState().isClosed()) {
            Log.d("?????", "current session = null or closed");
            mSessionTracker.setSession(null);
            final Session session = new Session.Builder(getBaseContext()).setApplicationId(applicationId).build();
            Session.setActiveSession(session);
            mCurrentSession = session;
        }

        if (!mCurrentSession.isOpened()) {
            Log.d("??????????", "current session = not opened");
            Session.OpenRequest openRequest = null;
            openRequest = new Session.OpenRequest(HomeActivity.this);

            if (openRequest != null) {
                Log.d(">>>>>>>>", "openRequest not null");
                openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
                openRequest.setPermissions(Arrays.asList("publish_actions"));
                openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);


                mCurrentSession.openForPublish(openRequest);
            }
        }
        //        Session session = Session.getActiveSession();
        //        else{
        if (mCurrentSession != null){
            Log.d(TAG, "trying to restablish permission");
            // Check for publish permissions    
            //                        List<String> permissions = mCurrentSession.getPermissions();
            //                        if (!Utility.isSubsetOf(PERMISSIONS, permissions)) {
            //                            pendingPublishReauthorization = true;
            //                            Session.NewPermissionsRequest newPermissionsRequest = new Session
            //                                    .NewPermissionsRequest(this, PERMISSIONS);
            //                            mCurrentSession.requestNewPublishPermissions(newPermissionsRequest);
            //                            return;
            //                        }

            pDialog.setMessage("অপ্লিকেসনটি ফেসবুকে শেয়ার করা হচ্ছে");
            pDialog.show();

            Bundle postParams = new Bundle();
            postParams.putString("name", "jonoPriyo");
            postParams.putString("caption", "Download & vote.");
            postParams.putString("description", "First polling app in BGD.");
            postParams.putString("link", "http://apps.priyo.com/jonopriyo/index.php");
            //            postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    String postId = null;
                    if(pDialog.isShowing())
                        pDialog.dismiss();

                    if(response != null){
                        Log.d(TAG, "response not null");
                        GraphObject graphObject = response.getGraphObject();
                        if(graphObject != null){
                            Log.d(TAG, "graphObject not null");
                            JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();

                            try {
                                alert("Successfully Posted to facebook.", true);
                                postId = graphResponse.getString("id");
                            } catch (JSONException e) {
                                Log.i(">>>>><<", "JSON error "+ e.getMessage());
                            }
                        }
                    }

                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Log.d(TAG, error.getErrorMessage());
                        Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, postId);
                    }                    
                }
            };

            Request request = new Request(mCurrentSession, "me/feed", postParams, 
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }

    }
    
    
    private class RetrievePollById extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("একটু অপেক্ষা করুন...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_POLL_FROM_POLL_ID));
            urlParam.add(new BasicNameValuePair("poll_id", "" + params[0]));
            
            String token = appInstance.getAccessToken();

            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, token);
            if(response.getStatus() == 200){
                JSONObject jsonResponse = response.getjObj();
                try {
                    JSONObject pollObj = jsonResponse.getJSONObject("poll_data");
                    Poll winnersPoll = Poll.parsePoll(pollObj);
                    appInstance.setSelectedPoll(winnersPoll);
                    return true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } 
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){                
                Intent i = new Intent(HomeActivity.this, AllPollsDetailsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

}
