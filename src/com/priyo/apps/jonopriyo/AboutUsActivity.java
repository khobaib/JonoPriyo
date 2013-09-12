package com.priyo.apps.jonopriyo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_new);
        
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        
        TextView Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.about_app));
        
        TextView aboutUsBody = (TextView) findViewById(R.id.about_us_body);
        aboutUsBody.setTypeface(tf);
        aboutUsBody.setText(getResources().getString(R.string.about_us));
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
    }

}
