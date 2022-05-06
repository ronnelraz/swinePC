package com.ronnelrazo.physical_counting.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.ListItem_Checklist;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.adapter.Adapter_Checklist;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_checklist_Details;
import com.ronnelrazo.physical_counting.model.modal_checklist_SubDetails;
import com.ronnelrazo.physical_counting.model.modal_checklist_maintopic;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Tab_checklist extends Fragment {


    public static String str_bu_Type;
    private Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<ListItem_Checklist> items =  new ArrayList<>();

    @BindView(R.id.loading)
    LinearLayout loading;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_checklist,parent,false);
        ButterKnife.bind(this,view);
        data = new Globalfunction(getActivity());
        sharedPref = new SharedPref(getActivity());


        recyclerView = view.findViewById(R.id.checlist_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        adapter = new Adapter_Checklist(items,getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        loading.setVisibility(View.VISIBLE);
        LoadFarmlist(str_bu_Type);

//        Globalfunction.getInstance(getActivity()).toast(R.raw.checked,str_bu_Type,Gravity.TOP,0,50);
        return view;
    }

    private void LoadFarmlist(String BUtype) {
//        data.Preloader(getActivity(),"Please wait...");
        items.clear();
        API_.getClient().checkList(BUtype).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    String checklist_type = jsonResponse.getString("type");
                    String maintopic = jsonResponse.getString("mainTopic");
                    JSONArray checklistData = jsonResponse.getJSONArray("data");
                    items.add(new modal_checklist_maintopic(maintopic));
                    if(success){
                        loading.setVisibility(View.GONE);
//                        data.loaddialog.dismiss();

                        for (int i = 0; i < checklistData.length(); i++) {
                            JSONObject object = checklistData.getJSONObject(i);
                            modal_checklist_SubDetails subDetails_items = new modal_checklist_SubDetails(
                                    object.getString("BUcode"),
                                    object.getString("Maincode"),
                                    object.getString("subDetailsCode"),
                                    object.getString("subDetails")
                            );
                            items.add(subDetails_items);
                            JSONArray details =  object.getJSONArray("details");
                                for (int i_d = 0; i_d < details.length(); i_d++){

                                        JSONObject details_data = details.getJSONObject(i_d);
                                        modal_checklist_Details  details_item = new modal_checklist_Details(
                                                details_data.getString("detailsCode"),
                                                details_data.getString("details")
                                        );
                                    items.add(details_item);
                                }


                        }

                        adapter = new Adapter_Checklist(items,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        data.toast(R.raw.error,"Invalid Params", Gravity.TOP|Gravity.CENTER,0,50); //50
//                        data.loaddialog.dismiss();
                        loading.setVisibility(View.GONE);
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
//                    data.loaddialog.dismiss();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}
