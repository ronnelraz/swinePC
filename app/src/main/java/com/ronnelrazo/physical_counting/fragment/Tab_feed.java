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
import com.ronnelrazo.physical_counting.adapter.Adapter_Breeder;
import com.ronnelrazo.physical_counting.adapter.Adapter_Feed;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_feed;
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

public class Tab_feed extends Fragment {


    public static String orgCode,farmOrg;

    private Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    public static List<model_feed> list = new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feed,parent,false);
        ButterKnife.bind(this,view);
        data = new Globalfunction(getActivity());
        sharedPref = new SharedPref(getActivity());

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.feed_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter_Feed(list,getActivity());
        recyclerView.setAdapter(adapter);
        list.clear();
        loading.setVisibility(View.VISIBLE);
        loadFeedData(orgCode,farmOrg);

        Log.d("swine", " ->breeder " + orgCode);
        Log.d("swine", " ->breeder " + farmOrg);
        Log.d("swine",data.getMonth());

        return view;

    }

    private void loadFeedData(String orgCode, String farmOrg) {

        list.clear();
        API_.getClient().Feed(orgCode,farmOrg,data.getMonth()).enqueue(new Callback<Object>() {
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
                            model_feed item = new model_feed(
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
                                    object.getString("stockWgh")

                            );

                            list.add(item);


                        }

                        adapter = new Adapter_Feed(list,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        data.toast(R.raw.error,"Invalid Params", Gravity.TOP|Gravity.CENTER,0,50); //50
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
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}
