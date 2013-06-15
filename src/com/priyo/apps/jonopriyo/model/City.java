package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class City {
    private Long id;
    private String name;
    
    public City() {
    }

    public City(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public static List<City> parseCityList(String jsonStr){
        List<City> cList = new ArrayList<City>();
        
        try {
            JSONObject cityObj = new JSONObject(jsonStr);
            JSONArray cArray = cityObj.getJSONArray("city");
            int numofC = cArray.length();
            for(int cIndex = 0; cIndex < numofC; cIndex++){
                JSONObject thisC = cArray.getJSONObject(cIndex);
                Long id = thisC.getLong("id");
                String name = thisC.getString("name");
                City city = new City(id, name);
                cList.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cList;
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
