package com.priyo.apps.jonopriyo.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.priyo.apps.jonopriyo.ProfileNewActivity;
import com.priyo.apps.jonopriyo.R;
import com.priyo.apps.jonopriyo.db.JonopriyoDatabase;
import com.priyo.apps.jonopriyo.model.Education;
import com.priyo.apps.jonopriyo.model.Profession;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class ProfileOthersFragment extends Fragment {

    Activity activity;

    JsonParser jsonParser;
    JonopriyoApplication appInstance;
    private ProgressDialog pDialog;

    List<Education> educationList;
    List<Profession> professionList;

    Typeface tf;

    TextView Gender, DoBTitle, ProfessionTitle, EducationTitle, OthersTitle;

    public static TextView DoB;
    public static String dobToStore;
    public static Spinner sProfession, sEducation, sSex;

    boolean sProfessionTouchable, sEducationTouchable;

    public ProfileOthersFragment() {
    }

    public static ProfileOthersFragment newInstance() {
        final ProfileOthersFragment fragment = new ProfileOthersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile_others, container, false);


        sProfession = (Spinner) view.findViewById(R.id.s_profession);
        sProfession.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!sProfessionTouchable){
                    Log.d("/////", "profession list retrieving");
                    sProfessionTouchable = true;
                    new RetrieveProfessionList().execute();
                    return false;
                }
                else{
                    return true;
                }
            }

        });

        sProfession.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                Log.d("/////", "profession selectable");
                ProfileNewActivity.professionId = professionList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sEducation = (Spinner) view.findViewById(R.id.s_education);
        sEducation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!sEducationTouchable){
                    Log.d("/////", "education list retrieving");
                    sEducationTouchable = true;
                    new RetrieveEducationList().execute();
                    return false;
                }
                else{
                    return true;
                }
            }
        });

        sEducation.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                ProfileNewActivity.educationId = educationList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sSex = (Spinner) view.findViewById(R.id.s_sex);
        sSex.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                ProfileNewActivity.sex = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        DoB = (TextView) view.findViewById(R.id.tv_dob);
        if(dobToStore != null)
            DoB.setText(Utility.parseDate(ProfileOthersFragment.dobToStore));        

        Gender = (TextView) view.findViewById(R.id.tv_sex);
        DoBTitle = (TextView) view.findViewById(R.id.tv_dob_title);
        ProfessionTitle = (TextView) view.findViewById(R.id.tv_profession);
        EducationTitle = (TextView) view.findViewById(R.id.tv_education);
        OthersTitle = (TextView) view.findViewById(R.id.tv_title);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {

            tf = Typeface.createFromAsset(this.activity.getAssets(), "font/suttony.ttf");

            Gender.setTypeface(tf);
            DoBTitle.setTypeface(tf);
            ProfessionTitle.setTypeface(tf);
            EducationTitle.setTypeface(tf);
            OthersTitle.setTypeface(tf);

            Gender.setText(getResources().getString(R.string.gender));
            DoBTitle.setText(getResources().getString(R.string.dob));
            ProfessionTitle.setText(getResources().getString(R.string.profession));
            EducationTitle.setText(getResources().getString(R.string.education));
            OthersTitle.setText(getResources().getString(R.string.others));

            appInstance = (JonopriyoApplication) this.activity.getApplication();
            jsonParser = new JsonParser();
            //            calendar = Calendar.getInstance();
            pDialog = new ProgressDialog(this.activity); 

            if(dobToStore != null)
                DoB.setText(Utility.parseDate(ProfileOthersFragment.dobToStore));

            JonopriyoDatabase dbInstance = new JonopriyoDatabase(this.activity);
            dbInstance.open();
            professionList = dbInstance.retrieveProfessionList();
            educationList = dbInstance.retrieveEducationList();
            dbInstance.close();

            if(educationList == null || educationList.size() == 0){
                new RetrieveEducationList().execute();
            }
            else{
                List<String> educationTypeList = new ArrayList<String>();
                for(Education education : educationList)
                    educationTypeList.add(education.getType());

                String[] eArray = educationTypeList.toArray(new String[0]);
                generateSpinner(ProfileOthersFragment.sEducation, eArray);

                for(int educationIndex = 0; educationIndex < educationList.size(); educationIndex++){
                    if(ProfileNewActivity.educationId.equals(educationList.get(educationIndex).getId())){
                        ProfileOthersFragment.sEducation.setSelection(educationIndex);
                        break;
                    }
                }
            }

            if(professionList == null || professionList.size() == 0){
                new RetrieveProfessionList().execute();
            }
            else{
                List<String> professionTypeList = new ArrayList<String>();
                for(Profession profession : professionList)
                    professionTypeList.add(profession.getType());

                String[] pArray = professionTypeList.toArray(new String[0]);
                generateSpinner(ProfileOthersFragment.sProfession, pArray);

                for(int professionIndex = 0; professionIndex < professionList.size(); professionIndex++){
                    if(ProfileNewActivity.professionId.equals(professionList.get(professionIndex).getId())){
                        ProfileOthersFragment.sProfession.setSelection(professionIndex);
                        break;
                    }
                }
            }

            generateSpinner(ProfileOthersFragment.sSex, Utility.sex_array);
            if(ProfileNewActivity.regInfo.getSex().equals("M")){
                ProfileOthersFragment.sSex.setSelection(0);
                ProfileNewActivity.sex = "male";
            }
            else{
                ProfileOthersFragment.sSex.setSelection(1);
                ProfileNewActivity.sex = "female";
            }
        }

        sProfessionTouchable = false;
        sEducationTouchable = false;
    }


    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                activity, R.layout.my_simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);

    }


    public class RetrieveProfessionList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                List<String> professionTypeList = new ArrayList<String>();
                for(Profession profession : professionList)
                    professionTypeList.add(profession.getType());

                JonopriyoDatabase dbInstance = new JonopriyoDatabase(activity);
                dbInstance.open();
                for(Profession profession : professionList){
                    dbInstance.insertOrUpdateProfession(profession);
                }
                dbInstance.close();

                String[] strarray = professionTypeList.toArray(new String[0]);
                generateSpinner(sProfession, strarray);

                for(int professionIndex = 0; professionIndex < professionList.size(); professionIndex++){
                    if(ProfileNewActivity.professionId.equals(professionList.get(professionIndex).getId())){
                        sProfession.setSelection(professionIndex);
                        break;
                    }
                }
                sProfessionTouchable = false;
                sProfession.performClick();
            }
        }        
    }


    public class RetrieveEducationList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                List<String> educationTypeList = new ArrayList<String>();
                for(Education education : educationList)
                    educationTypeList.add(education.getType());

                JonopriyoDatabase dbInstance = new JonopriyoDatabase(activity);
                dbInstance.open();
                for(Education education : educationList){
                    dbInstance.insertOrUpdateEducation(education);
                }
                dbInstance.close();

                String[] strarray = educationTypeList.toArray(new String[0]);
                generateSpinner(sEducation, strarray);

                for(int educationIndex = 0; educationIndex < educationList.size(); educationIndex++){
                    if(ProfileNewActivity.educationId.equals(educationList.get(educationIndex).getId())){
                        sEducation.setSelection(educationIndex);
                        break;
                    }
                }
                sEducationTouchable = false;
                sEducation.performClick();
            }
        }        
    }

}
