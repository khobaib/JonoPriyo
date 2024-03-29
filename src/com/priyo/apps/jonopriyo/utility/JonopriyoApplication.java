package com.priyo.apps.jonopriyo.utility;

import com.priyo.apps.jonopriyo.model.Poll;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class JonopriyoApplication extends Application {
    
    private static Context context;
    protected SharedPreferences User;
    
    private Poll selectedPoll;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        User = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        selectedPoll = null;
    }
    
    
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return context;        
    }
    
   
    public Poll getSelectedPoll() {
        return selectedPoll;
    }


    public void setSelectedPoll(Poll selectedPoll) {
        this.selectedPoll = selectedPoll;
    }


    public void setFirstTime(Boolean firstTimeFlag){
        Editor editor = User.edit();
        editor.putBoolean(Constants.FIRST_TIME, firstTimeFlag);
        editor.commit();        
    }
    
    public void setFirstTimeLoggedIn(Boolean firstTimeFlag){
        Editor editor = User.edit();
        editor.putBoolean(Constants.FIRST_TIME_LOGGED_IN, firstTimeFlag);
        editor.commit();        
    }
    
    public void setCredentials(String email, String password){
        Editor editor = User.edit();
        editor.putString(Constants.EMAIL, email);
        editor.putString(Constants.PASSWORD, password);
        editor.commit();
    }
    
    public void setRememberMe(Boolean rememberMeFlag){
        Editor editor = User.edit();
        editor.putBoolean(Constants.REMEMBER_ME, rememberMeFlag);
        editor.commit();        
    }
    
    public void setUserId(Long userId){
        Editor editor = User.edit();
        editor.putLong(Constants.USER_ID, userId);
        editor.commit();        
    }
    
    public void setAccessToken(String token){
        Editor editor = User.edit();
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.commit();        
    }
    
    public void setProfileImageUrl(String imageUrl){
        Editor editor = User.edit();
        editor.putString(Constants.PROFILE_PIC_URL, imageUrl);
        editor.commit();        
    }
    
    

    public boolean isFirstTime(){
        Boolean firstTimeFlag = User.getBoolean(Constants.FIRST_TIME, true);
        return firstTimeFlag;
    }
    
    public boolean isFirstTimeLoggedIn(){
        Boolean firstTimeFlag = User.getBoolean(Constants.FIRST_TIME_LOGGED_IN, true);
        return firstTimeFlag;
    }
    
    public String getEmail(){
        String email = User.getString(Constants.EMAIL, null);
        return email;
    }
    
    public String getPassword(){
        String pass = User.getString(Constants.PASSWORD, null);
        return pass;
    }
    
    public boolean isRememberMe(){
        Boolean rememberMeFlag = User.getBoolean(Constants.REMEMBER_ME, false);
        return rememberMeFlag;
    }
    
    public Long getUserId(){
        Long userId = User.getLong(Constants.USER_ID, 0);
        return userId;
    }
    
    public String getAccessToken(){
        String token = User.getString(Constants.ACCESS_TOKEN, null);
        return token;
    }
    
    public String getProfileImageUrl(){
        String imageUrl = User.getString(Constants.PROFILE_PIC_URL, null);
        return imageUrl;
    }

}
