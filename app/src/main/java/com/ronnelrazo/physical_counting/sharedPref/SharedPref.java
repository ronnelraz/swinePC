package com.ronnelrazo.physical_counting.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;

public class SharedPref {

    public static final String TAG = "Swine";
    private static SharedPref application;
    private static Context cont;


    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHARED_DATA = "SHARED_DATA";
    private static final String SHARED_TOKEN = "TOKEN";
    private static final String SHARED_KEEP_SIGNED_IN = "false";
    private static final String JWT_USERNAME = "JWT_USERNAME";
    private static final String JWT_ROLE = "JWT_ROLE";
    private static final String JWT_BU = "JWT_BU";


    public SharedPref(Context context){
        cont = context;
    }

    public static synchronized SharedPref getInstance(Context context){
        if(application == null){
            application = new SharedPref(context);
        }
        return application;
    }


    public boolean setJWTData(String username,String role,String bu){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(JWT_USERNAME,username);
        editor.putString(JWT_ROLE,role);
        editor.putString(JWT_BU,bu);
        editor.apply();
        return true;
    }

    public boolean signout(String logout){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SHARED_KEEP_SIGNED_IN, logout);
        editor.apply();
        return true;
    }

    public boolean set_login_auth(String token, String keeplogin){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SHARED_TOKEN,token);
        editor.putString(SHARED_KEEP_SIGNED_IN, keeplogin);
        editor.apply();
        return true;
    }


    public String getRole(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_ROLE, "");
    }

    public String getBU(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_BU, "");
    }

    public String getUser(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_USERNAME, "");
    }

    public String checkAuto_login_auth(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_KEEP_SIGNED_IN, "false");
    }

    public String getAuth_token(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_TOKEN,null);
    }



}
