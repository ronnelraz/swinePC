package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.clans.fab.FloatingActionButton;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Confirm_list;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.adapter.Adapter_user_list;
import com.ronnelrazo.physical_counting.adapter.menu_access_adapter;
import com.ronnelrazo.physical_counting.adapter.org_code_list_adapter;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_list_group_user;
import com.ronnelrazo.physical_counting.model.modal_menu_access;
import com.ronnelrazo.physical_counting.model.modal_org_code;
import com.ronnelrazo.physical_counting.model.modal_user_list;
import com.ronnelrazo.physical_counting.model.model_confirm_list;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.model.model_role_type;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.skyhope.materialtagview.enums.TagSeparator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import retrofit2.Call;
import retrofit2.Callback;

public class user_setup extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.addUser)
    ExtendedFloatingActionButton adduser;


    List<modal_org_code> listItem = new ArrayList<>();
    org_code_list_adapter  adapter;

    AlertDialog listmodal;

    AlertDialog menu_modal;

    List<String> chiplist = new ArrayList<>();

    public List<String> role_id= new ArrayList<>();
    public List<String> role_name= new ArrayList<>();
    ArrayAdapter role_adapter;
    AlertDialog ConfirmDialog;
    String roleType;

    @BindView(R.id.aduser)
    AutoCompleteTextView aduser_header;
    List<String> autocomplete_list = new ArrayList<>();
    private String str_ad_user = "";

    @BindViews({R.id.fab_role,R.id.fab_user})
    FloatingActionButton[] fabs;

    List<modal_menu_access> menu_access_list = new ArrayList<>();
    ArrayAdapter menu_Adapter;

    String[] menu_access_map = {
            "1. TRANSACTION LIST","2. CREATE", "3. EDIT","4. CONFIRM","5. CANCEL","6. GENERATE PDF","7. UPLOAD FILE","8. FARM SETUP","9. USER SETUP","10. REPORT"
    };

    List<String> select_menu_list_code = new ArrayList<>();
    List<String> select_menu_list_name = new ArrayList<>();


    @BindView(R.id.data)
    RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private final List<modal_user_list> list =  new ArrayList<>();



    @BindView(R.id.loading)
    LottieAnimationView loadingmain;

    update_save_click_listener save_interface;

    @BindView(R.id.role_header)
    Spinner role_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        adduser.shrink();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new Adapter_user_list(list,this,save_interface);
        recyclerView.setAdapter(recyclerAdapter);
        loaduserlist("","","all");


        save_interface = new update_save_click_listener() {
            @Override
            public void onClick(View v, boolean reload) {
                if(reload){
                    loaduserlist("","","all");
                }
            }
        };
        AutoCompleteCode();
        aduser_header.setOnTouchListener((v, event) -> {
            aduser_header.showDropDown();
            return false;
        });


        roleType(role_header);
        role_header.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getOrg_code = role_id.get(position);
                roleType = getOrg_code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        
        fabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdduserFab(v);
            }
        });
        
    }

    
    
    protected void AdduserFab(View view){
        MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_add_user_dialog,null);
        EditText ad = v.findViewById(R.id.ad);
        Spinner role = v.findViewById(R.id.role);
        TagContainerLayout tagview = v.findViewById(R.id.tagview);
        MaterialButton btnorg_code = v.findViewById(R.id.btnorg_code);
        MaterialButton cancel = v.findViewById(R.id.close);
        MaterialButton save = v.findViewById(R.id.okay);
        ImageView closemodal = v.findViewById(R.id.closemodal);
        TextView menu_access_btn = v.findViewById(R.id.menu_access);

        tagview.setBackgroundColor(Color.parseColor("#ffffff"));
        tagview.setIsTagViewSelectable(true);
        tagview.isEnableCross();
        tagview.setEnableCross(true);
        tagview.setCrossColor(Color.BLACK);
        tagview.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                tagview.removeTag(position);
            }
        });

        closemodal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.dismiss();
            }
        });



        menu_access_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openModalMenu_access(v,menu_access_btn);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(roleType.equals("RL003")){
                    String getuserAD = ad.getText().toString().trim();
                    String org_code = roleType;
//                    String getMenus = select_menu_list_code.toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",",", ");
                    save_map_menu(getuserAD,org_code);
                    Add_user_setup(getuserAD,org_code,"0000",0);
                }
                else{
                    if(roleType.equals("0")){
                        Toast.makeText(user_setup.this, "Please Select Role Type", Toast.LENGTH_SHORT).show();
                    }
                    else if(tagview.size() == 0){
                        Toast.makeText(user_setup.this, "Please Select Org Code", Toast.LENGTH_SHORT).show();
                        btnorg_code.performClick();
                    }
                    else{
                        for(int i = 0; i < tagview.size(); i++){
                            String getuserAD = ad.getText().toString().trim();
                            String org_code = roleType;
                            String getOrg_code = tagview.getTagText(i);
//                            Toast.makeText(user_setup.this, org_code, Toast.LENGTH_SHORT).show();
                            if(!(i + 1 < tagview.size())){
//                                String getMenus = select_menu_list_code.toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",",", ");
                                save_map_menu(getuserAD,org_code);
                                Add_user_setup(getuserAD,org_code,getOrg_code,0);
                            }
                            else{
                                Add_user_setup(getuserAD,org_code,getOrg_code,1);
                            }


                        }
                    }
                }



            }
        });



        roleType(role);
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getOrg_code = role_id.get(position);
                roleType = getOrg_code;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tagview.setTagBackgroundColor(Color.TRANSPARENT);

        btnorg_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chiplist.clear();
                openAddOrgCodeModal(v,tagview);
            }
        });

        Materialdialog.setView(v);
        ConfirmDialog = Materialdialog.create();
        ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(ConfirmDialog);
        ConfirmDialog.show();
    }

    private void AutoCompleteCode() {
        autocomplete_list.clear();
        API.getClient().list_users_map("","","all").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            autocomplete_list.add(object.getString("AD"));
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
        aduser_header.setThreshold(1);
        aduser_header.setAdapter(adapter);

        aduser_header.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getAD = autocomplete_list.get(position);
                str_ad_user = getAD;
//                Toast.makeText(user_setup.this, str_ad_user, Toast.LENGTH_SHORT).show();
            }
        });

    }



    public  void loaduserlist(String ad,String role,String type){

        loadingmain.setVisibility(View.VISIBLE);
        loadingmain.setAnimation(R.raw.loading);
        loadingmain.loop(true);
        loadingmain.playAnimation();

        list.clear();
        API.getClient().list_users_map(ad,role,type).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        loadingmain.setVisibility(View.GONE);
                        loadingmain.loop(true);
                        loadingmain.playAnimation();

                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            JSONArray group = object.getJSONArray("group");

                            List<modal_list_group_user> group_list = new ArrayList<>();
                            for(int g = 0; g < group.length(); g++){
                                JSONObject group_items = group.getJSONObject(g);

                                modal_list_group_user item_group = new modal_list_group_user(
                                        group_items.getString("org_code"),
                                        group_items.getString("name"),
                                        false
                                );
                                group_list.add(item_group);

                            }

                            modal_user_list item = new modal_user_list(
                                    object.getString("AD"),
                                    object.getString("role"),
                                    object.getString("role_id"),
                                    group_list,
                                    object.getString("status")
                            );

                            list.add(item);


                        }

                        recyclerAdapter = new Adapter_user_list(list,getApplicationContext(),save_interface);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                    else{
                        list.clear();
                        loadingmain.setAnimation(R.raw.nodatafile);
                        loadingmain.setVisibility(View.VISIBLE);
                        loadingmain.loop(true);
                        loadingmain.playAnimation();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");
                    loadingmain.setAnimation(R.raw.nodatafile);
                    loadingmain.setVisibility(View.VISIBLE);
                    loadingmain.loop(true);
                    loadingmain.playAnimation();

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                    loadingmain.setAnimation(R.raw.nodatafile);
                    loadingmain.setVisibility(View.VISIBLE);
                    loadingmain.loop(true);
                    loadingmain.playAnimation();
                }
            }
        });

    }

    public void upload(View view) {
        if(adduser.isExtended()){
            adduser.shrink();
            MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
            View v = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_add_user_dialog,null);
            EditText ad = v.findViewById(R.id.ad);
            Spinner role = v.findViewById(R.id.role);
            TagContainerLayout tagview = v.findViewById(R.id.tagview);
            MaterialButton btnorg_code = v.findViewById(R.id.btnorg_code);
            MaterialButton cancel = v.findViewById(R.id.close);
            MaterialButton save = v.findViewById(R.id.okay);
            ImageView closemodal = v.findViewById(R.id.closemodal);
            TextView menu_access_btn = v.findViewById(R.id.menu_access);

            tagview.setBackgroundColor(Color.parseColor("#ffffff"));
            tagview.setIsTagViewSelectable(true);
            tagview.isEnableCross();
            tagview.setEnableCross(true);
            tagview.setCrossColor(Color.BLACK);
            tagview.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {

                }

                @Override
                public void onTagLongClick(int position, String text) {

                }

                @Override
                public void onSelectedTagDrag(int position, String text) {

                }

                @Override
                public void onTagCrossClick(int position) {
                    tagview.removeTag(position);
                }
            });

            closemodal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmDialog.dismiss();
                }
            });



            menu_access_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openModalMenu_access(v,menu_access_btn);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmDialog.dismiss();
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(roleType.equals("RL003")){
                        String getuserAD = ad.getText().toString().trim();
                        String org_code = roleType;
//                        String getMenus = select_menu_list_code.toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",",", ");
                        save_map_menu(getuserAD,org_code);
                        Add_user_setup(getuserAD,org_code,"0000",0);
                    }
                    else{
                        if(roleType.equals("0")){
                            Toast.makeText(user_setup.this, "Please Select Role Type", Toast.LENGTH_SHORT).show();
                        }
                        else if(tagview.size() == 0){
                            Toast.makeText(user_setup.this, "Please Select Org Code", Toast.LENGTH_SHORT).show();
                            btnorg_code.performClick();
                        }
                        else{
                            for(int i = 0; i < tagview.size(); i++){
                                String getuserAD = ad.getText().toString().trim();
                                String org_code = roleType;
                                String getOrg_code = tagview.getTagText(i);
//                            Toast.makeText(user_setup.this, org_code, Toast.LENGTH_SHORT).show();
                                if(!(i + 1 < tagview.size())){
//                                    String getMenus = select_menu_list_code.toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",",", ");
                                    save_map_menu(getuserAD,org_code);
                                    Add_user_setup(getuserAD,org_code,getOrg_code,0);
                                }
                                else{
                                    Add_user_setup(getuserAD,org_code,getOrg_code,1);
                                }


                            }
                        }
                    }



                }
            });



            roleType(role);
            role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String getOrg_code = role_id.get(position);
                    roleType = getOrg_code;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            tagview.setTagBackgroundColor(Color.TRANSPARENT);

            btnorg_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chiplist.clear();
                    openAddOrgCodeModal(v,tagview);
                }
            });

            Materialdialog.setView(v);
            ConfirmDialog = Materialdialog.create();
            ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            BounceView.addAnimTo(ConfirmDialog);
            ConfirmDialog.show();
        }
        else{
            adduser.extend();

        }
    }

    protected void openModalMenu_access(View view,TextView menu_list){
        menu_access_list.clear();
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(view.getContext());
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.menu_list_modal,null);
        ListView listView = v.findViewById(R.id.listData);
        MaterialButton cancel = v.findViewById(R.id.cancel);
        MaterialButton done = v.findViewById(R.id.done);
        CheckBox all = v.findViewById(R.id.ALL);


        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    List<modal_menu_access> items = new menu_access_adapter(menu_access_list,v.getContext(),done).getSelected();
                    for(int i = 0; i < menu_access_map.length; i++){
                        menu_access_list.get(i).setCheck(true);
                        done.setEnabled(true);
                        menu_Adapter.notifyDataSetChanged();
                    }
                }
                else{
                    for(int i = 0; i < menu_access_map.length; i++){
                        menu_access_list.get(i).setCheck(false);
                        done.setEnabled(false);
                        menu_Adapter.notifyDataSetChanged();
                    }
                }
            }
        });



        for(int i = 0; i < menu_access_map.length; i++){

            modal_menu_access list = new modal_menu_access(
                    menu_access_map[i],
                    false
            );

            menu_access_list.add(list);


        }


        menu_Adapter = new menu_access_adapter(menu_access_list,view.getContext(),done,all);
        listView.setAdapter(menu_Adapter);



