package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Winner {
    private Long pollId;
    private Long pollNumber;
    private String pollQuestion;
    private String winnerName;
    private String winnerPicUrl;
    
    public Winner() {
    }

    public Winner(Long pollId, Long pollNumber, String pollQuestion, String winnerName, String winnerPicUrl) {
        this.pollId = pollId;
        this.pollNumber = pollNumber;
        this.pollQuestion = pollQuestion;
        this.winnerName = winnerName;
        this.winnerPicUrl = winnerPicUrl;
    }
    
    
    public static List<Winner> parseWinnerList(String jsonStr){
        List<Winner> winnerList = new ArrayList<Winner>();
        
        try {
            JSONArray winnerArray = new JSONArray(jsonStr);
            int numOfWinner = winnerArray.length();
            for(int winnerIndex = 0; winnerIndex < numOfWinner; winnerIndex++){
                JSONObject thisWinner = winnerArray.getJSONObject(winnerIndex);
                
                Long pollId = thisWinner.getLong("poll_id");
                Long pollNumber = thisWinner.getLong("poll_number");
                String pollQuestion = thisWinner.getString("poll_question");
                String winnerName = thisWinner.getString("winner_name");
                String winnerPicUrl = thisWinner.getString("winner_image_url");
                Winner winner = new Winner(pollId, pollNumber, pollQuestion, winnerName, winnerPicUrl);
                winnerList.add(winner);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return winnerList;        
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getPollNumber() {
        return pollNumber;
    }

    public void setPollNumber(Long pollNumber) {
        this.pollNumber = pollNumber;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerPicUrl() {
        return winnerPicUrl;
    }

    public void setWinnerPicUrl(String winnerPicUrl) {
        this.winnerPicUrl = winnerPicUrl;
    }

}
