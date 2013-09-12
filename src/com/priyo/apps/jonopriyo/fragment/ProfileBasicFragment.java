package com.priyo.apps.jonopriyo.fragment;

import android.app.Activity;
import android.graphics.Typeface;
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
    
    TextView NameTitle, EmailTitle, PhoneTitle, BasicInfoTitle;
    
    Typeface tf;
    
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
        
        NameTitle = (TextView) view.findViewById(R.id.tv_name);
        EmailTitle = (TextView) view.findViewById(R.id.tv_email_title);
        PhoneTitle = (TextView) view.findViewById(R.id.tv_phone);
        BasicInfoTitle = (TextView) view.findViewById(R.id.tv_title);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        if (this.activity != null) {
            tf = Typeface.createFromAsset(this.activity.getAssets(), "font/suttony.ttf");
            NameTitle.setTypeface(tf);
            EmailTitle.setTypeface(tf);
            PhoneTitle.setTypeface(tf);
            BasicInfoTitle.setTypeface(tf);
            
            NameTitle.setText(getResources().getString(R.string.name));
            EmailTitle.setText(getResources().getString(R.string.email));
            PhoneTitle.setText(getResources().getString(R.string.phone));
            BasicInfoTitle.setText(getResources().getString(R.string.basic_info));
            
            if(ProfileNewActivity.regInfo != null)
                tvEmail.setText(ProfileNewActivity.regInfo.getEmail());
        }
    }

}
