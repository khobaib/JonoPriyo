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
    private Long winnerUserId;
    private String winnerName;
    private String country;
    private String city;
    private String area;
    private String prizeType;
    private Long prizeValue;
    private String winnerPicUrl;
    
    public Winner() {
    }

    public Winner(Long pollId, Long pollNumber, String pollQuestion, Long winnerUserId, String winnerName,
            String country, String city, String area, String prizeType, Long prizeValue, String winnerPicUrl) {
        super();
        this.pollId = pollId;
        this.pollNumber = pollNumber;
        this.pollQuestion = pollQuestion;
        this.winnerUserId = winnerUserId;
        this.winnerName = winnerName;
        this.country = country;
        this.city = city;
        this.area = area;
        this.prizeType = prizeType;
        this.prizeValue = prizeValue;
        this.winnerPicUrl = winnerPicUrl;
    }

    public static Winner parseWinner(JSONObject winnerObj){
        Winner winner = new Winner();
        
        try {
            Long pollId = winnerObj.getLong("poll_id");
            Long pollNumber = winnerObj.getLong("poll_number");
            String pollQuestion = winnerObj.getString("poll_question");
            Long winnerUserId = winnerObj.getLong("winner_user_id");
            String winnerName = winnerObj.getString("winner_name");
            String country = winnerObj.getString("country");
            String city = winnerObj.getString("city");
            String area = winnerObj.getString("area");
            String prizeType = winnerObj.getString("prize_type");
            Long prizeValue = winnerObj.getLong("prize_value");
            String winnerPicUrl = winnerObj.getString("winner_image_url");
            
            winner = new Winner(pollId, pollNumber, pollQuestion, winnerUserId, winnerName, country,
                    city, area, prizeType, prizeValue, winnerPicUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        
        return winner;
    }



    public static List<Winner> parseWinnerList(String jsonStr){
        List<Winner> winnerList = new ArrayList<Winner>();
        
        try {
            JSONArray winnerArray = new JSONArray(jsonStr);
            int numOfWinner = winnerArray.length();
            for(int winnerIndex = 0; winnerIndex < numOfWinner; winnerIndex++){
                JSONObject thisWinner = winnerArray.getJSONObject(winnerIndex);
                Winner winner = parseWinner(thisWinner);
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

    public Long getWinnerUserId() {
        return winnerUserId;
    }


    public void setWinnerUserId(Long winnerUserId) {
        this.winnerUserId = winnerUserId;
    }


    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public Long getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(Long prizeValue) {
        this.prizeValue = prizeValue;
    }

    public String getWinnerPicUrl() {
        return winnerPicUrl;
    }

    public void setWinnerPicUrl(String winnerPicUrl) {
        this.winnerPicUrl = winnerPicUrl;
    }
    
    public String getAddress(){
        return area + ", " + city + ", " + country;
    }

}
