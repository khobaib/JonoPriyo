package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.fragment.DatePickerFragment;
import com.priyo.apps.jonopriyo.model.Area;
import com.priyo.apps.jonopriyo.model.City;
import com.priyo.apps.jonopriyo.model.Country;
import com.priyo.apps.jonopriyo.model.Education;
import com.priyo.apps.jonopriyo.model.Profession;
import com.priyo.apps.jonopriyo.model.RegistrationInfo;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.Utility;

public class RegisterActivity extends FragmentActivity implements OnDateSetListener {

    EditText Name, Email, Password, ConfirmPassword, Address, Phone;
    TextView DoB;
    Spinner sProfession, sEducation, sSex;
    AutoCompleteTextView tvCountry, tvCity, tvArea;
    
    RegistrationInfo regInfo;
    
    List<Country> countryList;
    List<City> cityList;
    List<Area> areaList;
    List<Education> educationList;
    List<Profession> professionList;
    
    Long selectedCountryId, selectedCityId, selectedAreaId;    
    Long educationId, professionId;
    String sex;
    
    JsonParser jsonParser;

    public ProgressDialog pDialog;

    private static final int DATE_PICKER = 1;

//    public String selectedDate;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        jsonParser = new JsonParser();
        selectedCountryId = (long) 0;
        selectedCityId = (long) 0;
        selectedAreaId = (long) 0;

        calendar = Calendar.getInstance();

        Name = (EditText) findViewById(R.id.et_name);
        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);
        ConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        Address = (EditText) findViewById(R.id.et_address);
        Phone = (EditText) findViewById(R.id.et_phone);

        DoB = (TextView) findViewById(R.id.tv_dob);
        DoB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment().newInstance(calendar);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

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
//        generateSpinner(sProfession, Utility.profession_array);

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
        //        generateSpinner(Education, Utility.education_array);

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
        

        tvCountry = (AutoCompleteTextView) findViewById(R.id.et_country);
        tvCountry.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                selectedCountryId = (long) 0;
                for(Country country : countryList){
                    if(selection.equals(country.getName())){
                        selectedCountryId = country.getId();
                        break;
                    }
                }
                new RetrieveCityList().execute(selectedCountryId);

            }
        });


        tvCity = (AutoCompleteTextView) findViewById(R.id.et_city);
        tvCity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                selectedCityId = (long) 0;
                for(City city : cityList){
                    if(selection.equals(city.getName())){
                        selectedCityId = city.getId();
                        break;
                    }
                }
                new RetrieveAreaList().execute(selectedCityId);

            }
        });

        tvArea = (AutoCompleteTextView) findViewById(R.id.et_area);
        tvArea.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                selectedAreaId = (long) 0;
                for(Area area : areaList){
                    if(selection.equals(area.getName())){
                        selectedAreaId = area.getId();
                        break;
                    }
                }

            }
        });

        new RetrieveCountryList().execute();
        new RetrieveProfessionList().execute();
        new RetrieveEducationList().execute();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DoB.setText(String.format("%02d-%02d-%04d", dayOfMonth, (monthOfYear + 1), year));
        calendar.set(year, monthOfYear, dayOfMonth);
