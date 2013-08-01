package com.priyo.apps.jonopriyo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.ProfileNewActivity;
import com.priyo.apps.jonopriyo.R;

public class ProfileBasicFragment extends Fragment {

    Activity activity;
    public static EditText etName, etPhone;
    public static TextView tvEmail;
    
    public ProfileBasicFragment() {
    }

    public static ProfileBasicFragment newInstance() {
        final ProfileBasicFragment fragment = new ProfileBasicFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile_basic, container, false);
        
        etName = (EditText) view.findViewById(R.id.et_name);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {
            if(ProfileNewActivity.regInfo != null)
                tvEmail.setText(ProfileNewActivity.regInfo.getEmail());
        }
    }

}
