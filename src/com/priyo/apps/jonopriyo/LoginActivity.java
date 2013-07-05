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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView ForgetPassword;

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
        
        ForgetPassword = (TextView) findViewById(R.id.tv_click_here);
        ForgetPassword.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                onClickForgetPassword();
                
            }
        });

        RememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
        RememberMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    Log.d(TAG, "Remember Me checked");
//                    appInstance.setRememberMe(true);
                }
                else{
                    Log.d(TAG, "Remember Me unchecked");
//                    appInstance.setRememberMe(false);
                }               
            }
        });
    }

    public void onClickLogin(View v){
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        
        if(email == null || email.equals(""))
            Toast.makeText(LoginActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
        else if(password == null || password.equals(""))
            Toast.makeText(LoginActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
        else{
            new LoadCredentials().execute();
        }
    }

    public void onClickRegister(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void onClickForgetPassword(){
                
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View textEntryView = inflater.inflate(R.layout.dialog_forget_password, null);
        final AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
        alert.setView(textEntryView, 0, 0, 0, 0);
                
        final EditText EmailAddress = (EditText) textEntryView.findViewById(R.id.et_email);
        
        
        Button OK = (Button) textEntryView.findViewById(R.id.b_ok);
        OK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = EmailAddress.getText().toString();
                alert.dismiss(); 
                new SendForgetPassRequest().execute(email);
            }

        });
        
        alert.show();
    }
    
    
    public class SendForgetPassRequest extends AsyncTask<String, Void, String> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Request forgetting password...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_FORGET_PASSWORD));
            
            try {
                JSONObject emailObj = new JSONObject();
                emailObj.put("email", params[0]);
                String emailData = emailObj.toString();

                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, emailData, null);
                if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                    Log.d(">>>><<<<", "RESPONSE_STATUS_CODE_SUCCESS");
                    JSONObject responsObj = response.getjObj();
                    String login = responsObj.getString("response");
                    return login;
                }
                return "false";
            } catch (JSONException e) {                
                e.printStackTrace();
                return "false";
            }
        }
        
        
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if(result.equals("success")){
                alert("Your password is sent to your email adderess.");
            }
            else if(result.equals("email doesn't exist")){
                alert("This email address doesn't exist.");
            }
            else{
                alert("Request Failure.");
            }

        }
        
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
                        if(RememberMe.isChecked())
                            appInstance.setRememberMe(true);
                        else
                            appInstance.setRememberMe(false);
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
