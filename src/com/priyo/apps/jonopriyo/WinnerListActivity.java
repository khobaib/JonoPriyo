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

import com.priyo.apps.jonopriyo.adapter.WinnerListAdapter;
import com.priyo.apps.jonopriyo.loader.PollListLoader;
import com.priyo.apps.jonopriyo.loader.WinnerListLoader;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class WinnerListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Winner>> {
    
    private static final int LOADER_ID = 1;
    
    JonopriyoApplication appInstance;
    String appToken;
    
    WinnerListAdapter mWinnerListAdapter;
    
    ListView WinnerList;
    TextView Title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner_list);
        
        appInstance = (JonopriyoApplication) getApplication();
        appToken = appInstance.getAccessToken();
        
        mWinnerListAdapter = new WinnerListAdapter(WinnerListActivity.this, null);
        
        WinnerList = (ListView) findViewById(R.id.lv_winner_list);
        WinnerList.setAdapter(mWinnerListAdapter);
        WinnerList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Poll selectedPoll = (Poll) parent.getItemAtPosition(position);
//                appInstance.setSelectedPoll(selectedPoll);
//              
//                Intent i = new Intent(AllPollsActivity.this, PollDetailsActivity.class);
//                i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_ALL_POLLS);
//                startActivity(i);
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.hasInternet(WinnerListActivity.this)) {
            Log.d(">>><<", "internet available!");
            getSupportLoaderManager().initLoader(LOADER_ID, null, WinnerListActivity.this);
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
    
    
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(WinnerListActivity.this);
        bld.setMessage(message);
        bld.setCancelable(false);
        bld.setNeutralButton("Ok", null);
        bld.create().show();
    }

    @Override
    public Loader<List<Winner>> onCreateLoader(int id, Bundle args) {
        return new WinnerListLoader(WinnerListActivity.this, appToken);
    }

    @Override
    public void onLoadFinished(Loader<List<Winner>> loader, List<Winner> winnerList) {
        mWinnerListAdapter.setData(winnerList);       
    }

    @Override
    public void onLoaderReset(Loader<List<Winner>> arg0) {
        mWinnerListAdapter.setData(null);
        
    }

}
