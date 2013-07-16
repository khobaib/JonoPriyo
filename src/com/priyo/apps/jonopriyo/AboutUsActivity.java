package com.priyo.apps.jonopriyo;

import android.app.Activity;
import android.os.Bundle;

public class AboutUsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.prev_slide_in, R.anim.prev_slide_out);
    }

}
