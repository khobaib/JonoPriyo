package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.adapter.NewPollAnswerListAdapter;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.PollAnswer;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class NewPollDetailsActivity extends Activity {

    private static final int VOTE_NOW = 1;
    private static final int CHECK_RESULT = 2;

    TextView tvPollQuestion, Title, ParticipationCount, PollCondition, ExpiryDate;    
    Button ResultOrVote;

    int flagResultOrVote;

    private ProgressDialog pDialog;
    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    Poll thisPoll;

    List<PollAnswer> pollAnswerList;
    ListView PollAnswerList;
    NewPollAnswerListAdapter pAnsListAdapter;
    
    Typeface tf;

    //    List<Integer> rbIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_poll_details);    

        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        jsonParser = new JsonParser();
        pDialog = new ProgressDialog(NewPollDetailsActivity.this);

        appInstance = (JonopriyoApplication) getApplication();
        thisPoll = appInstance.getSelectedPoll();
        
        getBanglaText();        
        
        TextView Participation = (TextView) findViewById(R.id.tv_participation_count_title);
        Participation.setTypeface(tf);
        Participation.setText(getResources().getString(R.string.participation));

        Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.poll) + " #" + thisPoll.getNumber());

        ParticipationCount = (TextView) findViewById(R.id.tv_participation_count);
        ParticipationCount.setTypeface(tf);
        ParticipationCount.setText(thisPoll.getParticipationCount() + "");
        
        ExpiryDate = (TextView) findViewById(R.id.tv_expiry_date); 
        ExpiryDate.setTypeface(tf);
        ExpiryDate.setText(Utility.parseDate(thisPoll.getExpiryDate()));

        ResultOrVote = (Button) findViewById(R.id.b_result_or_vote);
        ResultOrVote.setTypeface(tf);
        
        PollCondition = (TextView) findViewById(R.id.tv_poll_condition);
        PollCondition.setTypeface(tf);

        if(!thisPoll.getIsCastByMe()){
            PollCondition.setText(getResources().getString(R.string.poll_open));
            ResultOrVote.setText(getResources().getString(R.string.cast_vote));
//            PollCondition.setText("Poll is open -");
//            ResultOrVote.setText("Submit your vote");
            flagResultOrVote = VOTE_NOW;
        }
        else{
            PollCondition.setText(getResources().getString(R.string.already_voted));
            ResultOrVote.setText(getResources().getString(R.string.check_result));
//            PollCondition.setText("You already voted -");
//            ResultOrVote.setText("Check result"); 
            flagResultOrVote = CHECK_RESULT;
        }

        tvPollQuestion = (TextView) findViewById(R.id.tv_poll_question);
        tvPollQuestion.setText(thisPoll.getQuestion());

        PollAnswerList = (ListView) findViewById(R.id.lv_pollanswer_list);

        pollAnswerList = thisPoll.getAnswers();
        if(pollAnswerList == null || pollAnswerList.isEmpty()){
            PollAnswerList.setAdapter(null);
        }
        else{
            Log.d(">>>><<<<<", "poll answer count = " + pollAnswerList.size());
            PollAnswerList.setAdapter(new NewPollAnswerListAdapter(NewPollDetailsActivity.this, pollAnswerList));
        }       

    }


    private void getBanglaText() {
        TextView tvParticipation = (TextView) findViewById(R.id.tv_participation_count_title);
        tvParticipation.setTypeface(tf);
        tvParticipation.setText(getResources().getString(R.string.participation));
        
        TextView tvLastDate = (TextView) findViewById(R.id.tv_expiry_date_title);
        tvLastDate.setTypeface(tf);
        tvLastDate.setText(getResources().getString(R.string.last_vote_date));
        
    }


    public void onClickResultOrVote(View v){
        if(NewPollAnswerListAdapter.mSelectedPosition == -1){
            Toast.makeText(NewPollDetailsActivity.this, "আপনাকে একটি অপসন সিলেক্ট করতে হবে", Toast.LENGTH_SHORT).show();
        }
        else{
            if(flagResultOrVote == VOTE_NOW){
                Long qId = thisPoll.getId();
                Long ansId = thisPoll.getAnswers().get(NewPollAnswerListAdapter.mSelectedPosition).getId();
                new CastVote().execute(qId, ansId);
            }
            else{
                startActivity(new Intent(NewPollDetailsActivity.this, PollResultActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

        }
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


    public class CastVote extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("আপনার ভোট গৃহীত হচ্ছে, একটু অপেক্ষা করুন...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            Log.d("MARKER", "reached this point");
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_POST_USER_POLL));


            try {
                JSONObject pollObj = new JSONObject();
                pollObj.put("poll_question_id", params[0]);
                pollObj.put("poll_answer_id", params[1]);
                String pollData = pollObj.toString();
                Log.d("sending poll data", pollData);

                String token = appInstance.getAccessToken();

                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, pollData, token);
                if(response.getStatus() == 200){
                    Log.d(">>>><<<<", "success in casting vote");
                    JSONObject responsObj = response.getjObj();
                    String jsonResponse = responsObj.getString("response");
                    if(jsonResponse.equals("success"))
                        return true;
                }
                return false;
            } catch (JSONException e) {                
                e.printStackTrace();
                return false;
            }

        }


        @Override
        protected void onPostExecute(Boolean success) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(success){
                alert(getResources().getString(R.string.your_vote_is_cast), true);
            }
            else{
                alert(getResources().getString(R.string.you_voted_once), false);
            }

        }
    }


    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(NewPollDetailsActivity.this);

        bld.setMessage(message);
        bld.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(success){
                    startActivity(new Intent(NewPollDetailsActivity.this, PollResultActivity.class));
                    finish();
                }

            }
        });
        bld.create().show();
    }

}
