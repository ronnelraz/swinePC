package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_FarmDetails;
import com.ronnelrazo.physical_counting.adapter.Adapter_PDFReport;
import com.ronnelrazo.physical_counting.adapter.Adapter_editPDF;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_Farm_setup_details;
import com.ronnelrazo.physical_counting.model.model_edit_list;
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

public class Edit_pdf extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.search)
    AutoCompleteTextView search;
    List<String> autocompletelist = new ArrayList<>();

    private RecyclerView farmlist;
    private RecyclerView.Adapter adapter;
    private List<model_edit_list> list =  new ArrayList<>();


    @BindView(R.id.searchdate)
    TextView searchDate;

    @BindView(R.id.searchbtn)
    MaterialButton btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pdf);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        farmlist = findViewById(R.id.data);

        farmlist.setHasFixedSize(true);
        farmlist.setItemViewCacheSize(999999999);
        farmlist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_editPDF(list,this);
        farmlist.setAdapter(adapter);
        AutoCompleteCode(sharedPref.getUser());
        getFarmList("","");


        btn_search.setOnClickListener(v -> {
            String getOrg_code = search.getText().toString();
            String getDate = searchDate.getText().toString();

            if(getOrg_code.isEmpty() && getDate.isEmpty()){
                search.requestFocus();
                Toast.makeText(this, "Invalid filter parameters", Toast.LENGTH_SHORT).show();
            }
            else{
                getFarmList(getOrg_code,getDate);
            }
        });

    }

    private void AutoCompleteCode(String user) {
        autocompletelist.clear();
        API.getClient().autoCompleteOrg_code(user).enqueue(new Callback<Object>() {
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

                          adapter = new Adapter_editPDF(list, getApplicationContext());
                         farmlist.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(Edit_pdf.this, "error", Toast.LENGTH_SHORT).show();
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
        search.setThreshold(1);
        search.setAdapter(adapter);



        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(),R.style.picker,getDateto(), data.calendar
                        .get(Calendar.YEAR),data.calendar.get(Calendar.MONTH),
                        data.calendar.get(Calendar.DAY_OF_MONTH)).show();

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
        searchDate.setText(sdf.format(data.calendar.getTime()));
    }

    public void back(View view) {
        data.intent(inv_form.class,this);
    }


//    private void SearchDataModal(EditText search,RecyclerView farmlist){
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
//                ArrayList<model_edit_list> newList = new ArrayList<>();
//                for (model_edit_list sub : list)
//                {
//                    String code = sub.getOrg_code().toLowerCase();
//                    String name = sub.getFarm_name().toLowerCase();
//                    String audit = sub.getAudit_no().toLowerCase();
//                    if(name.contains(s) || code.contains(s) || audit.contains(s)){
//                        newList.add(sub);
//                    }
//                    adapter = new Adapter_editPDF(newList, getApplicationContext());
//                    farmlist.setAdapter(adapter);
//
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }

    private void getFarmList(String org_code,String date) {
        list.clear();
        API.getClient().edit_pdf_list(sharedPref.getUser(),org_code,date).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            model_edit_list item = new model_edit_list(
                                    object.getString("org_name"),
                                    object.getString("org_code"),
                                    object.getString("farm_code"),
                                    object.getString("farm_name"),
                                    object.getString("audit_no"),
                                    object.getString("doc_date"),
                                    object.getString("audit_date"),
                                    object.getString("business_group_code"),
                                    object.getString("buisness_type_code")
                            );

                            list.add(item);


                        }

                        adapter = new Adapter_editPDF(list,getApplicationContext());
                        farmlist.setAdapter(adapter);
                    }
                    else{
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
}