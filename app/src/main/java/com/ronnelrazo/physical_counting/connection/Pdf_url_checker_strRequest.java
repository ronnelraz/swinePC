package com.ronnelrazo.physical_counting.connection;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Pdf_url_checker_strRequest extends StringRequest {
    private Map<String,String> params;

    public Pdf_url_checker_strRequest(String path,Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Request.Method.GET,path,Listener,errorListener);
        params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}

