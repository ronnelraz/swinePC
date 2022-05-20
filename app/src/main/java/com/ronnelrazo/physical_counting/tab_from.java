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
import com.ronnelrazo.physical_counting.adapter.Adapter_Checklist;
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


    private int RowCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_from);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        //clear data first before fetch save all datas
//        data.clearAll(str_orgcode,str_farmcode);

        headerDetails[0].setText(str_types);
        headerDetails[1].setText(str_orgcode);
        headerDetails[2].setText(str_orgname);
        headerDetails[3].setText(str_farmcode);
        headerDetails[4].setText(str_farmname);
        headerDetails[5].setText(doc_date);
        headerDetails[6].setText(audit_date);



        Log.d("swine",business_type);//need
        Log.d("swine",bu_code);//need
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
           String tab_contact = 0 == 0 ? "N" : "Y";
           String tab_checklist = Globalfunction.getInstance(v.getContext()).tabUsed("TABLE_HEADER_DETAILS") == 0 ? "N" : "Y";
           String tab_breeder = Globalfunction.getInstance(v.getContext()).tabUsed("TABLE_BREEDER_DETAILS") == 0 ? "N" : "Y";
           String tab_feed = Globalfunction.getInstance(v.getContext()).tabUsed("TABLE_FEED_DETAILS") == 0 ? "N" : "Y";
           String tab_med = Globalfunction.getInstance(v.getContext()).tabUsed("TABLE_MED_DETAILS") == 0 ? "N" : "Y";
           Log.d("swine", tab_contact + " " + tab_checklist + " " + tab_breeder + " " + tab_feed + " " + tab_med);

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

        }
        saveHeaderOnineDB(listHeader);
    }


    //LOCALDATA CHECKLIST
    protected  void saveHeaderChecklist_details(String audit_no){
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getChecklistDetails(getorg_Code,getfarm_Code);
        int totalRow = cursor.getCount();
        while(cursor.moveToNext()){
            RowCounter++;
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
            if (RowCounter == totalRow) {
               data.toast(R.raw.checked,"[Checklist] Uploaded successfully! (" + totalRow +"/"+ RowCounter +")",Gravity.TOP|Gravity.CENTER,0,50);
               RowCounter = 0;
            }
        }

    }

    //LOCALDATA BREEDER COUNT
    protected  void saveBreederCount_details(String audit_no){
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getBreederDetails(getorg_Code,getfarm_Code);
        int totalRow = cursor.getCount();
        while(cursor.moveToNext()){
            RowCounter++;
            saveBreeder_count_OnineDB(
                    cursor.getString(1),
                    audit_no,
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12)
                    );
            if (RowCounter == totalRow) {
                data.toast(R.raw.checked,"[Breeder] Uploaded successfully! (" + totalRow +"/"+ RowCounter +")",Gravity.TOP|Gravity.CENTER,0,50);
                RowCounter = 0;
            }
        }

    }

    protected  void saveFeedCount_details(String audit_no){
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getFeedListDetails(getorg_Code,getfarm_Code);
        int totalRow = cursor.getCount();
        while(cursor.moveToNext()){
            RowCounter++;
            saveFeed_count_OnineDB(
                    cursor.getString(1),
                    audit_no,
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13)
            );
            if (RowCounter == totalRow) {
                data.toast(R.raw.checked,"[Feed] Uploaded successfully! (" + totalRow +"/"+ RowCounter +")",Gravity.TOP|Gravity.CENTER,0,50);
                RowCounter = 0;
            }
        }
    }


    protected  void saveMedCount_details(String audit_no){
        String getorg_Code = str_orgcode;
        String getfarm_Code = str_farmcode;

        Cursor cursor = data.getMedListDetails(getorg_Code,getfarm_Code);
        int totalRow = cursor.getCount();
        while(cursor.moveToNext()){
            RowCounter++;
            saveMed_count_OnineDB(
                    cursor.getString(1),
                    audit_no,
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13)
            );
            if (RowCounter == totalRow) {
                data.toast(R.raw.checked,"[Med] Uploaded successfully! (" + totalRow +"/"+ RowCounter +")",Gravity.TOP|Gravity.CENTER,0,50);
                RowCounter = 0;
            }
        }

    }



    /**************************************************/

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
                        data.toast(R.raw.checked,"Generate Audit No. " + audit_no,Gravity.TOP|Gravity.CENTER,0,50);
                        saveHeaderChecklist_details(audit_no);
