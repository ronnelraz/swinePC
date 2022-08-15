package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm_demo;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
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

public class Farm_categories extends AppCompatActivity {


    private  Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<ListItem> items =  new ArrayList<>();
//    List<model_farm> list = new ArrayList<>();
//    List<model_header_farm_org> headerlist = new ArrayList<>();



    @BindView(R.id.logout)
    MaterialButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_categories);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);


        recyclerView = findViewById(R.id.farmlist);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_Farm(items);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gd = new GridLayoutManager(this, 2);
        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ListItem.TYPE_HEADER ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gd);
        LoadFarmlist(sharedPref.getRole(), sharedPref.getOrg_code(), sharedPref.getFarm_code());


        logout.setOnClickListener(v -> {
            data.Confirmation(v.getContext(),"Are you sure you want to sign out your account?",R.drawable.ic_icons8_warning);
            data.positive.setText("Logout");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
                sharedPref.signout("false");
                data.intent(Login.class,v1.getContext());
                finish();
            });
        });

    }

    private void LoadFarmlist(String role,String org_code,String farm_code) {
        Log.d("org_code_list",org_code);
        data.Preloader(this,"Please wait...");
        items.clear();
        API_.getClient().farmAPI(role,org_code,farm_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    JSONObject company = jsonResponse.getJSONObject("company");
                    int getcountCompany = company.getInt("count");
                    String getTYpeCompany = company.getString("type");
                    JSONArray company_data = company.getJSONArray("data");

                    JSONObject integration = jsonResponse.getJSONObject("integration");
                    int getcountIntegration = integration.getInt("count");
                    String getTYpeIntegration = integration.getString("type");
                    JSONArray integration_data = integration.getJSONArray("data");

                    Log.d("swine",getTYpeCompany);
                    Log.d("swine",getTYpeIntegration);


                    if(success){
                        data.loaddialog.dismiss();
                        items.add(new model_header_farm_org(getTYpeCompany));
                        for (int i = 0; i < company_data.length(); i++) {
                            JSONObject object = company_data.getJSONObject(i);
                            model_farm item = new model_farm(
                                    object.getString("orgcode"),
                                    object.getString("orgname"),
                                    "0",
                                    object.getString("businessType"),
                                    object.getString("bu_code"),
                                    object.getString("bu_name"),
                                    object.getString("bu_type_name")

                            );

                            items.add(item);


                        }

                        items.add(new model_header_farm_org(getTYpeIntegration));
                        for (int i = 0; i < integration_data.length(); i++) {
                            JSONObject object = integration_data.getJSONObject(i);
                            model_farm item = new model_farm(
                                    object.getString("orgcode"),
                                    object.getString("orgname"),
                                    "1",
                                    object.getString("businessType"),
                                    object.getString("bu_code"),
                                    object.getString("bu_name"),
                                    object.getString("bu_type_name")
                            );

                            items.add(item);
                        }

                        adapter = new Adapter_Farm(items);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        data.toast(R.raw.error,"Invalid Params", Gravity.TOP|Gravity.CENTER,0,50); //50
                        data.loaddialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
                    data.loaddialog.dismiss();
                }
            }
        });
    }
}