//        selectedDate = DoB.getText().toString();
    }


    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                RegisterActivity.this, android.R.layout.simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    
    public void onClickRegister(View v){
        regInfo = new RegistrationInfo();
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmPass = ConfirmPassword.getText().toString();
        String selectedDate = DoB.getText().toString();
        
        if(name == null || name.equals("")){
            Toast.makeText(RegisterActivity.this, "Please insert your name.", Toast.LENGTH_SHORT).show();
        }
        else if(email == null || email.equals("")){
            Toast.makeText(RegisterActivity.this, "Please insert your email.", Toast.LENGTH_SHORT).show();
        }
        else if(password == null || password.equals("")){
            Toast.makeText(RegisterActivity.this, "Please select your password.", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPass)){
            Toast.makeText(RegisterActivity.this, "Password mismatch.", Toast.LENGTH_SHORT).show();
        }
        else if(selectedDate == null || selectedDate.equals("")){
            Toast.makeText(RegisterActivity.this, "Please select your date of birth.", Toast.LENGTH_SHORT).show();
        }
        else{
            regInfo.setName(name);
            regInfo.setEmail(email);
            regInfo.setPassword(password);
            regInfo.setDob(selectedDate);
            regInfo.setProfessionId(professionId);
            regInfo.setEducationId(educationId);
            regInfo.setSex((sex.equals("male") ? "M" : "F"));
            regInfo.setCountryId(selectedCountryId);
            regInfo.setCityId(selectedCityId);
            regInfo.setAreaId(selectedAreaId);
            regInfo.setAddress(Address.getText().toString());
            regInfo.setPhone(Phone.getText().toString());
            
            
            new SendRegisterRequest().execute();
        }
    }
    
    
    public class SendRegisterRequest extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Please wait while app registering you to the system...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_REGISTRATION));
            

            try {
                JSONObject regObj = new JSONObject();
                regObj.put("name", regInfo.getName());
                regObj.put("email", regInfo.getEmail());
                regObj.put("password", regInfo.getPassword());
                regObj.put("dob", regInfo.getDob());
                regObj.put("profession_id", regInfo.getProfessionId());
                regObj.put("education_id", regInfo.getEducationId());
                regObj.put("sex", regInfo.getSex());
                regObj.put("country_id", regInfo.getCountryId());
                regObj.put("city_id", regInfo.getCityId());
                regObj.put("area_id", regInfo.getAreaId());
                regObj.put("address", regInfo.getAddress());
                regObj.put("phone", regInfo.getPhone());
                
                String regData = regObj.toString();
                Log.d("<<>>", "req data = " + regData);
                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, regData, null);
                if(response.getStatus() == 200){
                    JSONObject responseObj = response.getjObj();
                    if(responseObj.has("success"))
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
                alert("Registration error, please try again.", false);
            }
//                updateUI();
        }        
    }


    public class RetrieveCountryList extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Retrieving data, please wait...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_COUNTRY));
            
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, null);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving country list");
                countryList = Country.parseCountryList(response.getjObj().toString());
                return true;
            }
                        
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                List<String> countryNameList = new ArrayList<String>();
                for(Country country : countryList)
                    countryNameList.add(country.getName());
                
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, countryNameList);
                tvCountry.setAdapter(countryAdapter);
            }
        }        
    }
    
    
    
    
    public class RetrieveCityList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Retrieving city list, please wait");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_CITIES));
            urlParam.add(new BasicNameValuePair("country_id", "" + params[0]));
            
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, null);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving city list");
                cityList = City.parseCityList(response.getjObj().toString());
                return true;
            }
                        
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                List<String> cityNameList = new ArrayList<String>();
                for(City city : cityList)
                    cityNameList.add(city.getName());
                
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, cityNameList);
                tvCity.setAdapter(cityAdapter);
            }
        }        
    }
    
    
    public class RetrieveAreaList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Retrieving area list, please wait");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_AREA));
            urlParam.add(new BasicNameValuePair("city_id", "" + params[0]));
            
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, null);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving area list");
                areaList = Area.parseAreaList(response.getjObj().toString());
                return true;
            }
                        
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                List<String> areaNameList = new ArrayList<String>();
                for(Area area : areaList)
                    areaNameList.add(area.getName());
                
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, areaNameList);
                tvArea.setAdapter(areaAdapter);
            }
        }        
    }
    
    
    
    public class RetrieveProfessionList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_PROFESSION));
            
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, null);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving area list");
                professionList = Profession.parseprofessionList(response.getjObj().toString());
                return true;
            }
                        
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                List<String> professionTypeList = new ArrayList<String>();
                for(Profession profession : professionList)
                    professionTypeList.add(profession.getType());
                
                String[] strarray = professionTypeList.toArray(new String[0]);
                generateSpinner(sProfession, strarray);
            }
        }        
    }
    
    
    public class RetrieveEducationList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            String rootUrl = Constants.URL_ROOT;
            
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_EDUCATION));
            
            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, null);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in retrieving area list");
                educationList = Education.parseEducationList(response.getjObj().toString());
                return true;
            }
                        
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                List<String> educationTypeList = new ArrayList<String>();
                for(Education education : educationList)
                    educationTypeList.add(education.getType());
                
                String[] strarray = educationTypeList.toArray(new String[0]);
                generateSpinner(sEducation, strarray);
            }
        }        
    }
    
    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(RegisterActivity.this);
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
