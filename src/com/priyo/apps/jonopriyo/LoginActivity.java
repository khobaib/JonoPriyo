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
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.model.RegistrationInfo;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    ProgressDialog pDialog;
    JonopriyoApplication appInstance;
    JsonParser jsonParser;

    EditText Email, Password;
    CheckBox RememberMe;
    TextView ForgetPassword;


    String email, password;
    String uid;

    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pDialog = new ProgressDialog(LoginActivity.this);
        jsonParser = new JsonParser();
        appInstance = (JonopriyoApplication) getApplication();
        //        Boolean rememberMeFlag = appInstance.isRememberMe();
        //        Log.d("loggine remember me", "" + rememberMeFlag);
        //        if(rememberMeFlag){
        //            email = appInstance.getEmail();
        //            password = appInstance.getPassword();
        //            new LoadCredentials().execute();
        //        }

        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");

        TextView AppName = (TextView) findViewById(R.id.tv_app_name);
        AppName.setTypeface(tf);
        AppName.setText(getResources().getString(R.string.jonopriyo));

        TextView ForgetPass = (TextView) findViewById(R.id.tv_forget_pass);
        ForgetPass.setTypeface(tf);
        ForgetPass.setText(getResources().getString(R.string.forget_password2));

        TextView NewUser = (TextView) findViewById(R.id.tv_no_acc);
        NewUser.setTypeface(tf);
        NewUser.setText(getResources().getString(R.string.new_user));

        Button Login = (Button) findViewById(R.id.b_login);
        Login.setTypeface(tf);
        Login.setText(getResources().getString(R.string.login));

        Button Register = (Button) findViewById(R.id.b_register);
        Register.setTypeface(tf);
        Register.setText(getResources().getString(R.string.registration));

        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);

        Email.setTypeface(tf);
        Password.setTypeface(tf);

        Email.setHint(getResources().getString(R.string.email));
        Password.setHint(getResources().getString(R.string.password));

        Utility.HandlingHintsInEditText(LoginActivity.this, Email, getResources().getString(R.string.email));
        Utility.HandlingHintsInEditText(LoginActivity.this, Password, getResources().getString(R.string.password));


        ForgetPassword = (TextView) findViewById(R.id.tv_click_here);
        ForgetPassword.setTypeface(tf);
        ForgetPassword.setText(getResources().getString(R.string.click_here));
        ForgetPassword.setPaintFlags(ForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ForgetPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickForgetPassword();

            }
        });

        RememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
        RememberMe.setTypeface(tf);
        RememberMe.setText(getResources().getString(R.string.remember_me));
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
            Toast.makeText(LoginActivity.this, "দয়া করে সঠিক ইমেইল এড্রেস প্রদান করুন", Toast.LENGTH_SHORT).show();
        else if(password == null || password.equals(""))
            Toast.makeText(LoginActivity.this, "সঠিক পাসওয়ার্ড প্রদান করুন", Toast.LENGTH_SHORT).show();
        else{
            new LoadCredentials().execute();
        }
    }

    public void onClickRegister(View v){
        startActivity(new Intent(LoginActivity.this, RegistrationNewActivity.class));
    }

    public void onClickForgetPassword(){

        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View textEntryView = inflater.inflate(R.layout.dialog_forget_password, null);
        final AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
        alert.setView(textEntryView, 0, 0, 0, 0);

        final EditText EmailAddress = (EditText) textEntryView.findViewById(R.id.et_email);

        TextView Title = (TextView) textEntryView.findViewById(R.id.tv_title);
        TextView Message = (TextView) textEntryView.findViewById(R.id.tv_message);

        Title.setTypeface(tf);
        Message.setTypeface(tf);

        Title.setText(getResources().getString(R.string.reset_pass));
        Message.setText(getResources().getString(R.string.enter_email_address));


        Button OK = (Button) textEntryView.findViewById(R.id.b_ok);
        OK.setTypeface(tf);
        OK.setText(getResources().getString(R.string.ok));
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
            pDialog.setMessage("একটু অপেক্ষা করুন...");
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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result.equals("success")){
                alert("আপনার ইমেইল এড্রেস এ পাসওয়ার্ড পাঠানো হয়েছে");
            }
            else if(result.equals("email doesn't exist")){
                alert("এটি বৈধ ইমেইল নয়");
            }
            else{
                alert("আপনার রিকুয়েস্ট টি সঠিক ভাবে সম্পন্ন হয়নি");
            }

        }

    }






    public class LoadCredentials extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("একটু অপেক্ষা করুন...");
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
                        Long userId = responsObj.getLong("user_id");
                        appInstance.setAccessToken(token);
                        appInstance.setProfileImageUrl(imageUrl);
                        appInstance.setUserId(userId);
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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(success){
                if(RememberMe.isChecked()){
                    appInstance.setRememberMe(true);
                    appInstance.setCredentials(email, password);
                }
                if(appInstance.isFirstTimeLoggedIn()){
                    runOnUiThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            new GetProfileData().execute();
                            
                        }
                    });

//                    startActivity(new Intent(LoginActivity.this, LoginFirstTimeActivity.class));
                }
                else{
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
            else{
                alert("লগইন এ সমস্যা দেখা দিয়েছে, আবার চেস্টা করুন");
                Password.setText("");
                //                Toast.makeText(LoginActivity.this, "Login error, please try again",  Toast.LENGTH_SHORT).show();
            }

        }
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(LoginActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("ঠিক আছে", null);
        //		Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }






    private class GetProfileData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if(!pDialog.isShowing())
//            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_PROFILE_INFO));

            String token = appInstance.getAccessToken();

            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, token);
            if(response.getStatus() == 200){
                JSONObject jsonResponse = response.getjObj();
                try {
                    String responseType = jsonResponse.getString("response");
                    if(responseType.equals("success")){
                        Log.d(">>>><<<<", "success in retrieving reg info");
                        JSONObject regObj = jsonResponse.getJSONObject("profile_info");
                        RegistrationInfo regInfo = RegistrationInfo.parseRegInfo(regObj.toString());
                        if(!regInfo.getDob().equals("0000-00-00"))
                            return true;
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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
                appInstance.setFirstTimeLoggedIn(false);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
            else
                startActivity(new Intent(LoginActivity.this, LoginFirstTimeActivity.class));


        }

    }   






}
