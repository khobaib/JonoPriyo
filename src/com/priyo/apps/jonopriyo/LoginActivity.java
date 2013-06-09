package com.priyo.apps.jonopriyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.priyo.apps.jonopriyo.parser.JsonParser;
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

    JsonParser jsonParser = new JsonParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        appInstance = (JonopriyoApplication) getApplication();

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

        new LoadCredentials().execute(email, password);
    }


    public class LoadCredentials extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(LoginScreen.this);
//            pDialog.setMessage("Loading your user data. Please wait...");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(true);
//            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("MARKER", "reached this point");



            return false;
        }


        @Override
        protected void onPostExecute(Boolean success) {
            pDialog.dismiss();

        }



    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        //		Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

}
