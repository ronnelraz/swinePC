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


    public SharedPref(Context context){
        cont = context;
    }

    public static synchronized SharedPref getInstance(Context context){
        if(application == null){
            application = new SharedPref(context);
        }
        return application;
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

    public String checkAuto_login_auth(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_KEEP_SIGNED_IN, "false");
    }

    public String getAuth_token(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_TOKEN,null);
    }



}
