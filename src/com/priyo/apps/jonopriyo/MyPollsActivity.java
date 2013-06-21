package com.priyo.apps.jonopriyo;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.priyo.apps.jonopriyo.adapter.PollListAdapter;
import com.priyo.apps.jonopriyo.loader.PollListLoader;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class MyPollsActivity  extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Poll>> {

    private static final int LOADER_ID = 1;
    
    JonopriyoApplication appInstance;
    String appToken;
    
    PollListAdapter mPollListAdapter;
    
    ListView PollList;
    TextView Title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_list);
        
        Title = (TextView) findViewById(R.id.tv_title);
        Title.setText("My Polls");
        
        appInstance = (JonopriyoApplication) getApplication();
        appToken = appInstance.getAccessToken();
        
        mPollListAdapter = new PollListAdapter(MyPollsActivity.this, null);
        
        PollList = (ListView) findViewById(R.id.lv_poll_list);
        PollList.setAdapter(mPollListAdapter);
        PollList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Poll selectedPoll = (Poll) parent.getItemAtPosition(position);
                appInstance.setSelectedPoll(selectedPoll);
              
                Intent i = new Intent(MyPollsActivity.this, PollDetailsActivity.class);
                i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_MY_POLLS);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.hasInternet(MyPollsActivity.this)) {
            Log.d(">>><<", "internet available!");
            getSupportLoaderManager().initLoader(LOADER_ID, null, MyPollsActivity.this);
//            new GetOfferData().execute();
        } else {
            alert("Please check your internet connection.");
        }
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

    @Override
    public Loader<List<Poll>> onCreateLoader(int id, Bundle args) {
        return new PollListLoader(MyPollsActivity.this, Constants.METHOD_GET_MY_POLLS, appToken);
    }

    @Override
    public void onLoadFinished(Loader<List<Poll>> loader, List<Poll> pollList) {
        mPollListAdapter.setData(pollList);
        
    }

    @Override
    public void onLoaderReset(Loader<List<Poll>> loader) {
        mPollListAdapter.setData(null);
        
    }
    
    
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(MyPollsActivity.this);
        bld.setMessage(message);
        bld.setCancelable(false);
        bld.setNeutralButton("Ok", null);
        bld.create().show();
    }

}
