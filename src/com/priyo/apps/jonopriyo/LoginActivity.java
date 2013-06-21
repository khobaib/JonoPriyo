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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.model.Country;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // Progress Dialog
    private ProgressDialog pDialog;

    EditText Email, Password;
    CheckBox RememberMe;

    JonopriyoApplication appInstance;
    String email, password;
    String uid;

    JsonParser jsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        appInstance = (JonopriyoApplication) getApplication();
//        Boolean rememberMeFlag = appInstance.isRememberMe();
//        Log.d("loggine remember me", "" + rememberMeFlag);
//        if(rememberMeFlag){
//            email = appInstance.getEmail();
//            password = appInstance.getPassword();
//            new LoadCredentials().execute();
//        }
        
        jsonParser = new JsonParser();

        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);	

        RememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
        RememberMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    Log.d(TAG, "Remember Me checked");
                    appInstance.setRememberMe(true);
                }
                else{
                    Log.d(TAG, "Remember Me unchecked");
                    appInstance.setRememberMe(false);
                }               
            }
        });
    }

    public void onClickLogin(View v){
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();

        new LoadCredentials().execute();
    }

    public void onClickRegister(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }


    public class LoadCredentials extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Signing in, Please wait...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("MARKER", "reached this point");
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_LOGIN));

            try {
                JSONObject loginObj = new JSONObject();
                loginObj.put("email", email);
                loginObj.put("password", password);
                String loginData = loginObj.toString();

                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, loginData, null);
                if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                    Log.d(">>>><<<<", "success in login");
                    JSONObject responsObj = response.getjObj();
                    String login = responsObj.getString("login");
                    if(login.equals("success")){
                        String token = responsObj.getString("token");
                        String imageUrl = responsObj.getString("image_url");
                        appInstance.setAccessToken(token);
                        appInstance.setProfileImageUrl(imageUrl);
                        return true;
                    }
                    else{
                        return false;
                    }
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
                if(RememberMe.isChecked()){
                    appInstance.setRememberMe(true);
                    appInstance.setCredentials(email, password);
                }
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
            else{
                alert("Login error, please try again");
                Password.setText("");
//                Toast.makeText(LoginActivity.this, "Login error, please try again",  Toast.LENGTH_SHORT).show();
            }

        }



    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(LoginActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        //		Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

}
