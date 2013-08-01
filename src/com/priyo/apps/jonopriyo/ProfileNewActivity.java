package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.FixedTabsView;
import com.astuetz.viewpager.extensions.TabsAdapter;
import com.astuetz.viewpager.extensions.ViewPagerTabButton;
import com.priyo.apps.jonopriyo.fragment.DatePickerFragment;
import com.priyo.apps.jonopriyo.fragment.ProfileAddressFragment;
import com.priyo.apps.jonopriyo.fragment.ProfileBasicFragment;
import com.priyo.apps.jonopriyo.fragment.ProfileOthersFragment;
import com.priyo.apps.jonopriyo.model.Area;
import com.priyo.apps.jonopriyo.model.City;
import com.priyo.apps.jonopriyo.model.Country;
import com.priyo.apps.jonopriyo.model.RegistrationInfo;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class ProfileNewActivity extends FragmentActivity implements OnDateSetListener  {

    private static final String TAG = ProfileNewActivity.class.getSimpleName();
    public static final String[] CONTENT    = new String[] { "Basic", "Address", "Others" };

//    EditText Name, Address, Phone;
//    TextView DoB, Email;
//    Spinner sProfession, sEducation, sSex;
//    AutoCompleteTextView tvCountry, tvCity, tvArea;


    public static RegistrationInfo regInfo;

//    List<Country> countryList;
//    List<City> cityList;
//    List<Area> areaList;


    public static Long selectedCountryId, selectedCityId, selectedAreaId;    
    public static Long educationId, professionId;
    public static String sex;


    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    private ProgressDialog pDialog;
    private static final int DATE_PICKER = 1;

    Calendar calendar;

    //  boolean sProfessionSelectable, sEducationSelectable;
//    boolean sProfessionTouchable, sEducationTouchable;
    

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.profile_settings_new);


        appInstance = (JonopriyoApplication) getApplication();
        jsonParser = new JsonParser();
        calendar = Calendar.getInstance();
        pDialog = new ProgressDialog(ProfileNewActivity.this);  
        
        
        new GetProfileData().execute();

//        Name = (EditText) findViewById(R.id.et_name);
//        Email = (TextView) findViewById(R.id.tv_email);
//        Address = (EditText) findViewById(R.id.et_address);
//        Phone = (EditText) findViewById(R.id.et_phone);

//        DoB = (TextView) findViewById(R.id.tv_dob);


        
        
//        sProfession = (Spinner) findViewById(R.id.s_profession);
//        sProfession.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(!sProfessionTouchable){
//                    Log.d("/////", "profession list retrieving");
//                    sProfessionTouchable = true;
//                    new RetrieveProfessionList().execute();
//                    return false;
//                }
//                else{
//                    return true;
//                }
//            }
//        });
//
//        sProfession.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//                Log.d("/////", "profession selectable");
//                professionId = professionList.get(position).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });

//        sEducation = (Spinner) findViewById(R.id.s_education);
//        sEducation.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(!sEducationTouchable){
//                    Log.d("/////", "education list retrieving");
//                    sEducationTouchable = true;
//                    new RetrieveEducationList().execute();
//                    return false;
//                }
//                else{
//                    return true;
//                }
//            }
//        });
//
//        sEducation.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//                educationId = educationList.get(position).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });

//        sSex = (Spinner) findViewById(R.id.s_sex);
//        generateSpinner(sSex, Utility.sex_array);
//        sSex.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//                sex = (String)parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });


//        tvCountry = (AutoCompleteTextView) findViewById(R.id.et_country);
//        tvCountry.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                String selection = (String)parent.getItemAtPosition(pos);
//                selectedCountryId = (long) 0;
//                for(Country country : countryList){
//                    if(selection.equals(country.getName())){
//                        selectedCountryId = country.getId();
//                        break;
//                    }
//                }
//                new RetrieveCityList().execute(selectedCountryId);
//
//            }
//        });


