package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.adapter.WinnerListAdapter;
import com.priyo.apps.jonopriyo.loader.WinnerListLoader;
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.Utility;

public class WinnerListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Winner>> {

    private static final int LOADER_ID = 1;

    JonopriyoApplication appInstance;
    private ProgressDialog pDialog;
    String appToken;
    long userId;

    List<Winner> winnerList;

    int flagWinnerType;

    WinnerListAdapter mWinnerListAdapter;

    ListView WinnerList;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner_list);

        appInstance = (JonopriyoApplication) getApplication();
        appToken = appInstance.getAccessToken();
        userId = appInstance.getUserId();
        flagWinnerType = Constants.FLAG_WINNER_ALL;
        winnerList = null;

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

    public void onClickWinner(View v){
        if(((Button) v).getText().equals("My Wins")){
            ((Button) v).setText("All Wins");
            flagWinnerType = Constants.FLAG_WINNER_ONLY_ME;
            if(winnerList != null){
                showMyWinList();                
            }
        }
        else{
            ((Button) v).setText("My Wins");
            flagWinnerType = Constants.FLAG_WINNER_ALL;
            mWinnerListAdapter.setData(winnerList);
        }

    }

    private void showMyWinList() {
        List<Winner> myWinList = new ArrayList<Winner>();
        int numOfWinners = winnerList.size();
        for(int winnerIndex = 0; winnerIndex < numOfWinners; winnerIndex++){
            if(winnerList.get(winnerIndex).getWinnerUserId().equals(userId))
                myWinList.add(winnerList.get(winnerIndex));
        }
        mWinnerListAdapter.setData(myWinList);
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
        pDialog = new ProgressDialog(WinnerListActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        return new WinnerListLoader(WinnerListActivity.this, appToken);
    }

    @Override
    public void onLoadFinished(Loader<List<Winner>> loader, List<Winner> winnerList) {
        this.winnerList = winnerList;
        mWinnerListAdapter.setData(winnerList);  
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<List<Winner>> arg0) {
        mWinnerListAdapter.setData(null);

    }

}
