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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.Utility;

public class RegistrationNewActivity extends Activity {

    EditText Name, Email, Password, ConfirmPassword, Phone;
    String name, email, password, confirmPass, phone;

    Spinner sPhonePrefix;
    String selectedPhonePrefix;

    JsonParser jsonParser;
    ProgressDialog pDialog;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new);

        jsonParser = new JsonParser();        
        pDialog = new ProgressDialog(RegistrationNewActivity.this);

        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");

        Name = (EditText) findViewById(R.id.et_name);
        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);
        ConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        Phone = (EditText) findViewById(R.id.et_phone);       

        Name.setTypeface(tf);
        Email.setTypeface(tf);
        Password.setTypeface(tf);
        ConfirmPassword.setTypeface(tf);
        Phone.setTypeface(tf);

        Name.setHint(getResources().getString(R.string.name));
        Email.setHint(getResources().getString(R.string.email));
        Password.setHint(getResources().getString(R.string.password));
        ConfirmPassword.setHint(getResources().getString(R.string.confirm_pass));
        Phone.setHint(getResources().getString(R.string.phone));
        
        Utility.HandlingHintsInEditText(RegistrationNewActivity.this, Name, getResources().getString(R.string.name));
        Utility.HandlingHintsInEditText(RegistrationNewActivity.this, Email, getResources().getString(R.string.email));
        Utility.HandlingHintsInEditText(RegistrationNewActivity.this, Password, getResources().getString(R.string.password));
        Utility.HandlingHintsInEditText(RegistrationNewActivity.this, ConfirmPassword, getResources().getString(R.string.confirm_pass));
        Utility.HandlingHintsInEditText(RegistrationNewActivity.this, Phone, getResources().getString(R.string.phone));

        
        TextView Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.registration));
        
        Button Register = (Button) findViewById(R.id.b_register);
        Register.setTypeface(tf);
        Register.setText(getResources().getString(R.string.register));
        
        
        sPhonePrefix = (Spinner) findViewById(R.id.s_phone_prefix);
        generateSpinner(sPhonePrefix, Utility.PHONE_NUMBER_PREFIX);
        sPhonePrefix.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selectedPhonePrefix = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }




//    private void HandlingHintsInEditText(RegistrationNewActivity registrationNewActivity, EditText email2, String string) {
//        // TODO Auto-generated method stub
//        
//    }




    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                RegistrationNewActivity.this, R.layout.my_simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);

    }

    public void onClickRegister(View v){
        name = Name.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        confirmPass = ConfirmPassword.getText().toString();
        phone = Phone.getText().toString();

        if(name == null || name.equals("")){
            Toast.makeText(RegistrationNewActivity.this, "Please insert your name.", Toast.LENGTH_SHORT).show();
        }
        else if(email == null || email.equals("")){
            Toast.makeText(RegistrationNewActivity.this, "Please insert your email.", Toast.LENGTH_SHORT).show();
        }
        else if(password == null || password.equals("")){
            Toast.makeText(RegistrationNewActivity.this, "Please select your password.", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPass)){
            Toast.makeText(RegistrationNewActivity.this, "Password mismatch.", Toast.LENGTH_SHORT).show();
        }

        else{

            new SendRegisterRequest().execute();
        }
    }


    public class SendRegisterRequest extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait while app is registering you to the system...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_REGISTRATION_BASIC));


            try {
                JSONObject regObj = new JSONObject();
                regObj.put("name", name);
                regObj.put("email", email);
                regObj.put("password", password);
                regObj.put("phone", selectedPhonePrefix + phone);

                String regData = regObj.toString();
                Log.d("<<>>", "req data = " + regData);
                ServerResponse serverResponse = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, regData, null);
                if(serverResponse.getStatus() == 200){
                    JSONObject responseObj = serverResponse.getjObj();
                    String response = responseObj.getString("response");
                    if(response.equals("success"))
                        return true;
                    return false;
                }
                else{
                    return false;
                }
            } catch (JSONException e) {                
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                alert("Registration Successful.", true);
            }
            else{
                alert("Email already exist, please try again.", false);
            }
        }        
    }

    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(RegistrationNewActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(success)
                    finish();

            }
        });
        bld.create().show();
    }

}
