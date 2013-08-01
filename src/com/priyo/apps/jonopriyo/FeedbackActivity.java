package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

public class FeedbackActivity extends Activity {

    ProgressDialog pDialog;
    JonopriyoApplication appInstance;
    JsonParser jsonParser;

    EditText etFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        pDialog = new ProgressDialog(FeedbackActivity.this);
        jsonParser = new JsonParser();
        appInstance = (JonopriyoApplication) getApplication();

        etFeedback = (EditText) findViewById(R.id.et_feedback);
    }

    public void onClickSubmit(View v){
        String feedback = etFeedback.getText().toString().trim();
        if(feedback == null || feedback.equals("")){
            Toast.makeText(FeedbackActivity.this, "Feedback text is empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            new UploadFeedback().execute(feedback);
        }
    }



    public class UploadFeedback extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("MARKER", "reached this point");
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_POST_FEEDBACK));

            try {
                JSONObject feedbackObj = new JSONObject();
                feedbackObj.put("feedback", params[0]);
                String feedbackData = feedbackObj.toString();

                String token = appInstance.getAccessToken();

                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, feedbackData, token);
                if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                    Log.d(">>>><<<<", "success in login");
                    JSONObject responsObj = response.getjObj();
                    String result = responsObj.getString("response");
                    if(result.equals("success"))
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
                alert("Successfully posted feedback", true);
            }
            else{
                alert("Failed to post feedback. Please try again", false);
            }

        }

    }

    void alert(String message, final boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(FeedbackActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(success){
                    finish();
                    overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
                }

            }
        });
        bld.create().show();
    }

}
