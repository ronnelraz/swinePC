package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Cancel_list;
import com.ronnelrazo.physical_counting.adapter.Adapter_Confirm_list;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_cancel_list;
import com.ronnelrazo.physical_counting.model.model_confirm_list;
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
import retrofit2.Call;
import retrofit2.Callback;

public class Cancel extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;
    @BindView(R.id.cancel)
    MaterialButton confirm;

    @BindView(R.id.checkedall)
    CheckBox checkedAll;


    @BindView(R.id.org_code_filter)
    AutoCompleteTextView org_code_filter;
    @BindView(R.id.audit_date_filter)
    TextInputEditText audit_date_filter;
    List<String> autocompletelist = new ArrayList<>();


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<model_cancel_list> list =  new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        recyclerView = findViewById(R.id.data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Cancel_list(list,this,confirm,checkedAll,checkBoxes);
        recyclerView.setAdapter(adapter);
        String getorg_codes = org_code_filter.getText().toString();
        String getAudit_dates = audit_date_filter.getText().toString();
        getFarmList(getorg_codes,getAudit_dates);


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

        confirm.setOnClickListener(v -> {
            List<model_cancel_list> selectedItemlist = new Adapter_Cancel_list(list,this,confirm,checkedAll,checkBoxes).getSelected();
            String getorg_code = org_code_filter.getText().toString();
            String getAudit_date = audit_date_filter.getText().toString();

            for(int i = 0; i <selectedItemlist.size(); i++){
                if(!(i + 1 < selectedItemlist.size())) {
                    data.toast(R.raw.checked,"Cancelled Data successfully!", Gravity.TOP|Gravity.CENTER,0,50);
                    data.flag(selectedItemlist.get(i).getAudit_no(),"C",sharedPref.getUser());
                    confirm.setEnabled(false);
                    getFarmList(getorg_code,getAudit_date);
                }
                else{
                    data.flag(selectedItemlist.get(i).getAudit_no(),"C",sharedPref.getUser());
                    getFarmList(getorg_code,getAudit_date);

                }


            }



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

    private void AutoCompleteCode(String user) {
        autocompletelist.clear();
        API.getClient().autoCompleteOrg_code_cancel(user).enqueue(new Callback<Object>() {
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

                        adapter = new Adapter_Cancel_list(list,getApplicationContext(),confirm,checkedAll,checkBoxes);
                        recyclerView.setAdapter(adapter);
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice,autocompletelist);
        org_code_filter.setThreshold(1);
        org_code_filter.setAdapter(adapter);

    }



    public void back(View view) {
        data.intent(inv_form.class,view.getContext());
        finish();
    }

    private void getFarmList(String org_code,String date) {
        list.clear();
        adapter.notifyDataSetChanged();
        API.getClient().cancel_list(sharedPref.getUser(),org_code,date).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            model_cancel_list item = new model_cancel_list(
                                    object.getString("org_code"),
                                    object.getString("audit_no"),
                                    object.getString("doc_date"),
                                    object.getString("audit_date"),
                                    object.getString("farm_code"),
                                    object.getString("org_name"),
                                    object.getString("farm_name"),
                                    object.getString("confirm"),
                                    false
                            );

                            list.add(item);


                        }

                        adapter = new Adapter_Cancel_list(list,getApplicationContext(),confirm,checkedAll,checkBoxes);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        list.clear();
                        Toast.makeText(getApplicationContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
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


    public void search(View view) {
        String getorg_code = org_code_filter.getText().toString();
        String getAudit_date = audit_date_filter.getText().toString();
        list.clear();
        getFarmList(getorg_code,getAudit_date);

    }


}