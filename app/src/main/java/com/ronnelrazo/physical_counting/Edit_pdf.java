package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_FarmDetails;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Edit_pdf extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.search)
    EditText search;

    private RecyclerView farmlist;
    private RecyclerView.Adapter adapter;
    private List<model_edit_list> list =  new ArrayList<>();

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
        getFarmList();
        SearchDataModal(search,farmlist);

    }

    public void back(View view) {
        data.intent(inv_form.class,this);
    }


    private void SearchDataModal(EditText search,RecyclerView farmlist){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                ArrayList<model_edit_list> newList = new ArrayList<>();
                for (model_edit_list sub : list)
                {
                    String code = sub.getOrg_code().toLowerCase();
                    String name = sub.getFarm_name().toLowerCase();
                    String audit = sub.getAudit_no().toLowerCase();
                    if(name.contains(s) || code.contains(s) || audit.contains(s)){
                        newList.add(sub);
                    }
                    adapter = new Adapter_editPDF(newList, getApplicationContext());
                    farmlist.setAdapter(adapter);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getFarmList() {
        list.clear();
        API.getClient().edit_pdf_list(sharedPref.getUser()).enqueue(new Callback<Object>() {
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
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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