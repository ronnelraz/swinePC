package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Feed;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder;
import com.ronnelrazo.physical_counting.fragment.Tab_feed;
import com.ronnelrazo.physical_counting.fragment.Tab_med;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_feed;
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

public class tab_from extends AppCompatActivity {


    public static String str_types,str_orgcode,str_orgname,str_farmcode,str_farmname,doc_date,audit_date;
    public static String business_type,bu_code,bu_name,bu_type_name;

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    protected List<String> listHeader = new ArrayList<>();

    @BindViews({R.id.types,R.id.orgcode,R.id.orgname,R.id.farmcode,R.id.farmname,R.id.docDate,R.id.auditDate})
    TextView[] headerDetails;

    @BindViews({R.id.save,R.id.cancel})
    public MaterialButton[] btn_func;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_from);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        //clear data first before fetch save all datas
        data.clearAll(str_orgcode,str_farmcode);

        headerDetails[0].setText(str_types);
        headerDetails[1].setText(str_orgcode);
        headerDetails[2].setText(str_orgname);
        headerDetails[3].setText(str_farmcode);
        headerDetails[4].setText(str_farmname);
        headerDetails[5].setText(doc_date);
        headerDetails[6].setText(audit_date);



        Log.d("swine",business_type);
        Log.d("swine",bu_code);
        Log.d("swine",bu_name);
        Log.d("Swine",bu_type_name);




        //SAVE HEADER
        boolean setChecklistHeader =  data.ADD_CHECKLIST_HEADER(str_orgcode, str_farmcode,str_orgname,str_farmname,doc_date,audit_date,str_types,business_type,bu_code,bu_name,bu_type_name);
        if(setChecklistHeader){
            Log.d("swine","save header");
        }
        else{
            Log.d("swine","error header");
        }


        //breeder
        Tab_breeder.orgCode = str_orgcode;
        Tab_breeder.farmOrg = str_farmcode;
        //feed
        Tab_feed.orgCode = str_orgcode;
        Tab_feed.farmOrg = str_farmcode;

        //Med
        Tab_med.orgCode = str_orgcode;
        Tab_med.farmOrg = str_farmcode;


        tabs.addTab(tabs.newTab().setText("Contact"));
        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter adapter = new TabLayoutAdapter(this,getSupportFragmentManager(),tabs.getTabCount());
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


        btn_func[0].setOnClickListener(v -> {
            saveHeaderChecklist();
        });





        btn_func[1].setOnClickListener(v -> {
            data.Confirmation(v.getContext(),"Are you sure you want to cancel " + str_farmname + "?",R.drawable.ic_icons8_warning);
            data.positive.setText("confirm");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                boolean result = data.clearAll(str_orgcode,str_farmcode);
                if(result){
                    Log.d("swine", "cancel transaction : " + result);
                    data.ConfirmDialog.dismiss();
                    data.intent(Farm_categories.class,v1.getContext());
                    finish();
                }

            });
        });
    }


    @Override
    public void onBackPressed() {
        data.clearAll(str_orgcode,str_farmcode);
        super.onBackPressed();
    }


    protected  void saveHeaderChecklist(){
        listHeader.clear();
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getHeader(getorg_Code,getfarm_Code);
        while(cursor.moveToNext()){
            listHeader.add(cursor.getString(0)); //org_code
            listHeader.add(cursor.getString(1)); //farm_code
            listHeader.add(cursor.getString(2)); //org_name
            listHeader.add(cursor.getString(3)); //farm_name
            listHeader.add(cursor.getString(4)); // doc_date
            listHeader.add(cursor.getString(5)); //audit_date
            listHeader.add(cursor.getString(6)); //type
            listHeader.add(cursor.getString(7)); //bu_type
            listHeader.add(cursor.getString(8)); //bu_code
            listHeader.add(cursor.getString(9)); //bu_name
            listHeader.add(cursor.getString(10)); //bu_type

            Log.d("Swine",cursor.getString(0) + " " +
                    cursor.getString(1) + " " +
                    cursor.getString(2) + " " +
                    cursor.getString(3) + " " +
                    cursor.getString(4) + " " +
                    cursor.getString(5) + " " +
                    cursor.getString(6) + " " +
                    cursor.getString(7) + " " +
                    cursor.getString(8) + " " +
                    cursor.getString(9) + " " +
                    cursor.getString(01));
        }
        saveHeaderOnineDB(listHeader);
    }

    protected  void saveHeaderChecklist_details(String audit_no){
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getChecklistDetails(getorg_Code,getfarm_Code);
        while(cursor.moveToNext()){
            saveHeader_detials_OnineDB(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(16),
                    cursor.getString(15),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14),
                    cursor.getString(4),
                    cursor.getString(5),
                    audit_no
            );
        }

    }

    protected void saveHeaderOnineDB(List<String> list){
        String ADuser = sharedPref.getUser();
        API.getClient().Header(list.get(0),list.get(4),list.get(5),list.get(8),list.get(9),list.get(7),list.get(10),list.get(1),ADuser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    String audit_no = jsonResponse.getString("audit_no");
                    if(success){
                        Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();
                        saveHeaderChecklist_details(audit_no);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }


    protected void saveHeader_detials_OnineDB(String ORG_CODE,String FARM_CODE,String BUSINESS_GROUP_CODE,String BUSINESS_TYPE_CODE,
                                              String MAIN_TOPIC_LIST_CODE, String MAIN_TOPIC_LIST_DESC, String MAIN_TOPIC_LIST_SEQ,
                                              String SUB_TOPIC_LIST_CODE,String SUB_TOPIC_LIST_DESC, String SUB_TOPIC_LIST_SEQ,
                                              String DETAIL_TOPIC_LIST_CODE, String DETAIL_TOPIC_LIST_DESC,String DETAIL_TOPIC_LIST_SEQ,
                                              String CHECK_FLAG,String REMARK,String audit_no){
        String ADuser = sharedPref.getUser();
        String getDoc_date = doc_date;
        String getAudit_date = audit_date;
//        Log.d("Swine",ORG_CODE + " " +getDoc_date + " " +getAudit_date + " " +FARM_CODE + " " +BUSINESS_GROUP_CODE + " " +BUSINESS_TYPE_CODE + " " +MAIN_TOPIC_LIST_CODE + " " +
//                MAIN_TOPIC_LIST_DESC + " " +MAIN_TOPIC_LIST_SEQ + " " +SUB_TOPIC_LIST_CODE + " " +SUB_TOPIC_LIST_DESC + " " +SUB_TOPIC_LIST_SEQ + " " +DETAIL_TOPIC_LIST_CODE + " " + DETAIL_TOPIC_LIST_DESC + " " +DETAIL_TOPIC_LIST_SEQ + " " +CHECK_FLAG + " " +REMARK + " " +ADuser);
        API.getClient().Header_checklist(ORG_CODE,getDoc_date,getAudit_date,FARM_CODE,BUSINESS_GROUP_CODE,BUSINESS_TYPE_CODE,MAIN_TOPIC_LIST_CODE,
                MAIN_TOPIC_LIST_DESC,MAIN_TOPIC_LIST_SEQ,SUB_TOPIC_LIST_CODE,SUB_TOPIC_LIST_DESC,SUB_TOPIC_LIST_SEQ,DETAIL_TOPIC_LIST_CODE,
                DETAIL_TOPIC_LIST_DESC,DETAIL_TOPIC_LIST_SEQ,CHECK_FLAG,REMARK,ADuser,audit_no).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }
}