//        String getSelected[] = menu_list.getText().toString().split(", ");
//
//        for(int x = 0; x < getSelected.length; x++){
//            int pos = menu_access_list.indexOf(getSelected[x]);
//
//            Toast.makeText(this, pos + " ", Toast.LENGTH_SHORT).show();
//        }


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_menu_list_code.clear();
                select_menu_list_name.clear();
                List<modal_menu_access> items = new menu_access_adapter(menu_access_list,v.getContext(),done,all).getSelected();
                for(int i = 0; i <items.size(); i++){
                    String getlist_code = items.get(i).mene.split("\\. ")[0];
                    if(!(i + 1 < items.size())) {
                        menu_modal.dismiss();


                        select_menu_list_code.add(getlist_code);
                        select_menu_list_name.add(items.get(i).getMene());
                        menu_list.setText(select_menu_list_name.toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",",", "));

                    }
                    else{
                        select_menu_list_code.add(getlist_code);
                        select_menu_list_name.add(items.get(i).getMene());
                    }


                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_modal.dismiss();
                menu_access_list.clear();
            }
        });




        dialogBuilder.setView(v);
        menu_modal = dialogBuilder.create();
        menu_modal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(menu_modal);
        menu_modal.show();
    }



    protected ArrayList setmenuAccess( List<String> list  ){
        HashSet<String> hashSet = new HashSet<>(list);
        hashSet.addAll(list);
        ArrayList<String> result = new ArrayList<>(hashSet);
        return result;
    }


    protected  void save_map_menu(String AD,String role_id){
        API.getClient().menu_map_setup(AD,sharedPref.getUser(),role_id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Log.d("qwertyu","save");
                    }
                    else{
                        Log.d("qwertyu","error");
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


    protected void Add_user_setup(String ad,String role,String org_code,int last){
        API.getClient().adduser_master(ad,role,org_code,sharedPref.getUser()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        if(last == 0){
                            data.toast(R.raw.checked,"Added Successfully!\nEmail Send to " + ad + "@cpf-phil.com" , Gravity.BOTTOM|Gravity.CENTER,0,50);
                            loaduserlist("","","all");
                            ConfirmDialog.dismiss();
                        }
                    }
                    else{
                        ConfirmDialog.dismiss();
                        data.toast(R.raw.error,"Org Code : " + org_code + " already Exist" , Gravity.BOTTOM|Gravity.CENTER,0,50);
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

    protected  void roleType(Spinner role){
        role_id.clear();
        role_name.clear();
        role_id.clear();
        role_name.clear();

        API.getClient().role_type().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray company_data = jsonResponse.getJSONArray("data");

                    if(success){
                        role_id.add("0");
                        role_name.add("Select Role Type");
                        for (int i = 0; i < company_data.length(); i++) {
                            JSONObject object = company_data.getJSONObject(i);
                            role_id.add(object.getString("role_id"));
                            role_name.add(object.getString("role"));

                        }

                        role_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,role_name);
                        role.setAdapter(role_adapter);


                    }
                    else{

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

    protected void openAddOrgCodeModal(View view, TagContainerLayout tagview){
        MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.org_code_list_modal,null);
        EditText search = v.findViewById(R.id.search);
        ListView listView = v.findViewById(R.id.listData);
        MaterialButton cancel = v.findViewById(R.id.cancel);
        MaterialButton done = v.findViewById(R.id.done);
        LottieAnimationView lottieAnimationView = v.findViewById(R.id.loading);

        listData(listView,view,lottieAnimationView,done);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listmodal.dismiss();
                listItem.clear();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<modal_org_code> items = new org_code_list_adapter(listItem,v.getContext(),done).getSelected();
                for(int i = 0; i <items.size(); i++){
                    if(!(i + 1 < items.size())) {
//                        Toast.makeText(user_setup.this, items.get(i).getOrg_code(), Toast.LENGTH_SHORT).show();
                        listmodal.dismiss();

                        addtaglist(tagview,items.get(i).getOrg_code());
                        HashSet<String> set = new HashSet<>(chiplist);
                        ArrayList<String> result = new ArrayList<>(set);
                        tagview.setTags(result);
//                        Toast.makeText(user_setup.this, "last" + i, Toast.LENGTH_SHORT).show();

                    }
                    else{
//                        Toast.makeText(user_setup.this, items.get(i).getOrg_code(), Toast.LENGTH_SHORT).show();
                        addtaglist(tagview,items.get(i).getOrg_code());
//                        Toast.makeText(user_setup.this, "more" + i, Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<modal_org_code> newListbottom = new ArrayList<>();
                for (modal_org_code sub : listItem)
                {
                    String name = sub.getName().toLowerCase();
                    String org_code = sub.getOrg_code().toLowerCase();
                    if(name.contains(s)){
                        newListbottom.add(sub);
                    }
                    else if(org_code.contains(s)){
                        newListbottom.add(sub);
                    }
                    adapter = new org_code_list_adapter(newListbottom,view.getContext(),done);
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Materialdialog.setView(v);
        listmodal = Materialdialog.create();
        listmodal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(listmodal);
        listmodal.show();
    }



    private void addtaglist(TagContainerLayout tagviewlist,String org_code){
        chiplist.add(org_code);
//        tagviewlist.addTag(org_code);
    }


    private void listData(ListView listview,View v,LottieAnimationView loading,MaterialButton done) {
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.loading);
        loading.loop(true);
        loading.playAnimation();
        listItem.clear();
        API.getClient().org_code_list().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray company_data = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        loading.loop(true);
                        loading.playAnimation();
                        for (int i = 0; i < company_data.length(); i++) {
                            JSONObject object = company_data.getJSONObject(i);
                            modal_org_code item = new modal_org_code(
                                    object.getString("org_code"),
                                    object.getString("org_name"),
                                    false

                            );

                            listItem.add(item);
                        }

                        adapter = new org_code_list_adapter(listItem,v.getContext(),done);
                        listview.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.nodatafile);
                        loading.setVisibility(View.VISIBLE);
                        loading.loop(true);
                        loading.playAnimation();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
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
                    loading.setAnimation(R.raw.nodatafile);
                    loading.setVisibility(View.VISIBLE);
                    loading.loop(true);
                    loading.playAnimation();
                }
            }
        });
    }

    public void back(View view) {
        data.intent(inv_form.class,view.getContext());
        finish();
    }

    public void search(View view) {
        String getAD = str_ad_user;
        String getRole = roleType;
        loaduserlist(getAD,getRole,"");
//        Toast.makeText(this, getAD + " " + getRole, Toast.LENGTH_SHORT).show();
    }

    public void reset(View view) {
        loaduserlist("","","all");
        aduser_header.setText("");
        role_header.setSelection(0);
    }
}