package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ronnelrazo.physical_counting.adapter.Adapter_Feed;
import com.ronnelrazo.physical_counting.adapter.Adapter_PDFReport;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;
import com.ronnelrazo.physical_counting.model.model_feed;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

public class Pdf_record_list extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ItemClickListener itemClickListener;
    private ArrayList<modal_pdf_report> list =  new ArrayList<>();


    @BindView(R.id.org_code_filter)
    AutoCompleteTextView org_code_filter;
    @BindView(R.id.audit_date_filter)
    TextInputEditText audit_date_filter;



    List<String> autocompletelist = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_record_list);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);


        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_PDFReport(list,this,itemClickListener);
        recyclerView.setAdapter(adapter);

        list.clear();

        PDFReport(sharedPref.getUser(),"","");


        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                                adapter.notifyDataSetChanged();
                        }
                    });


            }
        };

        AutoCompleteCode(sharedPref.getUser());
        org_code_filter.setOnTouchListener((v, event) -> {
            org_code_filter.showDropDown();
            return false;
        });

        audit_date_filter.setOnClickListener(v-> {
            new DatePickerDialog(v.getContext(),R.style.picker,getDateto(), data.calendar
                    .get(Calendar.YEAR),data.calendar.get(Calendar.MONTH),
                    data.calendar.get(Calendar.DAY_OF_MONTH)).show();


        });



    }


    public DatePickerDialog.OnDateSetListener getDateto(){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            data.calendar.set(Calendar.YEAR, year);
            data.calendar.set(Calendar.MONTH, monthOfYear);
            data.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Formatter();
        };
        return date;
    }

    private void Formatter() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        audit_date_filter.setText(sdf.format(data.calendar.getTime()));
    }

    private void PDFReport(String user,String org_code,String audit_date) {
        list.clear();
        API.getClient().ReportPDF(user,org_code,audit_date).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            Webhook("https://agro.cpf-phil.com/swinePC/api/checklistPDF?ORG_CODE="+object.getString("org_code")+"&AUDIT_NO="+ object.getString("audit_no")+"&FARM_ORG=","checklistPDF");
                            Webhook("https://agro.cpf-phil.com/swinePC/api/BreederPDF?ORG_CODE="+object.getString("org_code")+"&AUDIT_NO="+ object.getString("audit_no")+"&FARM_ORG=","BreederPDF");
                            Webhook("https://agro.cpf-phil.com/swinePC/api/FeedPDF?ORG_CODE="+object.getString("org_code")+"&AUDIT_NO="+ object.getString("audit_no")+"&FARM_ORG=","FeedPDF");
                            Webhook("https://agro.cpf-phil.com/swinePC/api/MedPDF?ORG_CODE="+object.getString("org_code")+"&AUDIT_NO="+ object.getString("audit_no")+"&FARM_ORG=","MedPDF");
                            modal_pdf_report item = new modal_pdf_report(
                                    object.getString("org_name"),
                                    object.getString("farm_name"),
                                    object.getString("org_code"),
                                    object.getString("farm_code"),
                                    object.getString("audit_no"),
                                    object.getString("audit_date"),
                                    false,
                                    object.getString("path"),
                                    object.getString("url")

                            );

                            list.add(item);


                        }

                        adapter = new Adapter_PDFReport(list,getApplicationContext(),itemClickListener);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(Pdf_record_list.this, "error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });
    }


    protected void Webhook(String urlString,String type) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d("swine",type +" Webhook : -> Started");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("swine",type +" Success" + " " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("swine",type +" Failed" + " " + errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d("swine",type +" retry ->" + retryNo);
            }
        });
    }


    private void AutoCompleteCode(String user) {
        autocompletelist.clear();
        API.getClient().autoCompleteOrg_code_generate(user).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            autocompletelist.add(object.getString("org_code"));
                        }

                        adapter = new Adapter_PDFReport(list,getApplicationContext(),itemClickListener);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(Pdf_record_list.this, "error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice,autocompletelist);
        org_code_filter.setThreshold(1);
        org_code_filter.setAdapter(adapter);

    }

    public void search(View view) {
        String getorg_code = org_code_filter.getText().toString();
        String getAudit_date = audit_date_filter.getText().toString();
        PDFReport(sharedPref.getUser(),getorg_code,getAudit_date);

    }
}