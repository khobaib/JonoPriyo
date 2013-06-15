package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.priyo.apps.jonopriyo.LoginActivity.LoadCredentials;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class SplashActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    JsonParser jsonParser;

    JonopriyoApplication appInstance;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();

        Boolean rememberMeFlag = appInstance.isRememberMe();
        Log.d("loggine remember me", "" + rememberMeFlag);
        if(rememberMeFlag){
            email = appInstance.getEmail();
            password = appInstance.getPassword();
            new LoadCredentials().execute();
        } 

        else{
            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(2500);                    
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }finally{

                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
            timer.start();
        }


    }

    public class LoadCredentials extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SplashActivity.this);
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
                if(response.getStatus() == 200){
                    Log.d(">>>><<<<", "success in login");
                    JSONObject responsObj = response.getjObj();
                    String login = responsObj.getString("login");
                    if(login.equals("success")){
                        String token = responsObj.getString("token");
                        appInstance.setAccessToken(token);
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
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
            else{
                alert("Login error, please try again");                

                //                Toast.makeText(LoginActivity.this, "Login error, please try again",  Toast.LENGTH_SHORT).show();
            }

        }



    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(SplashActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();

            }
        });
        bld.create().show();
    }

}
