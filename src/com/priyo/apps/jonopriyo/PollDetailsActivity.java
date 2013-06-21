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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

public class PollDetailsActivity extends Activity {
    
    TextView tvPollQuestion;
    RadioGroup rgAnswerOption;
    
    Button Submit;
    
    TextView Title;
    
    int selectedButtonIndex;

    // Progress Dialog
    private ProgressDialog pDialog;
    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    Poll thisPoll;
    int fromActivity;
    
    List<Integer> rbIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_details);     
        
        fromActivity = getIntent().getExtras().getInt(Constants.FROM_ACTIVITY);

        appInstance = (JonopriyoApplication) getApplication();
        thisPoll = appInstance.getSelectedPoll();
        
        Title = (TextView) findViewById(R.id.tv_poll_title);
//        String pollNum = "Poll #" + thisPoll.getNumber();
        Title.setText("Poll #" + thisPoll.getNumber());
        
        Submit = (Button) findViewById(R.id.b_submit);
        if(fromActivity != Constants.PARENT_ACTIVITY_NEW_POLLS)
            Submit.setVisibility(View.GONE);
        
        jsonParser = new JsonParser();
        
        
        
        selectedButtonIndex = -1;

        tvPollQuestion = (TextView) findViewById(R.id.tv_poll_question);
        rgAnswerOption = (RadioGroup) findViewById(R.id.rg_poll_answer);
        rgAnswerOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedButtonIndex = getRadioButtonIndex(checkedId);
//                Toast.makeText(PollDetailActivity.this, "checked index = " + selectedButtonIndex, Toast.LENGTH_SHORT).show();
            }
        });
        
        tvPollQuestion.setText(thisPoll.getQuestion());
        rgAnswerOption.removeAllViews();
        Log.d(">>>>>>??????", "rgAnswerOption length = " + rgAnswerOption.getChildCount());
        int ansCount = thisPoll.getAnswers().size();
        rbIdList = new ArrayList<Integer>();
        final RadioButton[] rb = new RadioButton[ansCount];
        for(int buttonIndex = 0; buttonIndex < ansCount; buttonIndex++){
            rb[buttonIndex] = new RadioButton(PollDetailsActivity.this);
            if(fromActivity == Constants.PARENT_ACTIVITY_ALL_POLLS){
                Log.d(">>>>", "ALL POLLS");
//                rb[buttonIndex].setClickable(false);
                rb[buttonIndex].setEnabled(false);
                rb[buttonIndex].setTextColor(getResources().getColor(R.color.gray_snow2));
            }
            Log.d("ID ID ID", "rb -> id = " + rb[buttonIndex].getId());
            rgAnswerOption.addView(rb[buttonIndex]);        //the RadioButtons are added to the radioGroup instead of the layout
            Log.d("--- ID ID ID", "rb -> id = " + rgAnswerOption.getChildAt(buttonIndex).getId());
//            rbIdList.set(buttonIndex, rgAnswerOption.getChildAt(buttonIndex).getId());
            rbIdList.add(rgAnswerOption.getChildAt(buttonIndex).getId());
            rb[buttonIndex].setText(thisPoll.getAnswers().get(buttonIndex).getAnswer());
        }        


    }
    
    
    private int getRadioButtonIndex(int checkedId){
        for(int i = 0; i < rbIdList.size(); i++){
            if(rbIdList.get(i) == checkedId){
                return i;
            }
        }
        return -1;   
    }
    
    public void onClickSubmit(View v){
        if(selectedButtonIndex == -1){
            Toast.makeText(PollDetailsActivity.this, "Please cast your vote first.", Toast.LENGTH_SHORT).show();
        }
        else{
            Long qId = thisPoll.getId();
            Long ansId = thisPoll.getAnswers().get(selectedButtonIndex).getId();
            new CastVote().execute(qId, ansId);
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
            pDialog = new ProgressDialog(PollDetailsActivity.this);
            pDialog.setMessage("Casting your vote, Please wait...");
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
//                pollObj.put("user_id", 5);              // hardcoded, no need this field.
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
            pDialog.dismiss();
            if(success){
                alert("Your vote is cast successfully.", true);
            }
            else{
                alert("Sorry try again.", false);
            }

        }
    }


//    public class RetrievePollData extends AsyncTask<String, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(PollDetailsActivity.this);
//            pDialog.setMessage("Retrieving poll data, Please wait...");
//            pDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//            Log.d("MARKER", "reached this point");
//            String rootUrl = Constants.URL_ROOT;
//
//            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
//            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_NEW_POLLS));
//
//            String token = appInstance.getAccessToken();
//
//            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
//                    urlParam, null, token);
//            if(response.getStatus() == 200){
//                Log.d(">>>><<<<", "success in login");
//                JSONObject responsObj = response.getjObj();
//                thisPoll = Poll.parsePoll(responsObj.toString());
//                return true;
//                //                String login = responsObj.getString("login");
//            }
//
//            return false;
//
//        }
//
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            pDialog.dismiss();
//            if(success){
//                tvPollQuestion.setText(thisPoll.getQuestion());
//                rgAnswerOption.removeAllViews();
//                Log.d(">>>>>>??????", "rgAnswerOption length = " + rgAnswerOption.getChildCount());
//                int ansCount = thisPoll.getAnswers().size();
//                rbIdList = new ArrayList<Integer>();
//                final RadioButton[] rb = new RadioButton[ansCount];
//                for(int buttonIndex = 0; buttonIndex < ansCount; buttonIndex++){
//                    rb[buttonIndex] = new RadioButton(PollDetailsActivity.this);
//                    Log.d("ID ID ID", "rb -> id = " + rb[buttonIndex].getId());
//                    rgAnswerOption.addView(rb[buttonIndex]);        //the RadioButtons are added to the radioGroup instead of the layout
//                    Log.d("--- ID ID ID", "rb -> id = " + rgAnswerOption.getChildAt(buttonIndex).getId());
////                    rbIdList.set(buttonIndex, rgAnswerOption.getChildAt(buttonIndex).getId());
//                    rbIdList.add(rgAnswerOption.getChildAt(buttonIndex).getId());
//                    rb[buttonIndex].setText(thisPoll.getAnswers().get(buttonIndex).getAnswer());
//                }
//            }
//            else{
//                //                Toast.makeText(LoginActivity.this, "Login error, please try again",  Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//
//
//    }
//    
    
    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(PollDetailsActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(success){
                    startActivity(new Intent(PollDetailsActivity.this, PollResultActivity.class));
                    finish();
                }
                
            }
        });
        bld.create().show();
    }

}
