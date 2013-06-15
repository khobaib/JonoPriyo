package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Education {
    private Long id;
    private String type;
    
    public Education() {
    }

    public Education(Long id, String type) {
        this.id = id;
        this.type = type;
    }
    
    
    public static List<Education> parseEducationList(String jsonStr){
        List<Education> educationList = new ArrayList<Education>();
        
        try {
            JSONObject educationObj = new JSONObject(jsonStr);
            JSONArray eArray = educationObj.getJSONArray("education");
            int numOfE = eArray.length();
            for(int eIndex = 0; eIndex < numOfE; eIndex++){
                JSONObject thisE = eArray.getJSONObject(eIndex);
                Long id = thisE.getLong("id");
                String type = thisE.getString("type");
                Education education = new Education(id, type);
                educationList.add(education);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return educationList;
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
