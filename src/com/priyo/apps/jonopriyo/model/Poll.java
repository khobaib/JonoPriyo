package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poll {
    private Long id;
    private Long number;
    private String question;
    private Boolean isNew;
    private Long myAnsId;
    private List<PollAnswer> answers;
    
    public Poll() {       
    }

//    public Poll(Long id, Long number, String question, Boolean isNew, List<PollAnswer> answers) {
//        this.id = id;
//        this.number = number;
//        this.question = question;
//        this.isNew = isNew;
//        this.myAnsId = (long) -1;
//        this.answers = answers;
//    }
    
    public Poll(Long id, Long number, String question, Boolean isNew, Long myAnsId, List<PollAnswer> answers) {
        this.id = id;
        this.number = number;
        this.question = question;
        this.isNew = isNew;
        this.myAnsId = myAnsId;
        this.answers = answers;
    }
    
//    public static Poll parsePoll(String jsonStr){
//        Poll poll = null;
//        
//        try {
//            JSONObject pollObj = new JSONObject(jsonStr);
//            Long id = pollObj.getLong("poll_id");
//            Long number = pollObj.getLong("poll_number");
//            String question = pollObj.getString("poll_question");
//            Boolean isNew = pollObj.getBoolean("is_new");
//            
//            Long myAnsId = null;
//            if(pollObj.has("my_answer_id"))
//                myAnsId = pollObj.getLong("my_answer_id");
//            
//            JSONArray ansArray = pollObj.getJSONArray("poll_answer");
//            List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());
//            
//            poll = new Poll(id, number, question, isNew, myAnsId, answers);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return poll;
//    }
    
    
    public static List<Poll> parsePollList(String jsonStr){
        List<Poll> pollList = new ArrayList<Poll>();
        
        try {
            JSONArray pollArray = new JSONArray(jsonStr);
            int numOfPoll = pollArray.length();
            for(int pollIndex = 0; pollIndex < numOfPoll; pollIndex++){
                JSONObject thisPoll = pollArray.getJSONObject(pollIndex);
                Long id = thisPoll.getLong("poll_id");
                Long number = thisPoll.getLong("poll_number");
                String question = thisPoll.getString("poll_question");
                
                Boolean isNew = null;
                if(thisPoll.has("is_new"))
                    isNew = thisPoll.getBoolean("is_new");
                
                Long myAnsId = null;
                if(thisPoll.has("my_answer_id"))
                    myAnsId = thisPoll.getLong("my_answer_id");
                
                JSONArray ansArray = thisPoll.getJSONArray("poll_answer");
                List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());
                
                Poll poll = new Poll(id, number, question, isNew, myAnsId, answers);   
                pollList.add(poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
                
        return pollList;
    }
    
    
//    public static Poll parseMyPolls(String jsonStr){
//        Poll poll = null;
//        
//        try {
//            JSONObject pollObj = new JSONObject(jsonStr);
//            Long id = pollObj.getLong("poll_id");
//            Long number = pollObj.getLong("poll_number");
//            String question = pollObj.getString("poll_question");
//            Boolean isNew = pollObj.getBoolean("is_new");
//            Long myAnsId = pollObj.getLong("my_answer_id");
//            
//            JSONArray ansArray = pollObj.getJSONArray("poll_answer");
//            List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());
//            
//            poll = new Poll(id, number, question, isNew, myAnsId, answers);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return poll;
//    }
    
    
    

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
    
    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Long getMyAnsId() {
        return myAnsId;
    }

    public void setMyAnsId(Long myAnsId) {
        this.myAnsId = myAnsId;
    }

    public List<PollAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PollAnswer> answers) {
        this.answers = answers;
    }   
}
