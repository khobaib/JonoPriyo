package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PollResult {
    private Long answerId;
    private String answer;
    private Long castCount;
    
    public PollResult() {
    }

    public PollResult(Long answerId, String answer, Long castCount) {
        this.answerId = answerId;
        this.answer = answer;
        this.castCount = castCount;
    }
    
    
    public static List<PollResult> parsePollResultList(String jsonStr){
        List<PollResult> resultList = new ArrayList<PollResult>();
        
        try {
            JSONArray resultArray = new JSONArray(jsonStr);
            int numOfResult = resultArray.length();
            for(int resultIndex = 0; resultIndex < numOfResult; resultIndex++){
                JSONObject thisResult = resultArray.getJSONObject(resultIndex);
                Long answerId = thisResult.getLong("poll_answer_id");
                String answer = thisResult.getString("poll_answer");
                Long castCount = thisResult.getLong("result_count");
                
                PollResult result = new PollResult(answerId, answer, castCount);
                resultList.add(result);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return resultList;
    }
    

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getCastCount() {
        return castCount;
    }

    public void setCastCount(Long castCount) {
        this.castCount = castCount;
    }

}
