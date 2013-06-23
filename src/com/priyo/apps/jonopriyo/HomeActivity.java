package com.priyo.apps.jonopriyo;

import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.lazylist.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


// 06-23 11:35:58.002: D/JsonParser(13431): sb = {"error":"invalid verb"} after 24 minute

public class HomeActivity extends Activity {

    JonopriyoApplication appInstance;
    ImageLoader imageLoader;

    ImageView ProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);   

        appInstance = (JonopriyoApplication) getApplication();
        imageLoader = new ImageLoader(HomeActivity.this);

        ProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
    }


    @Override
    protected void onResume() {
        super.onResume();

        String imageUrl = appInstance.getProfileImageUrl();
        Log.d(".......>>>>>", "image url = " + imageUrl);
        if(imageUrl != null && !imageUrl.equals("")){
            imageLoader.DisplayImage(imageUrl, ProfilePic);
//            Bitmap bMap = imageLoader.getBitmap(imageUrl, 400);
//            ProfilePic.setImageBitmap(bMap);
        }
    }


    public void onClickNewPolls(View v){
        startActivity(new Intent(HomeActivity.this, NewPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void onClickAllPolls(View v){
        startActivity(new Intent(HomeActivity.this, AllPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickMyPolls(View v){
        startActivity(new Intent(HomeActivity.this, MyPollsActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickWinners(View v){
        startActivity(new Intent(HomeActivity.this, WinnerListActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickProfile(View v){
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickShare(View v){

    }

    public void onClickProfilePic(View v){
        startActivity(new Intent(HomeActivity.this, UploadPicActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
