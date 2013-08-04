package com.priyo.apps.jonopriyo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Poll {
    private Long id;
    private Long number;
    private String question;
    private String imageUrl;
    private String category;
    private Boolean isNew;
    private Long myAnsId;
    private String releaseDate;
    private String expiryDate;
    private Long participationCount;
    private List<PollAnswer> answers;
    private Boolean isCastByMe;
    private String description;
    private PollPrize pollPrize;

    public Poll() {       
    }

    public Poll(Long id, Long number, String question, String imageUrl, String category,
            Boolean isNew, Long myAnsId, String releaseDate, String expiryDate, 
            Long participationCount, List<PollAnswer> answers, Boolean isCastByMe, String description, PollPrize pollPrize) {
        this.id = id;
        this.number = number;
        this.question = question;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isNew = isNew;
        this.myAnsId = myAnsId;
        this.releaseDate = releaseDate;
        this.expiryDate = expiryDate;
        this.participationCount = participationCount;
        this.answers = answers;
        this.isCastByMe = isCastByMe;
        this.description = description;
        this.pollPrize = pollPrize;
    }

    public static Poll parsePoll(JSONObject pollObj){
        Poll poll = null;
        try {
            Log.d("pollObj", "pollObj = " + pollObj.toString());
            Long id = pollObj.getLong("poll_id");
            Long number = pollObj.getLong("poll_number");
            String question = pollObj.getString("poll_question");
            String imageUrl = pollObj.getString("poll_image_url");
            String category = pollObj.getString("poll_category");
            Long participationCount = pollObj.getLong("poll_cast_count");
            String releaseDate = pollObj.getString("release_date");
            String expiryDate = pollObj.getString("expiry_date");


            Boolean isNew = true;               // only new-polls don't have this field, in which case - is_new = true
            if(pollObj.has("is_new"))
                isNew = pollObj.getBoolean("is_new");

            Long myAnsId = pollObj.optLong("my_answer_id");

            JSONArray ansArray = pollObj.getJSONArray("poll_answer");
            List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());

            Boolean isCastByme = true;          // only my-polls don't have this field, in which case, is_case_by_me = true
            if(pollObj.has("is_cast_by_me"))
                isCastByme = pollObj.getBoolean("is_cast_by_me");


            String description = pollObj.optString("poll_description");

            //                JSONArray prizeObj = thisPoll.optJSONArray("poll_prize");
            JSONObject prizeObj = pollObj.optJSONObject("poll_prize");
            //            Log.d("prizeObj", "prizeObj = " + prizeObj.toString());
            //                Log.d("PRIZE", "prize obj = " + prizeObj.toString());

            PollPrize pollPrize= new PollPrize();
            if(prizeObj != null)
                pollPrize = PollPrize.parsePrize(prizeObj.toString());

            poll = new Poll(id, number, question, imageUrl, category, isNew, myAnsId,
                    releaseDate, expiryDate, participationCount, answers, isCastByme, description, pollPrize); 
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return poll;

    }


    public static List<Poll> parsePollList(String jsonStr){
        List<Poll> pollList = new ArrayList<Poll>();

        try {
            JSONArray pollArray = new JSONArray(jsonStr);
            int numOfPoll = pollArray.length();
            for(int pollIndex = 0; pollIndex < numOfPoll; pollIndex++){
                JSONObject thisPoll = pollArray.getJSONObject(pollIndex);
                Poll poll = parsePoll(thisPoll);
                //                Long id = thisPoll.getLong("poll_id");
                //                Long number = thisPoll.getLong("poll_number");
                //                String question = thisPoll.getString("poll_question");
                //                String imageUrl = thisPoll.getString("poll_image_url");
                //                String category = thisPoll.getString("poll_category");
                //                Long participationCount = thisPoll.getLong("poll_cast_count");
                //                String releaseDate = thisPoll.getString("release_date");
                //                String expiryDate = thisPoll.getString("expiry_date");
                //
                //
                //                Boolean isNew = thisPoll.optBoolean("is_new");
                //
                //                Long myAnsId = thisPoll.optLong("my_answer_id");
                //
                //                JSONArray ansArray = thisPoll.getJSONArray("poll_answer");
                //                List<PollAnswer> answers = PollAnswer.paresePollAnswerList(ansArray.toString());
                //
                //                Boolean isCastByme = thisPoll.optBoolean("is_cast_by_me");
                //                String description = thisPoll.optString("poll_description");
                //
                //                //                JSONArray prizeObj = thisPoll.optJSONArray("poll_prize");
                //                JSONObject prizeObj = thisPoll.optJSONObject("poll_prize");
                //                //                Log.d("PRIZE", "prize obj = " + prizeObj.toString());
                //
                //                //                PollPrize pollPrize= new PollPrize();
                //                //                if(prizeObj != null)
                //                PollPrize pollPrize = PollPrize.parsePrize(prizeObj.toString());
                //
                //                Poll poll = new Poll(id, number, question, imageUrl, category, isNew, myAnsId,
                //                        releaseDate, expiryDate, participationCount, answers, isCastByme, description, pollPrize);   
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getParticipationCount() {
        return participationCount;
    }

    public void setParticipationCount(Long participationCount) {
        this.participationCount = participationCount;
    }

    public List<PollAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PollAnswer> answers) {
        this.answers = answers;
    }

    public Boolean getIsCastByMe() {
        return isCastByMe;
    }

    public void setIsCastByMe(Boolean isCastByMe) {
        this.isCastByMe = isCastByMe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PollPrize getPollPrize() {
        return pollPrize;
    }

    public void setPollPrize(PollPrize pollPrize) {
        this.pollPrize = pollPrize;
    }       
}
