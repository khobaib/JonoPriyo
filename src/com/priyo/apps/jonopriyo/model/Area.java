package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Area {
    private Long id;
    private String name;
    
    public Area() {
    }

    public Area(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public static List<Area> parseAreaList(String jsonStr){
        List<Area> aList = new ArrayList<Area>();
        
        try {
            JSONObject areaObj = new JSONObject(jsonStr);
            JSONArray aArray = areaObj.getJSONArray("area");
            int numOfA = aArray.length();
            for(int aIndex = 0; aIndex < numOfA; aIndex++){
                JSONObject thisA = aArray.getJSONObject(aIndex);
                Long id = thisA.getLong("id");
                String name = thisA.getString("name");
                Area area = new Area(id, name);
                aList.add(area);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }            

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
