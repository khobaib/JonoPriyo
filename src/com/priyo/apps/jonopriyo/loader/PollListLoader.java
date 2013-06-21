package com.priyo.apps.jonopriyo.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PollListLoader extends AsyncTaskLoader<List<Poll>> {
    
    private JsonParser jsonParser;
    private List<Poll> pollList;
    private String method;                      // get_all_polls/get_my_polls
    private String token;
    
    private List<Poll> mPolls;                  // holder to keep previous polls while copying new ones

    public PollListLoader(Context context, String method, String token) {
        super(context);
        jsonParser = new JsonParser();
        this.method = method;
        this.token = token;
    }

    @Override
    public List<Poll> loadInBackground() {
        String rootUrl = Constants.URL_ROOT;

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        urlParams.add(new BasicNameValuePair("method", method));

//        String url = Constants.URL_OFFER_GET + Constants.App_Code;
        ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET,
                rootUrl, urlParams, null, token);

        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject result = response.getjObj();
            try {
                JSONArray pollArray = result.getJSONArray("poll_list");
                
                pollList = Poll.parsePollList(pollArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }            

            return pollList;
        }

        return null;
    }
    
    
    
    
    @Override
    public void deliverResult(List<Poll> polls) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (polls != null) {
                releaseResources(polls);
                return;
            }
        }
        
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Poll> oldpolls = mPolls;
        mPolls = polls;

        if (isStarted()) {
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(polls);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldpolls != null && oldpolls != polls) {
            releaseResources(oldpolls);
        }
    }
    
    
    
    @Override
    protected void onStartLoading() {

        if (mPolls != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mPolls);
        }

        if (mPolls == null) {
            // If the current data is null... then we should make it non-null! :)
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();
    }



    @Override
    protected void onReset() {
        // Ensure the loader is stopped.
        onStopLoading();

        mPolls = null;
    }


    @Override
    public void onCanceled(List<Poll> polls) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(polls);

        // The load has been canceled, so we should release the resources
        // associated with 'mApps'.
        releaseResources(polls);
    }


    private void releaseResources(List<Poll> polls) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

}