//                        saveBreederCount_details(audit_no);
//                        saveFeedCount_details(audit_no);
//                        saveMedCount_details(audit_no);

                    }
                    else{
                        data.toast(R.raw.error,"Data Already Exist" + audit_no,Gravity.TOP|Gravity.CENTER,0,50);
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



    protected void saveHeader_detials_OnineDB(String ORG_CODE,String FARM_CODE,String BUSINESS_GROUP_CODE,String BUSINESS_TYPE_CODE,String MAIN_TOPIC_LIST_CODE, String MAIN_TOPIC_LIST_DESC, String MAIN_TOPIC_LIST_SEQ,String SUB_TOPIC_LIST_CODE,String SUB_TOPIC_LIST_DESC, String SUB_TOPIC_LIST_SEQ,String DETAIL_TOPIC_LIST_CODE, String DETAIL_TOPIC_LIST_DESC,String DETAIL_TOPIC_LIST_SEQ,String CHECK_FLAG,String REMARK,String audit_no){
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
                    String remark = jsonResponse.getString("message");
                    if(success){
                        Toast.makeText(getApplicationContext(), remark, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
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

    protected void saveBreeder_count_OnineDB(String ORG_CODE,String AUDIT_NO, String BUSINESS_GROUP_CODE, String BUSINESS_TYPE_CODE,String LOCATION,String FARM_CODE, String FARM_ORG,String FARM_NAME, String SYS_FEMALE_STOCK,String SYS_MALE_STOCK,String SYS_TOTAL_STOCK,String COUNTING_STOCK,String REMARK){
        String ADuser = sharedPref.getUser();
        String getDoc_date = doc_date;
        String getAudit_date = audit_date;

        API.getClient().Header_BreederCountList(ORG_CODE,AUDIT_NO,getDoc_date,getDoc_date,getAudit_date,BUSINESS_GROUP_CODE,BUSINESS_TYPE_CODE,LOCATION,
                FARM_CODE,FARM_ORG,FARM_NAME,SYS_FEMALE_STOCK,SYS_MALE_STOCK,SYS_TOTAL_STOCK,COUNTING_STOCK,REMARK,ADuser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
//                        Toast.makeText(getApplicationContext(), "ok breeder", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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

    protected void saveFeed_count_OnineDB(String ORG_CODE,
                                          String AUDIT_NO,
                                          String BUSINESS_GROUP_CODE,
                                          String BUSINESS_TYPE_CODE,
                                          String FARM_CODE,
                                          String FARM_ORG,
                                          String FARM_NAME,
                                          String FEED_CODE,
                                          String FEED_NAME,
                                          String SYS_FFED_STOCK_QTY,
                                          String SYS_FEED_STOCK_WGH,
                                          String STOCK_UNIT,
                                          String COUNTING_STOCK,
                                          String REMARK){
        String ADuser = sharedPref.getUser();
        String getDoc_date = doc_date;
        String getAudit_date = audit_date;

        API.getClient().Header_FeedCountList(ORG_CODE,AUDIT_NO,getDoc_date,getDoc_date,getAudit_date,BUSINESS_GROUP_CODE,BUSINESS_TYPE_CODE,FARM_CODE,FARM_ORG,FARM_NAME,FEED_CODE,FEED_NAME,SYS_FFED_STOCK_QTY,SYS_FEED_STOCK_WGH,STOCK_UNIT,COUNTING_STOCK,REMARK,ADuser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    Log.d("swine",String.valueOf(success));
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


    protected void saveMed_count_OnineDB(String ORG_CODE,
                                          String AUDIT_NO,
                                          String BUSINESS_GROUP_CODE,
                                          String BUSINESS_TYPE_CODE,
                                          String FARM_CODE,
                                          String FARM_ORG,
                                          String FARM_NAME,
                                          String MED_CODE,
                                          String MED_NAME,
                                          String SYS_MED_STOCK_QTY,
                                          String SYS_MED_STOCK_WGH,
                                          String STOCK_UNIT,
                                          String COUNTING_STOCK,
                                          String REMARK){
        String ADuser = sharedPref.getUser();
        String getDoc_date = doc_date;
        String getAudit_date = audit_date;

        API.getClient().Header_MedCountList(ORG_CODE,AUDIT_NO,getDoc_date,getDoc_date,getAudit_date,BUSINESS_GROUP_CODE,BUSINESS_TYPE_CODE,FARM_CODE,FARM_ORG,FARM_NAME,MED_CODE,MED_NAME,SYS_MED_STOCK_QTY,SYS_MED_STOCK_WGH,STOCK_UNIT,COUNTING_STOCK,REMARK,ADuser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    Log.d("swine",String.valueOf(success));
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