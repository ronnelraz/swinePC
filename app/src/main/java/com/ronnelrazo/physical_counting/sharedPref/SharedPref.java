package com.ronnelrazo.physical_counting.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private static final String JWT_ORG_CODE = "JWT_ORG_CODE";
    private static final String JWT_FARM_CODE = "JWT_FARM_CODE";
    private static final String JWT_BU = "JWT_BU";
    private static final String JWT_ROLE_ID = "JWT_ROLE_ID";
    private static final String JWT_MENU_ACCESS = "JWT_MENU_ACCESS";



    public SharedPref(Context context){
        cont = context;
    }

    public static synchronized SharedPref getInstance(Context context){
        if(application == null){
            application = new SharedPref(context);
        }
        return application;
    }


    public boolean setJWTData(String username, String role, String bu, String org_code, String farm_code,String role_id,String menu_access){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(JWT_USERNAME,username);
        editor.putString(JWT_ROLE,role);
        editor.putString(JWT_BU,bu);
        editor.putString(JWT_ORG_CODE, org_code);
        editor.putString(JWT_FARM_CODE, farm_code);
        editor.putString(JWT_ROLE_ID,role_id);
        editor.putString(JWT_MENU_ACCESS,menu_access);
        editor.apply();
        return true;
    }

    protected Set<String> arr (String item){
        Set<String> hs = sharedPreferences.getStringSet(item, new HashSet<String>());
        Set<String> in = new HashSet<String>(hs);
        return in;
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

    public String getRole_id(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_ROLE_ID, "");
    }

    public String getJwtMenuAccess(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_MENU_ACCESS, "");
    }

    public String getOrg_code(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_ORG_CODE, "");
    }

    public String getFarm_code(){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_FARM_CODE, "");
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
