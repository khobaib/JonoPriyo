package com.priyo.apps.jonopriyo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.ProfileNewActivity;
import com.priyo.apps.jonopriyo.R;
import com.priyo.apps.jonopriyo.model.Area;
import com.priyo.apps.jonopriyo.model.City;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;

public class ProfileAddressFragment extends Fragment {

    Activity activity;

    public static TextView tvCountry;
    public static AutoCompleteTextView tvCity, tvArea;
    public static EditText etAddress;

    public static List<City> cityList;
    public static List<Area> areaList;

    JsonParser jsonParser;


    public ProfileAddressFragment() {
    }

    public static ProfileAddressFragment newInstance() {
        final ProfileAddressFragment fragment = new ProfileAddressFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile_address, container, false);

        tvCountry = (TextView) view.findViewById(R.id.tv_country);

        tvCity = (AutoCompleteTextView) view.findViewById(R.id.et_city);
        tvCity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                ProfileNewActivity.selectedCityId = (long) 0;
                for(City city : cityList){
                    if(selection.equals(city.getName())){
                        ProfileNewActivity.selectedCityId = city.getId();
                        break;
                    }
                }
                new RetrieveAreaList().execute(ProfileNewActivity.selectedCityId);

            }
        });

        tvArea = (AutoCompleteTextView) view.findViewById(R.id.et_area);
        tvArea.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                ProfileNewActivity.selectedAreaId = (long) 0;
                for(Area area : areaList){
                    if(selection.equals(area.getName())){
                        ProfileNewActivity.selectedAreaId = area.getId();
                        break;
                    }
                }

            }
        });


        etAddress = (EditText) view.findViewById(R.id.et_address);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {

            jsonParser = new JsonParser();
            //            new RetrieveCityList().execute(ProfileNewActivity.selectedCountryId);
            //            new RetrieveAreaList().execute(ProfileNewActivity.selectedCityId);
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
                areaList = Area.parseAreaList(response.getjObj().toString());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                List<String> areaNameList = new ArrayList<String>();
                for(Area area : areaList)
                    areaNameList.add(area.getName());

                ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(activity,
                        R.layout.my_autocomplete_text_style, areaNameList);
                ProfileAddressFragment.tvArea.setAdapter(areaAdapter);
                
                ProfileAddressFragment.tvArea.setText("");
                ProfileNewActivity.selectedAreaId = (long) -1;

//                for(int areaIndex = 0; areaIndex < areaList.size(); areaIndex++){
//                    if(ProfileNewActivity.selectedAreaId.equals(areaList.get(areaIndex).getId())){
//                        ProfileAddressFragment.tvArea.setText(areaList.get(areaIndex).getName());
//                        break;
//                    }
//                }
            }
        }        
    }

}
