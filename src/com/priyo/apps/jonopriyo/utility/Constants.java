package com.priyo.apps.jonopriyo.utility;

import java.io.File;

import android.os.Environment;

public class Constants {
    
    public static final int REQUEST_TYPE_GET = 1;
    public static final int REQUEST_TYPE_POST = 2;
    
    public static final String FIRST_TIME = "first_time_run";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String REMEMBER_ME = "remember_me";
    public static final String PROFILE_PIC_URL = "profile_pic_url";
    
    public static final String URL_ROOT = "http://apps.priyo.com/jonopriyo/api-v3.php";
    
    public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_REGISTRATION = "registration";
    public static final String METHOD_GET_COUNTRY = "get_countries";
    public static final String METHOD_GET_CITIES = "get_cities";
    public static final String METHOD_GET_AREA = "get_area";
    public static final String METHOD_GET_EDUCATION = "get_education";
    public static final String METHOD_GET_PROFESSION = "get_profession";
    
    public static final String METHOD_GET_NEW_POLLS = "get_new_polls";
    public static final String METHOD_POST_USER_POLL = "post_user_poll";
    public static final String METHOD_GET_POLL_RESULT = "poll_result";
    public static final String METHOD_GET_ALL_POLLS = "get_all_polls";
    public static final String METHOD_UPLOAD_PROFILE_PICTURE = "upload_profile_picture";
    public static final String METHOD_GET_MY_POLLS = "get_my_polls";
    public static final String METHOD_GET_WINNERS = "get_winners";
    public static final String METHOD_GET_PROFILE_INFO = "get_profile_info";
    public static final String METHOD_POST_PROFILE_INFO = "post_profile_info";
    
    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;
    
    public static final String FROM_ACTIVITY = "from_activity";
    public static final int PARENT_ACTIVITY_NEW_POLLS = 101;
    public static final int PARENT_ACTIVITY_ALL_POLLS = 102;
    public static final int PARENT_ACTIVITY_MY_POLLS = 103;

    
    public static final File APP_DIRECTORY =
            new File(Environment.getExternalStorageDirectory(),"jonoPriyo");
    

}
