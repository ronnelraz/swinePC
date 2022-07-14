package com.ronnelrazo.physical_counting.connection;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_breeder extends StringRequest {

    public static final String con = config.URLSeparate + "swineBreederCount.php";
    private Map<String,String> params;

    public con_breeder(String org_code, String farm_code, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("orgCode",org_code);
        params.put("farmCode",farm_code);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
