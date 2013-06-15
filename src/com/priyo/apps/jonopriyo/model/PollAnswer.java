package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PollAnswer {
    private Long id;
    private String answer;
    
    public PollAnswer() {
    }

    public PollAnswer(Long id, String answer) {
        this.id = id;
        this.answer = answer;
    }
    
    public static List<PollAnswer> paresePollAnswerList(String jsonStr){
        List<PollAnswer> ansList = new ArrayList<PollAnswer>();
        
        try {
            JSONArray ansArray = new JSONArray(jsonStr);
            int numOfAns = ansArray.length();
            for(int ansIndex = 0; ansIndex < numOfAns; ansIndex++){
                JSONObject thisAns = ansArray.getJSONObject(ansIndex);
                Long id = thisAns.getLong("id");
                String ans = thisAns.getString("poll_answer");
                PollAnswer answer = new PollAnswer(id, ans);
                ansList.add(answer);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ansList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
   
}
