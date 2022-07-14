package com.ronnelrazo.physical_counting.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.adapter.Adapter_Breeder;
import com.ronnelrazo.physical_counting.adapter.Adapter_Breeder_edit;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_breeder_edit;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.ronnelrazo.physical_counting.tab_from;

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

public class Tab_breeder_edit extends Fragment {



    private Globalfunction data;
    private SharedPref sharedPref;
    public static String str_audit_no;
    public static String Farm_code;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    public static List<model_breeder_edit> list = new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_breeder,parent,false);
        ButterKnife.bind(this,view);
        data = new Globalfunction(getActivity());
        sharedPref = new SharedPref(getActivity());

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.breeder_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter_Breeder_edit(list,getActivity(),str_audit_no,Farm_code);
        recyclerView.setAdapter(adapter);
        list.clear();
        loading.setVisibility(View.VISIBLE);
        loadBreederForm(str_audit_no);

        return view;

    }

    public void loadBreederForm(String str_audit_no) {
        list.clear();
        API.getClient().edit_breeder(str_audit_no).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            model_breeder_edit item = new model_breeder_edit(
                                    object.getString("locationCode"),
                                    object.getString("locationName"),
                                    object.getString("orgCode"),
                                    object.getString("orgName"),
                                    object.getString("farmOrg"),
                                    object.getString("farmName"),
                                    object.getString("maleQty"),
                                    object.getString("femaleQty"),
                                    object.getString("stockBalance"),
                                    tab_from.business_type,
                                    tab_from.bu_code,
                                    object.getString("farmOrg"),
                                    object.getString("counting"),
                                    object.getString("remark")


                            );

                           list.add(item);


                        }

                        adapter = new Adapter_Breeder_edit(list,getActivity(),str_audit_no,Farm_code);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        data.toast(R.raw.error,"Data Not Available", Gravity.TOP|Gravity.CENTER,0,50); //50
//                        data.loaddialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
                    data.loaddialog.dismiss();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}