//        tvCity = (AutoCompleteTextView) findViewById(R.id.et_city);
//        tvCity.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                String selection = (String)parent.getItemAtPosition(pos);
//                selectedCityId = (long) 0;
//                for(City city : cityList){
//                    if(selection.equals(city.getName())){
//                        selectedCityId = city.getId();
//                        break;
//                    }
//                }
//                new RetrieveAreaList().execute(selectedCityId);
//
//            }
//        });

//        tvArea = (AutoCompleteTextView) findViewById(R.id.et_area);
//        tvArea.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                String selection = (String)parent.getItemAtPosition(pos);
//                selectedAreaId = (long) 0;
//                for(Area area : areaList){
//                    if(selection.equals(area.getName())){
//                        selectedAreaId = area.getId();
//                        break;
//                    }
//                }
//
//            }
//        });


        FixedTabsView mFixedTabs = (FixedTabsView) findViewById(R.id.fixed_tabs);

        final FragmentPagerAdapter adapter = new ProfileTabsAdapter(getSupportFragmentManager());

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //        final TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        //        indicator.setViewPager(pager);

        TabsAdapter mFixedTabsAdapter = new FixedTabsAdapter(this);
        mFixedTabs.setAdapter(mFixedTabsAdapter);
        mFixedTabs.setViewPager(pager);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            finish();
            overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onClickCalendar(View v){
        DialogFragment newFragment = new DatePickerFragment().newInstance(calendar, "profile");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    } 

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        ProfileOthersFragment.dobToStore = String.format("%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
        ProfileOthersFragment.DoB.setText(Utility.parseDate(ProfileOthersFragment.dobToStore));
        calendar.set(year, monthOfYear, dayOfMonth);
        //        selectedDate = DoB.getText().toString();

    }  


    public void onClickChangePassword(View v){

        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View textEntryView = inflater.inflate(R.layout.dialog_change_password, null);
        final AlertDialog alert = new AlertDialog.Builder(ProfileNewActivity.this).create();
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
                    Toast.makeText(ProfileNewActivity.this, "Confirmation mismatch.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alert.show();
    }


    public void onClickUpdate(View v){
        String name = ProfileBasicFragment.etName.getText().toString();
        String selectedDate = ProfileOthersFragment.dobToStore;

        if(name == null || name.equals("")){
            Toast.makeText(ProfileNewActivity.this, "Please insert your name.", Toast.LENGTH_SHORT).show();
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
            regInfo.setAddress(ProfileAddressFragment.etAddress.getText().toString());
            regInfo.setPhone(ProfileBasicFragment.etPhone.getText().toString());


            new PostProfileData().execute();
        }
    }
    
    
//    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
//                ProfileNewActivity.this, R.layout.my_simple_spinner_item, arrayToSpinner);
//        spinner.setAdapter(myAdapter);
//        myAdapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
//
//    }


    private void initializeFields(){
        ProfileBasicFragment.etName.setText(regInfo.getName());
        ProfileBasicFragment.tvEmail.setText(regInfo.getEmail());
        ProfileBasicFragment.etPhone.setText(regInfo.getPhone());
        
        ProfileAddressFragment.etAddress.setText(regInfo.getAddress());
        ProfileAddressFragment.tvCountry.setText("Bangladesh");             // hard-coded

        String date = regInfo.getDob();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        calendar.set(year, month, day);
        ProfileOthersFragment.dobToStore = String.format("%04d-%02d-%02d", year, (month + 1), day);
//        ProfileOthersFragment.DoB.setText(Utility.parseDate(ProfileOthersFragment.dobToStore));

//        generateSpinner(ProfileOthersFragment.sSex, Utility.sex_array);
//        if(regInfo.getSex().equals("M")){
//            ProfileOthersFragment.sSex.setSelection(0);
//            sex = "male";
//        }
//        else{
//            ProfileOthersFragment.sSex.setSelection(1);
//            sex = "female";
//        }

//        selectedCountryId = regInfo.getCountryId();
        selectedCountryId = (long) 14;
        selectedCityId = regInfo.getCityId();
        selectedAreaId = regInfo.getAreaId();
        educationId = regInfo.getEducationId();
        professionId = regInfo.getProfessionId();


//        new RetrieveCountryList().execute();
        new RetrieveCityList().execute(selectedCountryId);
        new RetrieveAreaList().execute(selectedCityId);

//        List<String> educationTypeList = new ArrayList<String>();
//        for(Education education : educationList)
//            educationTypeList.add(education.getType());
//
//        String[] eArray = educationTypeList.toArray(new String[0]);
//        generateSpinner(ProfileOthersFragment.sEducation, eArray);
//
//        for(int educationIndex = 0; educationIndex < educationList.size(); educationIndex++){
//            if(educationId.equals(educationList.get(educationIndex).getId())){
//                ProfileOthersFragment.sEducation.setSelection(educationIndex);
//                break;
//            }
//        }
//
//        List<String> professionTypeList = new ArrayList<String>();
//        for(Profession profession : professionList)
//            professionTypeList.add(profession.getType());
//
//        String[] pArray = professionTypeList.toArray(new String[0]);
//        generateSpinner(ProfileOthersFragment.sProfession, pArray);
//
//        for(int professionIndex = 0; professionIndex < professionList.size(); professionIndex++){
//            if(professionId.equals(professionList.get(professionIndex).getId())){
//                ProfileOthersFragment.sProfession.setSelection(professionIndex);
//                break;
//            }
//        }
    }


    private class ChangePasswordReq extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileNewActivity.this);
            pDialog.setMessage("Loading...");
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
            if(pDialog.isShowing())
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
            pDialog.setMessage("Loading...");
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
            if(pDialog.isShowing())
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

            pDialog.setMessage("Loading...");
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
            if(pDialog.isShowing())
                pDialog.dismiss();
        }

    }   
