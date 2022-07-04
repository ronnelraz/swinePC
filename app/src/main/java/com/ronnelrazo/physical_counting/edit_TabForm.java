package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_editPDF;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter_edit;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_edit_list;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class edit_TabForm extends AppCompatActivity {


    public static String Getorg_code,Getfarm_code;

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;



    @BindViews({R.id.types,R.id.orgcode,R.id.orgname,R.id.farmcode,R.id.farmname,R.id.docDate,R.id.auditDate})
    TextView[] headerDetails;

    @BindViews({R.id.save,R.id.cancel})
    public MaterialButton[] btn_func;


    private int RowCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tab_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        getHeader(Getorg_code,Getfarm_code);


        tabs.addTab(tabs.newTab().setText("Contact"));
        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter_edit adapter = new TabLayoutAdapter_edit(this,getSupportFragmentManager(),tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getHeader(String org_code,String farm_code) {
        Log.d("swine",org_code + " " + farm_code);
        API.getClient().edit_header(org_code,farm_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("header");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            headerDetails[0].setText(object.getString("type_type"));
                            headerDetails[1].setText(object.getString("org_code"));
                            headerDetails[2].setText(object.getString("org_name"));
                            headerDetails[3].setText(object.getString("farm_code"));
                            headerDetails[4].setText(object.getString("farm_name"));
                            headerDetails[5].setText(object.getString("doc_date"));
                            headerDetails[6].setText(object.getString("audit_date"));


                        }


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