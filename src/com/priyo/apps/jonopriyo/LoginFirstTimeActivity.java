package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.db.JonopriyoDatabase;
import com.priyo.apps.jonopriyo.fragment.DatePickerFragment;
import com.priyo.apps.jonopriyo.model.Education;
import com.priyo.apps.jonopriyo.model.Profession;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class LoginFirstTimeActivity extends FragmentActivity implements OnDateSetListener {

    JonopriyoApplication appInstance;
    JsonParser jsonParser;
    ProgressDialog pDialog;
    Calendar calendar;

    Long educationId, professionId;
    String sex;

    List<Education> educationList;
    List<Profession> professionList;

    TextView DoB;
    String dobToStore;
    Spinner sProfession, sEducation, sSex;
    
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_first_time);   

        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();
        pDialog = new ProgressDialog(LoginFirstTimeActivity.this);
        calendar = Calendar.getInstance();
        
        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        
        TextView Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.profile_setup));
        
        TextView TitleDesc = (TextView) findViewById(R.id.tv_heading);
        TitleDesc.setTypeface(tf);
        TitleDesc.setText(getResources().getString(R.string.profile_setup_desc));
        
        TextView tvGender = (TextView) findViewById(R.id.tv_sex);
        tvGender.setTypeface(tf);
        tvGender.setText(getResources().getString(R.string.gender));
        
        TextView tvDoB = (TextView) findViewById(R.id.tv_dob_title);
        tvDoB.setTypeface(tf);
        tvDoB.setText(getResources().getString(R.string.dob));
        
        TextView tvProfession = (TextView) findViewById(R.id.tv_profession);
        tvProfession.setTypeface(tf);
        tvProfession.setText(getResources().getString(R.string.profession));
        
        TextView tvEducation = (TextView) findViewById(R.id.tv_education);
        tvEducation.setTypeface(tf);
        tvEducation.setText(getResources().getString(R.string.education));
        
        Button bUpdate = (Button) findViewById(R.id.b_update);
        bUpdate.setTypeface(tf);
        bUpdate.setText(getResources().getString(R.string.update));
        

        sProfession = (Spinner) findViewById(R.id.s_profession);
        sProfession.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                professionId = professionList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sEducation = (Spinner) findViewById(R.id.s_education);
        sEducation.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                educationId = educationList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sSex = (Spinner) findViewById(R.id.s_sex);
        generateSpinner(sSex, Utility.sex_array);
        sSex.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                sex = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        DoB = (TextView) findViewById(R.id.tv_dob);

        new RetrieveProfessionAndEducationList().execute();

        if(appInstance.isFirstTimeLoggedIn()){
            Log.d(">>>>><<<<<", "first time logged in.");
            // TO DO
            // isFirstTimeLoggedIn = true -> not only at first-time login but also if we delete data from settings,
            // so here we will check if the profile is updated or not. if not, need to retrieve data
            // for profession & education & store in DB
            // then we will show the alertDialog

        }

        //        userId = appInstance.getUserId();
    }


    private void showSpinnerData(){

        List<String> professionTypeList = new ArrayList<String>();
        Log.d(">>>>>>>>>", "profession list count = " + professionList.size());
        for(Profession profession : professionList)
            professionTypeList.add(profession.getType());
        String[] pfArray = professionTypeList.toArray(new String[0]);
        generateSpinner(sProfession, pfArray); 

        List<String> educationTypeList = new ArrayList<String>();
        Log.d(">>>>>>>>>", "educationList count = " + educationList.size());
        for(Education education : educationList)
            educationTypeList.add(education.getType());
        String[] edArray = educationTypeList.toArray(new String[0]);
        generateSpinner(sEducation, edArray);
        
        updateDatabase();
    }
    
    public void updateDatabase(){
        JonopriyoDatabase dbInstance = new JonopriyoDatabase(LoginFirstTimeActivity.this);
        dbInstance.open();
        for(Profession profession : professionList){
            dbInstance.insertOrUpdateProfession(profession);
        }
        for(Education education : educationList){
            dbInstance.insertOrUpdateEducation(education);
        }
        dbInstance.close();
    }


    public void onClickUpdate(View v){
        new FirstTimeLoginRequest().execute();
    }

    public void onClickCalendar(View v){
        DialogFragment newFragment = new DatePickerFragment().newInstance(calendar, "first_login");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dobToStore = String.format("%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
        DoB.setText(Utility.parseDate(dobToStore));
        calendar.set(year, monthOfYear, dayOfMonth);
    }


    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                LoginFirstTimeActivity.this, R.layout.my_simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);

    }


    private class FirstTimeLoginRequest extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("একটু অপেক্ষা করুন...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_REGISTRATION_AFTER_LOGIN));

            String token = appInstance.getAccessToken();

            try {
                JSONObject regObj = new JSONObject();
                regObj.put("dob", dobToStore);
                regObj.put("profession_id", "" + professionId);
                regObj.put("education_id", "" + educationId);
                regObj.put("sex", (sex.equals("male") ? "M" : "F"));

                String regData = regObj.toString();
                Log.d("<<>>", "req data = " + regData);
                ServerResponse serverResponse = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, regData, token);
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
                appInstance.setFirstTimeLoggedIn(false);
                alert(getResources().getString(R.string.your_profile_is_updated), true);
            }
            else{
                alert(getResources().getString(R.string.update_error), false);
            }
        }        
    }





    public class RetrieveProfessionAndEducationList extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("অ্যাপ্লিকেশন সক্রিয় হচ্ছে...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParamProf = new ArrayList<NameValuePair>();
            urlParamProf.add(new BasicNameValuePair("method", Constants.METHOD_GET_PROFESSION));

            ServerResponse responseProfessions = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParamProf, null, null);

            List<NameValuePair> urlParamEd = new ArrayList<NameValuePair>();
            urlParamEd.add(new BasicNameValuePair("method", Constants.METHOD_GET_EDUCATION));

            ServerResponse responseEducations = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParamEd, null, null);


            if(responseProfessions.getStatus() == 200 && responseEducations.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving profession & education list");
                professionList = Profession.parseprofessionList(responseProfessions.getjObj().toString());
                educationList = Education.parseEducationList(responseEducations.getjObj().toString());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                showSpinnerData();
            }
            else{
                finish();
            }
        }        
    }


    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(LoginFirstTimeActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("ঠিক আছে", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(success){
                    startActivity(new Intent(LoginFirstTimeActivity.this, HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }

            }
        });
        bld.create().show();
    }

}