//    
//    public class RetrieveCountryList extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //            pDialog = new ProgressDialog(ProfileActivity.this);
//            //            pDialog.setMessage("Retrieving data, please wait...");
//            //            pDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            String rootUrl = Constants.URL_ROOT;
//
//            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
//            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_COUNTRY));
//
//            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
//                    urlParam, null, null);
//            if(response.getStatus() == 200){
//                Log.d(">>>><<<<", "success in retrieving country list");
//                countryList = Country.parseCountryList(response.getjObj().toString());
//                return true;
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if(result){
//                List<String> countryNameList = new ArrayList<String>();
//                for(Country country : countryList)
//                    countryNameList.add(country.getName());
//
//                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(ProfileNewActivity.this,
//                        
//                        R.layout.my_autocomplete_text_style, countryNameList);
//                tvCountry.setAdapter(countryAdapter);
//
//                for(int countryIndex = 0; countryIndex < countryList.size(); countryIndex++){
//
//                    if(selectedCountryId.equals(countryList.get(countryIndex).getId())){
//                        tvCountry.setText(countryList.get(countryIndex).getName());
//                        break;
//                    }
//                }
//            }
//        }        
//    }
    
    
    public class RetrieveCityList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                ProfileAddressFragment.cityList = City.parseCityList(response.getjObj().toString());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                List<String> cityNameList = new ArrayList<String>();
                for(City city : ProfileAddressFragment.cityList)
                    cityNameList.add(city.getName());

                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(ProfileNewActivity.this,
                        R.layout.my_autocomplete_text_style, cityNameList);
                ProfileAddressFragment.tvCity.setAdapter(cityAdapter);

                for(int cityIndex = 0; cityIndex < ProfileAddressFragment.cityList.size(); cityIndex++){
                    if(selectedCityId.equals(ProfileAddressFragment.cityList.get(cityIndex).getId())){
                        ProfileAddressFragment.tvCity.setText(ProfileAddressFragment.cityList.get(cityIndex).getName());
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
                ProfileAddressFragment.areaList = Area.parseAreaList(response.getjObj().toString());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                List<String> areaNameList = new ArrayList<String>();
                for(Area area : ProfileAddressFragment.areaList)
                    areaNameList.add(area.getName());

                ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(ProfileNewActivity.this,
                        R.layout.my_autocomplete_text_style, areaNameList);
                ProfileAddressFragment.tvArea.setAdapter(areaAdapter);

                for(int areaIndex = 0; areaIndex < ProfileAddressFragment.areaList.size(); areaIndex++){
                    if(selectedAreaId.equals(ProfileAddressFragment.areaList.get(areaIndex).getId())){
                        ProfileAddressFragment.tvArea.setText(ProfileAddressFragment.areaList.get(areaIndex).getName());
                        break;
                    }
                }
            }
        }        
    }



