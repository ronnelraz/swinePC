package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.adapter.Adapter_FarmList;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_Farmlist;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Integration_submenu extends AppCompatActivity {

    public static String Integration,orgname,orgcode;
    private Globalfunction data;
    private SharedPref sharedPref;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<ListItem> items =  new ArrayList<>();


    @BindViews({R.id.breadcrumb_header,R.id.breadcrumb_orgname,R.id.breadcrumb_orgcode})
    TextView[] breadcrumbs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration_submenu);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        breadcrumbs[0].setText(Integration);
        breadcrumbs[1].setText(orgname);
        breadcrumbs[2].setText(orgcode);


        recyclerView = findViewById(R.id.farmlist);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_FarmList(items);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gd = new GridLayoutManager(this, 2);
        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ListItem.TYPE_HEADER ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gd);
        Log.d("swine",orgcode);
        LoadFarmlist(orgcode);
    }

    private void LoadFarmlist(String orgcode) {
        data.Preloader(this,"Please wait...");
        items.clear();
        API_.getClient().farmListAPI(orgcode).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONObject integration = jsonResponse.getJSONObject("integration");
                    int getcountIntegration = integration.getInt("count");
                    String getTYpeIntegration = integration.getString("type");
                    JSONArray integration_data = integration.getJSONArray("data");
                    Log.d("swine",getTYpeIntegration);


                    if(success){
                        data.loaddialog.dismiss();
                        items.add(new model_header_farm_org(getTYpeIntegration));
                        for (int i = 0; i < integration_data.length(); i++) {
                            JSONObject object = integration_data.getJSONObject(i);
                            model_Farmlist item = new model_Farmlist(
                                    object.getString("orgcode"),
                                    object.getString("orgname"),
                                    object.getString("farmcode"),
                                    object.getString("farmname")
                            );

                            items.add(item);
                        }

                        adapter = new Adapter_FarmList(items);
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

    public void backtocategory(View view) {
        data.intent(Farm_categories.class,view.getContext());
        finish();
    }
}