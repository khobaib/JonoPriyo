package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profession {
    private Long id;
    private String type;
    
    public Profession() {
    }

    public Profession(Long id, String type) {
        this.id = id;
        this.type = type;
    }
    
    
    public static List<Profession> parseprofessionList(String jsonStr){
        List<Profession> professionList = new ArrayList<Profession>();
        
        try {
            JSONObject professionObj = new JSONObject(jsonStr);
            JSONArray pArray = professionObj.getJSONArray("profession");
            int numOfE = pArray.length();
            for(int pIndex = 0; pIndex < numOfE; pIndex++){
                JSONObject thisE = pArray.getJSONObject(pIndex);
                Long id = thisE.getLong("id");
                String type = thisE.getString("type");
                Profession profession = new Profession(id, type);
                professionList.add(profession);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return professionList;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
