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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.adapter.PollAnswerListAdapter;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.PollAnswer;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

public class PollDetailsActivity extends Activity {
    
    TextView tvPollQuestion;
//    RadioGroup rgAnswerOption;
    
    Button Submit;
    
    TextView Title;
    
//    static int selectedButtonIndex;

    private ProgressDialog pDialog;
    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    Poll thisPoll;
    
    List<PollAnswer> pollAnswerList;
    ListView PollAnswerList;
    PollAnswerListAdapter pAnsListAdapter;
    
//    List<Integer> rbIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_details);    
        
        jsonParser = new JsonParser();

        appInstance = (JonopriyoApplication) getApplication();
        thisPoll = appInstance.getSelectedPoll();
        
        Title = (TextView) findViewById(R.id.tv_title);
        Title.setText("Poll #" + thisPoll.getNumber());
        
        tvPollQuestion = (TextView) findViewById(R.id.tv_poll_question);
        tvPollQuestion.setText(thisPoll.getQuestion());
        
        PollAnswerList = (ListView) findViewById(R.id.lv_pollanswer_list);
        
        pollAnswerList = thisPoll.getAnswers();
        if(pollAnswerList == null || pollAnswerList.isEmpty()){
            PollAnswerList.setAdapter(null);
        }
        else{
            Log.d(">>>><<<<<", "poll answer count = " + pollAnswerList.size());
            PollAnswerList.setAdapter(new PollAnswerListAdapter(PollDetailsActivity.this, pollAnswerList));
        }
        
        Submit = (Button) findViewById(R.id.b_submit);
       
    }
    
    
    public void onClickSubmit(View v){
        if(PollAnswerListAdapter.mSelectedPosition == -1){
            Toast.makeText(PollDetailsActivity.this, "Please cast your vote first.", Toast.LENGTH_SHORT).show();
        }
        else{
            Long qId = thisPoll.getId();
            Long ansId = thisPoll.getAnswers().get(PollAnswerListAdapter.mSelectedPosition).getId();
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
            pDialog.dismiss();
            if(success){
                alert("Your vote is cast successfully.", true);
            }
            else{
                alert("You already voted once.", false);
            }

        }
    }

    
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
