package com.priyo.apps.jonopriyo;

import java.util.Collections;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.adapter.NothingSelectedSpinnerAdapter;
import com.priyo.apps.jonopriyo.adapter.PollListAdapter;
import com.priyo.apps.jonopriyo.loader.PollListLoader;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.jonopriyo.utility.PollCategoryComparator;
import com.priyo.apps.jonopriyo.utility.PollNumberComparator;
import com.priyo.apps.jonopriyo.utility.PollPrizeValueComparator;
import com.priyo.apps.jonopriyo.utility.PollReleaseDateComparator;
import com.priyo.apps.jonopriyo.utility.Utility;

public class NewPollsActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Poll>> {

    private static final int LOADER_ID = 1;

    JonopriyoApplication appInstance;
    String appToken;

    PollListAdapter mPollListAdapter;

    List<Poll> pollList;

    ListView PollList;
    TextView Title;

    final String[] sortParams = {"sort by Poll Number", "sort by Category", "sort by Release Date", "sort by prize value"};
    Spinner sSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_list);

        Title = (TextView) findViewById(R.id.tv_title);
        Title.setText("New Polls");

        appInstance = (JonopriyoApplication) getApplication();
        appToken = appInstance.getAccessToken();

        mPollListAdapter = new PollListAdapter(NewPollsActivity.this, null);

        PollList = (ListView) findViewById(R.id.lv_poll_list);
        PollList.setAdapter(mPollListAdapter);
        PollList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Poll selectedPoll = (Poll) parent.getItemAtPosition(position);
                appInstance.setSelectedPoll(selectedPoll);
                Log.d("<<<<<<>>>>>>>>", "poll number = " + selectedPoll.getNumber());

                Intent i = null;
                if(selectedPoll.getIsCastByMe()){
                    i = new Intent(NewPollsActivity.this, PastPollDetailsActivity.class);
                    i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_MY_POLLS);
                }
                else{
                    i = new Intent(NewPollsActivity.this, PollDetailsActivity.class);
                    //                    i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_NEW_POLLS);
                }
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        sSort = (Spinner) findViewById(R.id.s_sort);
        ArrayAdapter<String> sAdapter= new ArrayAdapter<String>(this, R.layout.my_simple_dialog_item, sortParams);
        sSort.setAdapter(new NothingSelectedSpinnerAdapter(sAdapter, R.layout.spinner_row_nothing_selected,                         
                NewPollsActivity.this));

        sSort.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                switch(pos){
                    case 1:
                        Collections.sort(pollList, new PollNumberComparator());
                        mPollListAdapter.setData(pollList);
                        break;
                    case 2:
                        Collections.sort(pollList, new PollCategoryComparator());
                        mPollListAdapter.setData(pollList);
                        break;
                    case 3:
                        Collections.sort(pollList, new PollReleaseDateComparator());
                        mPollListAdapter.setData(pollList);
                        break;
                    case 4:
                        Collections.sort(pollList, new PollPrizeValueComparator());
                        mPollListAdapter.setData(pollList);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    public void onClickSort(View v){
        sSort.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.hasInternet(NewPollsActivity.this)) {
            Log.d(">>><<", "internet available!");
            getSupportLoaderManager().initLoader(LOADER_ID, null, NewPollsActivity.this);
            //            new GetOfferData().execute();
        } else {
            alert("Please check your internet connection.");
        }
    }

    @Override
    public Loader<List<Poll>> onCreateLoader(int id, Bundle args) {
        return new PollListLoader(NewPollsActivity.this, Constants.METHOD_GET_NEW_POLLS, appToken);
    }

    @Override
    public void onLoadFinished(Loader<List<Poll>> loader, List<Poll> pollList) {
        this.pollList = pollList;
        Collections.sort(pollList, new PollNumberComparator());
        mPollListAdapter.setData(pollList);

    }

    @Override
    public void onLoaderReset(Loader<List<Poll>> loader) {
        mPollListAdapter.setData(null);

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
        AlertDialog.Builder bld = new AlertDialog.Builder(NewPollsActivity.this);
        bld.setMessage(message);
        bld.setCancelable(false);
        bld.setNeutralButton("Ok", null);
        bld.create().show();
    }

}
