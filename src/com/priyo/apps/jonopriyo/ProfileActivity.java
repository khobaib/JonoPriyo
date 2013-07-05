package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.apps.jonopriyo.LoginActivity.SendForgetPassRequest;
import com.priyo.apps.jonopriyo.RegisterActivity.RetrieveAreaList;
import com.priyo.apps.jonopriyo.RegisterActivity.RetrieveCityList;
import com.priyo.apps.jonopriyo.RegisterActivity.RetrieveCountryList;
import com.priyo.apps.jonopriyo.RegisterActivity.RetrieveEducationList;
import com.priyo.apps.jonopriyo.RegisterActivity.RetrieveProfessionList;
import com.priyo.apps.jonopriyo.RegisterActivity.SendRegisterRequest;
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
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class ProfileActivity extends FragmentActivity implements OnDateSetListener {

    EditText Name, Email, Address, Phone;
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
    JonopriyoApplication appInstance;

    private ProgressDialog pDialog;
    private static final int DATE_PICKER = 1;

    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);

        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();


        calendar = Calendar.getInstance();

        Name = (EditText) findViewById(R.id.et_name);
        Email = (EditText) findViewById(R.id.et_email);
        Address = (EditText) findViewById(R.id.et_address);
        Phone = (EditText) findViewById(R.id.et_phone);

        DoB = (TextView) findViewById(R.id.tv_dob);
        DoB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment().newInstance(calendar, "profile");
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

        new GetProfileData().execute();
    }

    public void onClickChangePassword(View v){

        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View textEntryView = inflater.inflate(R.layout.dialog_change_password, null);
        final AlertDialog alert = new AlertDialog.Builder(ProfileActivity.this).create();
        alert.setView(textEntryView, 0, 0, 0, 0);

        final EditText CurrentPass = (EditText) textEntryView.findViewById(R.id.et_current_password);
        final EditText NewPass = (EditText) textEntryView.findViewById(R.id.et_new_password);
        final EditText ConfirmNewPass = (EditText) textEntryView.findViewById(R.id.et_confirm_new_password);


        Button OK = (Button) textEntryView.findViewById(R.id.b_ok);
        OK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String curPass = CurrentPass.getText().toString();
                String newPass = NewPass.getText().toString();
                String confirmNewPass = ConfirmNewPass.getText().toString();
                alert.dismiss(); 
                // HERE WE NEED TO ADD CHECK ON CURRENT PASS, NEED TO MODIFY API
                if(newPass.equals(confirmNewPass))
                    new ChangePasswordReq().execute(curPass, newPass);
                else{
                    Toast.makeText(ProfileActivity.this, "Confirmation mismatch.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alert.show();
    }


    public void onClickUpdate(View v){
        String name = Name.getText().toString();
        String selectedDate = DoB.getText().toString();

        if(name == null || name.equals("")){
            Toast.makeText(ProfileActivity.this, "Please insert your name.", Toast.LENGTH_SHORT).show();
        }

        else{
            regInfo.setName(name);
            regInfo.setDob(selectedDate);
            regInfo.setProfessionId(professionId);
            regInfo.setEducationId(educationId);
            regInfo.setSex((sex.equals("male") ? "M" : "F"));
            regInfo.setCountryId(selectedCountryId);
            regInfo.setCityId(selectedCityId);
            regInfo.setAreaId(selectedAreaId);
            regInfo.setAddress(Address.getText().toString());
            regInfo.setPhone(Phone.getText().toString());


            new PostProfileData().execute();
        }
    }


    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                ProfileActivity.this, android.R.layout.simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    private void initializeFields(){
        Name.setText(regInfo.getName());
        Email.setText(regInfo.getEmail());
        Address.setText(regInfo.getAddress());
        Phone.setText(regInfo.getPhone());

        String date = regInfo.getDob();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        calendar.set(year, month, day);
        DoB.setText(String.format("%04d-%02d-%02d", year, (month + 1), day));

        if(regInfo.getSex().equals("M")){
            sSex.setSelection(0);
            sex = "male";
        }
        else{
            sSex.setSelection(1);
            sex = "female";
        }

        selectedCountryId = regInfo.getCountryId();
        selectedCityId = regInfo.getCityId();
        selectedAreaId = regInfo.getAreaId();
        educationId = regInfo.getEducationId();
        professionId = regInfo.getProfessionId();


        new RetrieveCountryList().execute();
        new RetrieveCityList().execute(selectedCountryId);
        new RetrieveAreaList().execute(selectedCityId);

        new RetrieveProfessionList().execute();
        new RetrieveEducationList().execute();

    }


    private class ChangePasswordReq extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Requesting password change, please wait...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_CHANGE_PASSWORD));

            String token = appInstance.getAccessToken();

            try {
                JSONObject passwordObj = new JSONObject();
                passwordObj.put("current_password", params[0]);
                passwordObj.put("new_password", params[1]);
                String content = passwordObj.toString();

                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, content, token);
                if(response.getStatus() == 200){
                    JSONObject jsonResponse = response.getjObj();
                    try {
                        String responseType = jsonResponse.getString("response");
                        if(responseType.equals("success")){
                            Log.d(">>>><<<<", "success in retrieving reg info");
                            return true;
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }                
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                alert("Password updated.", false);
            }

        }

    }


    private class PostProfileData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Please wait while your profile is being updated...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_POST_PROFILE_INFO));


            try {
                JSONObject profileObj = new JSONObject();
                profileObj.put("name", regInfo.getName());
                profileObj.put("dob", regInfo.getDob());
                profileObj.put("profession_id", regInfo.getProfessionId());
                profileObj.put("education_id", regInfo.getEducationId());
                profileObj.put("sex", regInfo.getSex());
                profileObj.put("country_id", regInfo.getCountryId());
                profileObj.put("city_id", regInfo.getCityId());
                profileObj.put("area_id", regInfo.getAreaId());
                profileObj.put("address", regInfo.getAddress());
                profileObj.put("phone", regInfo.getPhone());

                JSONObject profileInfo = new JSONObject();
                profileInfo.put("profile_info", profileObj);

                String profileData = profileInfo.toString();
                Log.d("<<>>", "req data = " + profileData);

                String token = appInstance.getAccessToken();
                ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, rootUrl,
                        urlParam, profileData, token);
                if(response.getStatus() == 200){
                    JSONObject jsonResponse = response.getjObj();
                    String responseType = jsonResponse.getString("response");
                    if(responseType.equals("success")){
                        return true;
                    }


                }
            }catch (JSONException e) {                
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog != null)
                pDialog.dismiss();
            if(result){
                alert("Profile Update successful.", true);
            }
            else{
                alert("Error updating profile.", false);
            }
            //                updateUI();
        }        
    }



    private class GetProfileData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Retrieving profile data, please wait...");
            pDialog.show();
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
                        regInfo = RegistrationInfo.parseRegInfo(regObj.toString());
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
            if(result){
                initializeFields();
                // call all asynctask
            }
            if(pDialog != null)
                pDialog.dismiss();
        }

    }





    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DoB.setText(String.format("%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));
        calendar.set(year, monthOfYear, dayOfMonth);
        //        selectedDate = DoB.getText().toString();

    }




    public class RetrieveCountryList extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //            pDialog = new ProgressDialog(ProfileActivity.this);
            //            pDialog.setMessage("Retrieving data, please wait...");
            //            pDialog.show();
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
            //            if(pDialog != null)
            //                pDialog.dismiss();
            if(result){
                List<String> countryNameList = new ArrayList<String>();
                for(Country country : countryList)
                    countryNameList.add(country.getName());

                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, countryNameList);
                tvCountry.setAdapter(countryAdapter);

                for(int countryIndex = 0; countryIndex < countryList.size(); countryIndex++){
                    //                    Log.d(">>>>>>>>><<<<<", "selected country id = " + selectedCountryId);
                    Log.d(">>>>>>>>><<<<<", "countrylist country id = " + countryList.get(countryIndex).getId());
                    if(selectedCountryId.equals(countryList.get(countryIndex).getId())){
                        //                        Log.d(">>>>>>>>><<<<<", "selected country name = " + countryList.get(countryIndex).getName());
                        tvCountry.setText(countryList.get(countryIndex).getName());
                        break;
                    }
                }
            }
        }        
    }




    public class RetrieveCityList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //            pDialog = new ProgressDialog(ProfileActivity.this);
            //            pDialog.setMessage("Retrieving city list, please wait");
            //            pDialog.show();
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
            //            if(pDialog != null)
            //                pDialog.dismiss();
            if(result){
                List<String> cityNameList = new ArrayList<String>();
                for(City city : cityList)
                    cityNameList.add(city.getName());

                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, cityNameList);
                tvCity.setAdapter(cityAdapter);

                for(int cityIndex = 0; cityIndex < cityList.size(); cityIndex++){
                    if(selectedCityId.equals(cityList.get(cityIndex).getId())){
                        tvCity.setText(cityList.get(cityIndex).getName());
                        break;
                    }
                }
            }
        }        
    }


    public class RetrieveAreaList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //            pDialog = new ProgressDialog(ProfileActivity.this);
            //            pDialog.setMessage("Retrieving area list, please wait");
            //            pDialog.show();
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
            //            if(pDialog != null)
            //                pDialog.dismiss();
            if(result){
                List<String> areaNameList = new ArrayList<String>();
                for(Area area : areaList)
                    areaNameList.add(area.getName());

                ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, areaNameList);
                tvArea.setAdapter(areaAdapter);

                for(int areaIndex = 0; areaIndex < areaList.size(); areaIndex++){
                    if(selectedAreaId.equals(areaList.get(areaIndex).getId())){
                        tvArea.setText(areaList.get(areaIndex).getName());
                        break;
                    }
                }
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

                for(int professionIndex = 0; professionIndex < professionList.size(); professionIndex++){
                    if(professionId.equals(professionList.get(professionIndex).getId())){
                        sProfession.setSelection(professionIndex);
                        break;
                    }
                }
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

                for(int educationIndex = 0; educationIndex < educationList.size(); educationIndex++){
                    if(educationId.equals(educationList.get(educationIndex).getId())){
                        sEducation.setSelection(educationIndex);
                        break;
                    }
                }
            }
        }        
    }


    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(ProfileActivity.this);
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
