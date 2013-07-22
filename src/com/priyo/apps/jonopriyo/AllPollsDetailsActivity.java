package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.adapter.AllPollsAnswerListAdapter;
import com.priyo.apps.jonopriyo.adapter.NewPollAnswerListAdapter;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.PollAnswer;
import com.priyo.apps.jonopriyo.model.PollResult;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.lazylist.ImageLoader;

public class AllPollsDetailsActivity extends Activity{
    
    private static final int VOTE_NOW = 1;
    private static final int CHECK_RESULT = 2;
    
    TextView tvPollQuestion, Title, ParticipationCount, PollCondition;
    RelativeLayout rlWinner;
    TextView WinnerName, WinnerAddress;
    ImageView WinnerPic;
    Button ResultOrVote;
    
    int flagResultOrVote;

//    private ProgressDialog pDialog;
    JsonParser jsonParser;
    JonopriyoApplication appInstance;
    ImageLoader imageLoader;

    Poll thisPoll;
    Winner thisWinner;
//    int fromActivity;  
    
    List<PollAnswer> pollAnswerList;
    ListView PollAnswerList;
    NewPollAnswerListAdapter pAnsListAdapter;
    
    List<PollResult> pollResultList;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_polls_details);     
        
//        fromActivity = getIntent().getExtras().getInt(Constants.FROM_ACTIVITY);

        appInstance = (JonopriyoApplication) getApplication();
        thisPoll = appInstance.getSelectedPoll();
        thisWinner = null;
        
        imageLoader = new ImageLoader(AllPollsDetailsActivity.this);
        
        Title = (TextView) findViewById(R.id.tv_title);
        Title.setText("Poll #" + thisPoll.getNumber());
        
        ParticipationCount =  (TextView) findViewById(R.id.tv_participation_count);
        ParticipationCount.setText("" + thisPoll.getParticipationCount());
        
        ResultOrVote = (Button) findViewById(R.id.b_result_or_vote);
        PollCondition = (TextView) findViewById(R.id.tv_poll_condition);
        
        if(thisPoll.getIsNew()){
            if(!thisPoll.getIsCastByMe()){
                PollCondition.setText("Poll is open -");
                ResultOrVote.setText("Vote Now");
                flagResultOrVote = VOTE_NOW;
            }
            else{
                PollCondition.setText("You already voted -");
                ResultOrVote.setText("Check result"); 
                flagResultOrVote = CHECK_RESULT;
            }
        }
        else{
            PollCondition.setText("Poll is closed - ");
            ResultOrVote.setText("Check result");
            flagResultOrVote = CHECK_RESULT;
        }

        /*
         * initially winner wont be displayed.
         * we will display winners pic, name & address when we confirm that it's closed poll
         * & there's some winner for this closed poll
         */
        rlWinner = (RelativeLayout) findViewById(R.id.rl_winner);
        rlWinner.setVisibility(View.GONE);
        
        WinnerName = (TextView) findViewById(R.id.tv_winner_name);
        WinnerAddress = (TextView) findViewById(R.id.tv_winner_address);
        WinnerPic = (ImageView) findViewById(R.id.iv_winner_pic);
        

        
        if(!thisPoll.getIsNew())
            new RetrieveWinner().execute();
        
        jsonParser = new JsonParser();
//        pDialog = new ProgressDialog(AllPollsDetailsActivity.this);

        tvPollQuestion = (TextView) findViewById(R.id.tv_poll_question);       
        tvPollQuestion.setText(thisPoll.getQuestion());
        
        PollAnswerList = (ListView) findViewById(R.id.lv_pollanswer_list);
        
        pollAnswerList = thisPoll.getAnswers();
        if(pollAnswerList == null || pollAnswerList.isEmpty()){
            PollAnswerList.setAdapter(null);
        }
        else{
            Log.d(">>>><<<<<", "poll answer count = " + pollAnswerList.size());
            PollAnswerList.setAdapter(new AllPollsAnswerListAdapter(AllPollsDetailsActivity.this, pollAnswerList));
        }
    }
    
    
    public void onClickResultOrVote(View v){
        if(flagResultOrVote == VOTE_NOW){
            startActivity(new Intent(AllPollsDetailsActivity.this, NewPollDetailsActivity.class));
        }
        else{
            startActivity(new Intent(AllPollsDetailsActivity.this, PollResultActivity.class));
        }
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
       
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            finish();
            overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    private class RetrieveWinner extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_WINNER_FROM_POLL_ID));
            urlParam.add(new BasicNameValuePair("poll_id", "" + thisPoll.getId()));

            String token = appInstance.getAccessToken();
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, token);
            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonResponse = response.getjObj();
                try {
                    String responseType = jsonResponse.getString("response");
                    if(responseType.equals("success")){
                        JSONObject winnerObj = jsonResponse.getJSONObject("winner");
                        thisWinner = Winner.parseWinner(winnerObj);
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } 
            
            return false;
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                // a winner is found
                rlWinner.setVisibility(View.VISIBLE);
                WinnerName.setText(thisWinner.getWinnerName());
                WinnerAddress.setText(thisWinner.getAddress());
                imageLoader.DisplayImage(thisWinner.getWinnerPicUrl(), WinnerPic);
            }
        }
        
    }
  
    
    
//    void alert(String message, final Boolean success) {
//        AlertDialog.Builder bld = new AlertDialog.Builder(PastPollDetailsActivity.this);
//        bld.setMessage(message);
//        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//            
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(success){
//                    startActivity(new Intent(PastPollDetailsActivity.this, PollResultActivity.class));
//                    finish();
//                }
//                
//            }
//        });
//        bld.create().show();
//    }

}
