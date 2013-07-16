package com.priyo.apps.jonopriyo.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;

public class WinnerListLoader extends AsyncTaskLoader<List<Winner>> {
    
    private JsonParser jsonParser;
    private List<Winner> winnerList;
    private String token;
    
    private List<Winner> mWinners;

    public WinnerListLoader(Context context, String token) {
        super(context);
        jsonParser = new JsonParser();
        this.token = token;
    }

    @Override
    public List<Winner> loadInBackground() {
        String rootUrl = Constants.URL_ROOT;

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        urlParams.add(new BasicNameValuePair("method", Constants.METHOD_GET_WINNERS));

//        String url = Constants.URL_OFFER_GET + Constants.App_Code;
        ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET,
                rootUrl, urlParams, null, token);

        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject result = response.getjObj();
            try {
                JSONArray winnerArray = result.getJSONArray("winner_list");
                
                winnerList = Winner.parseWinnerList(winnerArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }            

            return winnerList;
        }
        return null;
    }
    

    @Override
    public void deliverResult(List<Winner> winners) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (winners != null) {
                releaseResources(winners);
                return;
            }
        }
        
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Winner> oldWinners = mWinners;
        mWinners = winners;

        if (isStarted()) {
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(winners);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldWinners != null && oldWinners != winners) {
            releaseResources(oldWinners);
        }
    }
    
    
    
    @Override
    protected void onStartLoading() {

        if (mWinners != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mWinners);
        }

        if (mWinners == null) {
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

        mWinners = null;
    }


    @Override
    public void onCanceled(List<Winner> winners) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(winners);

        // The load has been canceled, so we should release the resources
        // associated with 'mApps'.
        releaseResources(winners);
    }


    private void releaseResources(List<Winner> winners) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }


}
