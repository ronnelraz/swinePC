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
import com.ronnelrazo.physical_counting.adapter.Adapter_Feed;
import com.ronnelrazo.physical_counting.adapter.Adapter_Med;
import com.ronnelrazo.physical_counting.adapter.MedTab_adapter;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_feed;
import com.ronnelrazo.physical_counting.model.model_med;
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

public class Tab_med extends Fragment {

    public static String orgCode,farmOrg,cutoffDate;

    private Globalfunction data;
    private SharedPref sharedPref;

//    RecyclerView recyclerView;
//    RecyclerView.Adapter adapter;
//    public static List<model_med> list = new ArrayList<>();
//
//    @BindView(R.id.loading)
//    LinearLayout loading;


    @BindView(R.id.nodataContainer) LinearLayout nodatacontainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  new MedTab_adapter(getContext(),orgCode,farmOrg,cutoffDate);//inflater.inflate(R.layout.fragment_med,parent,false);
        return view;

    }

//    private void loadMedData(String orgCode, String farmOrg) {
//
//        list.clear();
//        API_.getClient().Med(orgCode,farmOrg,data.getMonth()).enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
//                try {
//
//                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
//                    boolean success = jsonResponse.getBoolean("success");
//                    JSONArray result = jsonResponse.getJSONArray("data");
//
//                    if(success){
//                        loading.setVisibility(View.GONE);
//                        nodatacontainer.setVisibility(View.GONE);
//                        for (int i = 0; i < result.length(); i++) {
//                            JSONObject object = result.getJSONObject(i);
//                            model_med item = new model_med(
//                                    object.getString("orgCode"),
//                                    object.getString("period"),
//                                    object.getString("projectCode"),
//                                    object.getString("farmCode"),
//                                    object.getString("farmOrg"),
//                                    object.getString("farmName"),
//                                    object.getString("productCode"),
//                                    object.getString("productName"),
//                                    object.getString("stockUnit"),
//                                    object.getString("stockQty"),
//                                    object.getString("stockWgh")
//
//                            );
//
//                            list.add(item);
//
//
//                        }
//
//                        adapter = new Adapter_Med(list,getActivity());
//                        recyclerView.setAdapter(adapter);
//                    }
//                    else{
//                        loading.setVisibility(View.GONE);
//                        nodatacontainer.setVisibility(View.VISIBLE);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("swine",e.getMessage());
//                    loading.setVisibility(View.GONE);
//                    nodatacontainer.setVisibility(View.GONE);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                if (t instanceof IOException) {
//                    data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
//                    loading.setVisibility(View.GONE);
//                    nodatacontainer.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
}
