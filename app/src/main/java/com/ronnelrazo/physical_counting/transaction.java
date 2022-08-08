package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_PDFReport;
import com.ronnelrazo.physical_counting.adapter.Adapter_transaction_details;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.model.model_viewfile;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class transaction extends AppCompatActivity {

    Globalfunction data;
    SharedPref sharedPref;

    @BindView(R.id.org_code_filter)
    AutoCompleteTextView org_code;
    List<String> autocomplete_list = new ArrayList<>();
    private String str_org_code = "";

    @BindViews({R.id.to,R.id.from}) TextInputEditText[] datefilter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<model_transaction_details> list;

    @BindView(R.id.loading)
    LottieAnimationView loading;
    @BindView(R.id.types)
    RadioGroup types;
    int selectedid;
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_transaction_details(list,this);
        recyclerView.setAdapter(adapter);

        datefilter[0].setText(data.transaction_date());
        datefilter[1].setText(data.transaction_date());

        transactionList(sharedPref.getUser(),"",data.transaction_date(),data.transaction_date(),"All");

        AutoCompleteCode(sharedPref.getUser());
            org_code.setOnTouchListener((v, event) -> {
                org_code.showDropDown();
                return false;
        });

        datefilter[0].setOnClickListener(v -> {
            datemodal(datefilter[0]);
        });

        datefilter[1].setOnClickListener(v -> {
            datemodal(datefilter[1]);
        });




    }



    private void transactionList(String user,String org_code,String to,String from,String types) {
        list.clear();

        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.loading);
        loading.loop(true);
        loading.playAnimation();
        API.getClient().transaction_detials(user,org_code,to,from,types).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        loading.loop(true);
                        loading.playAnimation();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            model_transaction_details item = new model_transaction_details(
                                    object.getString("title"),
                                    object.getString("org_code"),
                                    object.getString("audit_no"),
                                    object.getString("farm_name"),
                                    object.getString("audit_date"),
                                    object.getString("status"),
                                    object.getString("farm_code")

                            );

                            list.add(item);


                        }

                        adapter = new Adapter_transaction_details(list,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.nodatafile);
                        loading.setVisibility(View.VISIBLE);
                        loading.loop(true);
                        loading.playAnimation();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");
                    loading.setAnimation(R.raw.nodatafile);
                    loading.setVisibility(View.VISIBLE);
                    loading.loop(true);
                    loading.playAnimation();

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

    private  void datemodal(TextInputEditText s){
        new DatePickerDialog(this,R.style.picker,getDateto(s), data.calendar
                .get(Calendar.YEAR),data.calendar.get(Calendar.MONTH),
                data.calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public DatePickerDialog.OnDateSetListener getDateto(TextInputEditText datefilter){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            data.calendar.set(Calendar.YEAR, year);
            data.calendar.set(Calendar.MONTH, monthOfYear);
            data.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            datefilter.setText(sdf.format(data.calendar.getTime()));
        };
        return date;
    }

    private void AutoCompleteCode(String user) {
        autocomplete_list.clear();
        API.getClient().transaction_org_code(user).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            autocomplete_list.add(object.getString("FARM_NAME"));
                        }

                    }
                    else{

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item ,R.id.autoCompleteItem,autocomplete_list);
        org_code.setThreshold(1);
        org_code.setAdapter(adapter);

        org_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] getFarm = autocomplete_list.get(position).split(" \\(" )[1].split("\\)");
                str_org_code = getFarm[0];
//                Toast.makeText(transaction.this, str_org_code, Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void back(View view) {
        data.intent(inv_form.class,view.getContext());
        finish();
    }

    public void search(View view) {
     String getOrg_code = str_org_code.trim();
     String to = datefilter[0].getText().toString();
     String from = datefilter[1].getText().toString();

     selectedid = types.getCheckedRadioButtonId();
     radioButton = (types).findViewById(selectedid);
     String getTypes = radioButton.getText().toString();

     transactionList(sharedPref.getUser(),getOrg_code, to, from,getTypes);


    }
}