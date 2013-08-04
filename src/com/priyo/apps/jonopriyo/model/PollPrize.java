package com.priyo.apps.jonopriyo.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PollPrize {
    private String type;
    private Long value;
    private String imageUrl;
    
    public PollPrize() {
        // temporary
        this.type = null;
        this.value = (long) 0;
        this.imageUrl = null;
    }

    public PollPrize(String type, Long value, String imageUrl) {
        this.type = type;
        this.value = value;
        this.imageUrl = imageUrl;
    }
    
    
    public static PollPrize parsePrize(String jsonStr){
        PollPrize pollPrize= new PollPrize();
        Log.d("parsePrize", "json data = " + jsonStr);
        
        try {
//            JSONArray prizeArray = new JSONArray(jsonStr);
//            JSONObject thisPrize = prizeArray.getJSONObject(0);
            JSONObject thisPrize = new JSONObject(jsonStr);
            String pType = thisPrize.optString("prize_type");
            Long pValue = thisPrize.optLong("prize_value");
            String imageUrl= thisPrize.optString("prize_url");
            pollPrize = new PollPrize(pType, pValue, imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pollPrize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
