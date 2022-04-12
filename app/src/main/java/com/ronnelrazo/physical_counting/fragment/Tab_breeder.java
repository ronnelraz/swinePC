package com.ronnelrazo.physical_counting.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.ListItem_Checklist;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.adapter.Adapter_Breeder;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Tab_breeder extends Fragment {


    public static String orgCode,farmOrg;

    private Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    public static List<model_breeder> list = new ArrayList<>();


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
        adapter = new Adapter_Breeder(list,getActivity());
        recyclerView.setAdapter(adapter);
        loadBreederForm(orgCode,farmOrg);

        Log.d("swine", " ->breeder " + orgCode);
        Log.d("swine", " ->breeder " + farmOrg);
        return view;

    }

    public void loadBreederForm(String str_orgCode,String str_farmOrg) {
        list.clear();
        API_.getClient().Breeder(str_orgCode,str_farmOrg).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            model_breeder item = new model_breeder(
                                    object.getString("locationCode"),
                                    object.getString("locationName"),
                                    object.getString("orgCode"),
                                    object.getString("orgName"),
                                    object.getString("farmOrg"),
                                    object.getString("farmName"),
                                    object.getString("maleQty"),
                                    object.getString("femaleQty"),
                                    object.getString("stockBalance")

                            );

                           list.add(item);


                        }

                        adapter = new Adapter_Breeder(list,getActivity());
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
