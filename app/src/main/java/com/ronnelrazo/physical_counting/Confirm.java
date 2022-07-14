package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Confirm_list;
import com.ronnelrazo.physical_counting.adapter.Adapter_editPDF;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_confirm_list;
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

public class Confirm extends AppCompatActivity {
    private Globalfunction data;
    private SharedPref sharedPref;
    @BindView(R.id.Confirm)
    MaterialButton confirm;

    @BindView(R.id.checkedall)
    CheckBox checkedAll;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<model_confirm_list> list =  new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        recyclerView = findViewById(R.id.data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Confirm_list(list,this,confirm,checkedAll,checkBoxes);
        recyclerView.setAdapter(adapter);
        getFarmList();


        confirm.setOnClickListener(v -> {
            List<model_confirm_list> selectedItemlist = new Adapter_Confirm_list(list,this,confirm,checkedAll,checkBoxes).getSelected();
//            checkedAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        for (int i = 0; i < selectedItemlist.size(); i++) {
//                            CheckBox currentChecBox = checkBoxes.get(i);
//                            currentChecBox.setChecked(true);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            });

            for(int i = 0; i <selectedItemlist.size(); i++){
                if(!(i + 1 < selectedItemlist.size())) {
                    data.toast(R.raw.checked,"Confirmed Data successfully!", Gravity.TOP|Gravity.CENTER,0,50);
                    data.flag(selectedItemlist.get(i).getAudit_no(),"Y");
                    confirm.setEnabled(false);
                    getFarmList();
                }
                else{
                    data.flag(selectedItemlist.get(i).getAudit_no(),"Y");
                    adapter.notifyItemRemoved(i);
                    adapter.notifyDataSetChanged();
                }


            }



        });


    }

    public void back(View view) {
        data.intent(inv_form.class,view.getContext());
        finish();
    }

    private void getFarmList() {
        list.clear();
        API.getClient().confirm_list(sharedPref.getUser()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            model_confirm_list item = new model_confirm_list(
                                    object.getString("org_code"),
                                    object.getString("audit_no"),
                                    object.getString("doc_date"),
                                    object.getString("audit_date"),
                                    object.getString("farm_code"),
                                    object.getString("org_name"),
                                    object.getString("farm_name"),
                                    false
                            );

                            list.add(item);


                        }

                        adapter = new Adapter_Confirm_list(list,getApplicationContext(),confirm,checkedAll,checkBoxes);
                        recyclerView.setAdapter(adapter);
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