package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.bugsense.trace.BugSenseHandler;
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

public class MyPollsActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Poll>> {

    private static final int LOADER_ID = 1;
    
    ProgressDialog pDialog;
    JonopriyoApplication appInstance;
    String appToken;
    
    PollListAdapter mPollListAdapter;
    
    List<Poll> pollList;
    
    ListView PollList;
    TextView Title;
    
    Typeface tf;
    
    final String[] sortParams = {"জরিপের ক্রম অনুযায়ী", "জরিপের বিভাগ অনুযায়ী", "প্রকাশের তারিখ অনুযায়ী", "পুরস্কারের মূল্য অনুযায়ী"};
    Spinner sSort;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(MyPollsActivity.this, "e8ecd3f1");
        setContentView(R.layout.poll_list);
        
        pDialog = new ProgressDialog(MyPollsActivity.this);
        pDialog.setMessage("একটু অপেক্ষা করুন...");
    
        tf = Typeface.createFromAsset(getAssets(), "font/suttony.ttf");
        
        Title = (TextView) findViewById(R.id.tv_title);
        Title.setTypeface(tf);
        Title.setText(getResources().getString(R.string.my_polls));
        
        appInstance = (JonopriyoApplication) getApplication();
        appToken = appInstance.getAccessToken();
        
        mPollListAdapter = new PollListAdapter(MyPollsActivity.this, null, Constants.PARENT_ACTIVITY_MY_POLLS);
        
        PollList = (ListView) findViewById(R.id.lv_poll_list);
        PollList.setAdapter(mPollListAdapter);
        PollList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Poll selectedPoll = (Poll) parent.getItemAtPosition(position);
                appInstance.setSelectedPoll(selectedPoll);
              
                Intent i = new Intent(MyPollsActivity.this, AllPollsDetailsActivity.class);
                i.putExtra(Constants.FROM_ACTIVITY, Constants.PARENT_ACTIVITY_MY_POLLS);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        
        
        sSort = (Spinner) findViewById(R.id.s_sort);
        ArrayAdapter<String> sAdapter= new ArrayAdapter<String>(this, R.layout.my_simple_dialog_item, sortParams);
        sSort.setAdapter(new NothingSelectedSpinnerAdapter(sAdapter, R.layout.spinner_row_nothing_selected,                         
                MyPollsActivity.this));

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
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(MyPollsActivity.this);
    }
    
    public void onClickSort(View v){
        sSort.performClick();
    }
    
    public void onClickBack(View v){
        v.setVisibility(View.GONE);
        Collections.sort(pollList, new PollReleaseDateComparator());
        mPollListAdapter.setData(pollList);
    }
    
    public void showCategoryWisePollList(String category){
        List<Poll> categoryPollList = new ArrayList<Poll>();
        int numOfPolls = pollList.size();
        for(int pollIndex = 0; pollIndex < numOfPolls; pollIndex++){
            if(pollList.get(pollIndex).getCategory().equals(category))
                categoryPollList.add(pollList.get(pollIndex));
        }     
        mPollListAdapter.setData(categoryPollList);
        
        findViewById(R.id.b_back).setVisibility(View.VISIBLE);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.hasInternet(MyPollsActivity.this)) {
            Log.d(">>><<", "internet available!");
            if(!pDialog.isShowing())
                pDialog.show();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, MyPollsActivity.this);
//            new GetOfferData().execute();
        } else {
            alert("অনুগ্রহপূর্বক আপনার ইন্টারনেট কানেকসন চেক করুন");
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            BugSenseHandler.closeSession(MyPollsActivity.this);
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
        this.pollList = pollList;
        Collections.sort(pollList, new PollReleaseDateComparator());
        mPollListAdapter.setData(pollList);
        if(pDialog.isShowing())
            pDialog.dismiss();  
        
    }

    @Override
    public void onLoaderReset(Loader<List<Poll>> loader) {
        mPollListAdapter.setData(null);
        
    }
    
    
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(MyPollsActivity.this);
        bld.setMessage(message);
        bld.setCancelable(false);
        bld.setNeutralButton("ঠিক আছে", null);
        bld.create().show();
    }

}
