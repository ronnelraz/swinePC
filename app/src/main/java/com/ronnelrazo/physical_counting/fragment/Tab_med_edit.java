package com.ronnelrazo.physical_counting.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.adapter.Adapter_Med;
import com.ronnelrazo.physical_counting.adapter.Adapter_Med_edit;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_med;
import com.ronnelrazo.physical_counting.model.model_med_edit;
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

public class Tab_med_edit extends Fragment {

    public static String audit_no;

    private Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    public static List<model_med_edit> list = new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;





    @BindView(R.id.nodataContainer) LinearLayout nodatacontainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_med,parent,false);
        ButterKnife.bind(this,view);




        data = new Globalfunction(getActivity());
        sharedPref = new SharedPref(getActivity());

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.med_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter_Med_edit(list,getActivity(),audit_no);
        recyclerView.setAdapter(adapter);
        list.clear();
        loading.setVisibility(View.VISIBLE);
        loadMedData(audit_no);

        return view;

    }

    private void loadMedData(String str_audit_no) {

        list.clear();
        API.getClient().edit_med(str_audit_no).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        nodatacontainer.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            model_med_edit item = new model_med_edit(
                                    object.getString("orgCode"),
                                    object.getString("period"),
                                    object.getString("projectCode"),
                                    object.getString("farmCode"),
                                    object.getString("farmOrg"),
                                    object.getString("farmName"),
                                    object.getString("productCode"),
                                    object.getString("productName"),
                                    object.getString("stockUnit"),
                                    object.getString("stockQty"),
                                    object.getString("stockWgh"),
                                    object.getString("counting"),
                                    object.getString("remark")

                            );

                            list.add(item);


                        }

                        adapter = new Adapter_Med_edit(list,getActivity(),audit_no);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        nodatacontainer.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                    loading.setVisibility(View.GONE);
                    nodatacontainer.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
                    loading.setVisibility(View.GONE);
                    nodatacontainer.setVisibility(View.GONE);
                }
            }
        });
    }
}