//    public class RetrieveProfessionList extends AsyncTask<Long, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Long... params) {
//            String rootUrl = Constants.URL_ROOT;
//
//            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
//            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_PROFESSION));
//
//            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
//                    urlParam, null, null);
//            if(response.getStatus() == 200){
//                Log.d(">>>><<<<", "success in retrieving area list");
//                professionList = Profession.parseprofessionList(response.getjObj().toString());
//                return true;
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if(pDialog.isShowing())
//                pDialog.dismiss();
//            if(result){
//                List<String> professionTypeList = new ArrayList<String>();
//                for(Profession profession : professionList)
//                    professionTypeList.add(profession.getType());
//                
//                JonopriyoDatabase dbInstance = new JonopriyoDatabase(ProfileNewActivity.this);
//                dbInstance.open();
//                for(Profession profession : professionList){
//                    dbInstance.insertOrUpdateProfession(profession);
//                }
//                dbInstance.close();
//
//                String[] strarray = professionTypeList.toArray(new String[0]);
//                generateSpinner(sProfession, strarray);
//
//                for(int professionIndex = 0; professionIndex < professionList.size(); professionIndex++){
//                    if(professionId.equals(professionList.get(professionIndex).getId())){
//                        sProfession.setSelection(professionIndex);
//                        break;
//                    }
//                }
//                sProfessionTouchable = false;
//                sProfession.performClick();
//            }
//        }        
//    }


//    public class RetrieveEducationList extends AsyncTask<Long, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Long... params) {
//            String rootUrl = Constants.URL_ROOT;
//
//            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
//            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_EDUCATION));
//
//            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
//                    urlParam, null, null);
//            if(response.getStatus() == 200){
//                Log.d(">>>><<<<", "success in retrieving area list");
//                educationList = Education.parseEducationList(response.getjObj().toString());
//                return true;
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if(pDialog.isShowing())
//                pDialog.dismiss();
//            if(result){
//                List<String> educationTypeList = new ArrayList<String>();
//                for(Education education : educationList)
//                    educationTypeList.add(education.getType());
//                
//                JonopriyoDatabase dbInstance = new JonopriyoDatabase(ProfileNewActivity.this);
//                dbInstance.open();
//                for(Education education : educationList){
//                    dbInstance.insertOrUpdateEducation(education);
//                }
//                dbInstance.close();
//
//                String[] strarray = educationTypeList.toArray(new String[0]);
//                generateSpinner(sEducation, strarray);
//
//                for(int educationIndex = 0; educationIndex < educationList.size(); educationIndex++){
//                    if(educationId.equals(educationList.get(educationIndex).getId())){
//                        sEducation.setSelection(educationIndex);
//                        break;
//                    }
//                }
//                sEducationTouchable = false;
//                sEducation.performClick();
//            }
//        }        
//    }



    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(ProfileNewActivity.this);
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






















    public  class ProfileTabsAdapter extends FragmentPagerAdapter{
        public ProfileTabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ProfileBasicFragment.newInstance();
            } else if (position == 1) {
                return ProfileAddressFragment.newInstance();

            } else if (position == 2) {
                return ProfileOthersFragment.newInstance();
            } else
                return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }


    public class FixedTabsAdapter implements TabsAdapter {

        private Activity mContext;        

        public FixedTabsAdapter(Activity ctx) {
            this.mContext = ctx;
        }

        @Override
        public View getView(int position) {
            ViewPagerTabButton tab;

            LayoutInflater inflater = mContext.getLayoutInflater();
            tab = (ViewPagerTabButton) inflater.inflate(R.layout.tab_fixed, null);

            if (position < CONTENT.length) tab.setText(CONTENT[position]);

            return tab;
        }

    }




}
