package com.priyo.apps.jonopriyo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {
    
    LinearLayout llThisWeekPoll, llAllPolls, llProfileSettings, llShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        llThisWeekPoll = (LinearLayout) findViewById(R.id.ll1);
        llThisWeekPoll.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ThisWeekPollActivity.class));
                
            }
        });
        
        llAllPolls = (LinearLayout) findViewById(R.id.ll2);
        llAllPolls.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllPollsActivity.class));
                
            }
        });
    }
    
}
