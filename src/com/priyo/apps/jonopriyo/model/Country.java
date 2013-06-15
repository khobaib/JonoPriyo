package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Country {
    private Long id;
    private String name;
    
    public Country() {
    }

    public Country(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public static List<Country> parseCountryList(String jsonStr){
        List<Country> cList = new ArrayList<Country>();
        
        try {
            JSONObject countryObj = new JSONObject(jsonStr);
            JSONArray cArray = countryObj.getJSONArray("country");
            int numofC = cArray.length();
            for(int cIndex = 0; cIndex < numofC; cIndex++){
                JSONObject thisC = cArray.getJSONObject(cIndex);
                Long id = thisC.getLong("id");
                String name = thisC.getString("name");
                Country country = new Country(id, name);
                cList.add(country);
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
