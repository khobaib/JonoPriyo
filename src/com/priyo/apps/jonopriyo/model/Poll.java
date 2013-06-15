package com.priyo.apps.jonopriyo.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poll {
    private Long id;
    private Long number;
    private String question;
    private List<PollAnswer> answers;
    
    public Poll() {
        // TODO Auto-generated constructor stub
    }

    public Poll(Long id, Long number, String question, List<PollAnswer> answers) {
        this.id = id;
        this.number = number;
        this.question = question;
        this.answers = answers;
    }
    
    public static Poll parsePoll(String jsonStr){
        Poll poll = null;
        
        try {
            JSONObject pollObj = new JSONObject(jsonStr);
            Long id = pollObj.getLong("poll_id");
            Long number = pollObj.getLong("poll_number");
            String question = pollObj.getString("poll_question");
            
            JSONArray ansArray = pollObj.getJSONArray("poll_answer");
            List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());
            
            poll = new Poll(id, number, question, answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poll;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<PollAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PollAnswer> answers) {
        this.answers = answers;
    }
    
    
    
    
}
