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
import android.graphics.Typeface;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.priyo.apps.jonopriyo.model.RegistrationInfo;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class ProfileNewActivity extends FragmentActivity implements OnDateSetListener  {

    private static final String TAG = ProfileNewActivity.class.getSimpleName();
    public static final String[] CONTENT    = new String[] { "প্রাথমিক", "ঠিকানা", "অন্যান্য" };

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
    public static String sex = "male";


    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    private ProgressDialog pDialog;
    private static final int DATE_PICKER = 1;

    Calendar calendar;
    
    TextView Title;
    Button Update;
    
    Typeface tf;

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
        
        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        
        Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.profile));
        
        Update = (Button) findViewById(R.id.b_update);
        Update.setTypeface(tf);
        Update.setText(getResources().getString(R.string.update));
        
        
        new GetProfileData().execute();


        FixedTabsView mFixedTabs = (FixedTabsView) findViewById(R.id.fixed_tabs);

        final FragmentPagerAdapter adapter = new ProfileTabsAdapter(getSupportFragmentManager());

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

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
        
        TextView ChangePass = (TextView) textEntryView.findViewById(R.id.tv_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        ChangePass.setTypeface(tf);
        ChangePass.setText(getResources().getString(R.string.change_pass));

        final EditText CurrentPass = (EditText) textEntryView.findViewById(R.id.et_current_password);
        final EditText NewPass = (EditText) textEntryView.findViewById(R.id.et_new_password);
        final EditText ConfirmNewPass = (EditText) textEntryView.findViewById(R.id.et_confirm_new_password);

        CurrentPass.setTypeface(tf);
        NewPass.setTypeface(tf);
        ConfirmNewPass.setTypeface(tf);
        CurrentPass.setHint(getResources().getString(R.string.current_pass));
        NewPass.setHint(getResources().getString(R.string.new_pass));
        ConfirmNewPass.setHint(getResources().getString(R.string.confirm_new_pass));
        
        Button OK = (Button) textEntryView.findViewById(R.id.b_ok);
        OK.setTypeface(tf);
        OK.setText(getResources().getString(R.string.ok));        
        
        CurrentPass.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CurrentPass.setTypeface(Typeface.SANS_SERIF); 
                return false;
            }
        });
        
        NewPass.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                NewPass.setTypeface(Typeface.SANS_SERIF); 
                return false;
            }
        });
        
        ConfirmNewPass.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConfirmNewPass.setTypeface(Typeface.SANS_SERIF); 
                return false;
            }
        });        
        
        
        OK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String curPass = CurrentPass.getText().toString();
                String newPass = NewPass.getText().toString();
                String confirmNewPass = ConfirmNewPass.getText().toString();
                if(curPass.trim().equals("")){
                    Toast.makeText(ProfileNewActivity.this, "বর্তমান পাসওয়ার্ড সঠিক ভাবে লিখুন", Toast.LENGTH_SHORT).show();
                }
                else if(newPass.trim().equals("") || newPass.equals(confirmNewPass)){
                    alert.dismiss(); 
                    new ChangePasswordReq().execute(curPass, newPass);
                }
                else{
                    Toast.makeText(ProfileNewActivity.this, "পাসওয়ার্ড নিশ্চিতকরণে ভুল হয়েছে", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alert.show();
    }


    public void onClickUpdate(View v){
        String name = ProfileBasicFragment.etName.getText().toString();
        String selectedDate = ProfileOthersFragment.dobToStore;

        if(name == null || name.equals("")){
            Toast.makeText(ProfileNewActivity.this, "দয়া করে সঠিক ভাবে নাম লিখুন", Toast.LENGTH_SHORT).show();
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

        selectedCountryId = (long) 14;
        selectedCityId = regInfo.getCityId();
        selectedAreaId = regInfo.getAreaId();
        educationId = regInfo.getEducationId();
        professionId = regInfo.getProfessionId();

        new RetrieveCityList().execute(selectedCountryId);
        new RetrieveAreaList().execute(selectedCityId);
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
                alert("পাসওয়ার্ড আপডেট করা হয়েছে", false);
            }
            else{
                alert("পাসওয়ার্ড আপডেট সফল হয়নি, অনুগ্রহ করে আবার চেষ্টা করুন.", false);
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
                alert("প্রোফাইল আপডেট সফল হয়েছে.", true);
            }
            else{
                alert("প্রোফাইল আপডেট সফল হয়নি, অনুগ্রহ করে আবার চেষ্টা করুন.", false);
            }
            //                updateUI();
        }        
    }



    private class GetProfileData extends AsyncTask<Void, Void, Boolean> {

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



    void alert(String message, final Boolean success) {
        AlertDialog.Builder bld = new AlertDialog.Builder(ProfileNewActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("ঠিক আছে", new DialogInterface.OnClickListener() {

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
