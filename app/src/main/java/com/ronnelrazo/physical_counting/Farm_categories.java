package com.ronnelrazo.physical_counting;

import static com.loopj.android.http.AsyncHttpClient.log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.adapter.Adapter_FarmDetails;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_Farm_setup_details;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Farm_categories extends AppCompatActivity {


    private  Globalfunction data;
    private SharedPref sharedPref;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    Adapter_Farm adapter_farm;
    @BindView(R.id.searchFarmlist)
    EditText search;
    private ArrayList<ListItem> items =  new ArrayList<>();
     public model_farm item;
    public ArrayList<model_farm> list = new ArrayList<>();

    public model_header_farm_org header;
    ArrayList<model_header_farm_org> headerlist = new ArrayList<>();



    @BindView(R.id.logout)
    MaterialButton logout;

//    filter_interface search_filter = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_categories);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);



        recyclerView = findViewById(R.id.farmlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        adapter_farm = new Adapter_Farm(items,list,headerlist);
        recyclerView.setAdapter(adapter_farm);
        GridLayoutManager gd = new GridLayoutManager(this, 2);
        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter_farm.getItemViewType(position) == ListItem.TYPE_HEADER ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gd);


//        search.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if(event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        search.setText("");
//                        LoadFarmlist(sharedPref.getRole(), sharedPref.getOrg_code(), sharedPref.getFarm_code());
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase();
                ArrayList<model_farm> filteredList = new ArrayList<>();

                for (model_farm farm : list) {
                    if (farm.getOrgcode().toLowerCase().contains(searchText) || farm.getOrgname().toLowerCase().contains(searchText)) {
                        filteredList.add(farm);
                    }
                }
                adapter_farm.filterListSearch(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        LoadFarmlist(sharedPref.getRole(), sharedPref.getOrg_code(), sharedPref.getFarm_code());
        Log.d("params_farm",sharedPref.getRole() + "\n" + sharedPref.getOrg_code() + "\n" + sharedPref.getFarm_code());


        logout.setOnClickListener(v -> {
            data.Confirmation(v.getContext(),"Are you sure you want to sign out your account?",R.drawable.ic_icons8_warning);
            data.positive.setText("Logout");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
                sharedPref.signout("false");
                data.intent(Login.class,v1.getContext());
                finish();
            });
        });



    }

    private void LoadFarmlist(String role, String org_code, String farm_code) {
        Log.d("org_code_list", org_code);
        data.Preloader(this, "Please wait...");
        // Clear the existing data
        items.clear();
        list.clear();
        headerlist.clear();
        API_.getClient().farmAPI(role, org_code, sharedPref.getUser()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    JSONObject jsonResponse = new JSONObject((Map) response.body());
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {


                        // Process "company" data
                        if (jsonResponse.has("company")) {
                            processCompanyData(jsonResponse);
                        }

                        if (jsonResponse.has("integration")) {
                            // Process "integration" data
                            processIntegrationData(jsonResponse);
                        }



                        // Initialize the adapter and set it to the RecyclerView
                        adapter_farm = new Adapter_Farm(items, list,headerlist);
                        recyclerView.setAdapter(adapter_farm);

                        // Notify the adapter that the data has changed
//                        adapter.notifyDataSetChanged();

                        data.loaddialog.dismiss();
                    } else {
                        data.toast(R.raw.error, "Invalid Params", Gravity.TOP | Gravity.CENTER, 0, 50); //50
                        data.loaddialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    data.toast(R.raw.error, t.getMessage(), Gravity.TOP | Gravity.CENTER, 0, 50);
                    data.loaddialog.dismiss();
                }
            }
        });
    }

    private void processCompanyData(JSONObject jsonResponse) throws JSONException {
        JSONObject company = jsonResponse.getJSONObject("company");
        int getCount = company.getInt("count");
        String getTYpeCompany = company.getString("type");
        JSONArray company_data = company.getJSONArray("data");

        if(getCount > 0){
            header = new model_header_farm_org(getTYpeCompany);
            items.add(header);
            int dataSize = company_data.length();
            for (int i = 0; i < dataSize; i++) {

                JSONObject object = company_data.getJSONObject(i);
                addItemToListAndItems(object, "0");
            }
            list.add(item);
        }

    }

    private void processIntegrationData(JSONObject jsonResponse) throws JSONException {
        JSONObject integration = jsonResponse.getJSONObject("integration");
        int getCount = integration.getInt("count");
        String getTYpeIntegration = integration.getString("type");
        JSONArray integration_data = integration.getJSONArray("data");

        if(getCount > 0){
            //        items.add(new model_header_farm_org(getTYpeIntegration));
            header = new model_header_farm_org(getTYpeIntegration);
            items.add(header);

            int dataSizeInt = integration_data.length();
            for (int ii = 0; ii < dataSizeInt; ii++) {

                JSONObject object = integration_data.getJSONObject(ii);
                addItemToListAndItems(object, "1");
            }

            list.add(item);
        }




    }

    private void addItemToListAndItems(JSONObject object, String type) throws JSONException {
        item = new model_farm(
                object.getString("orgcode"),
                object.getString("orgname"),
                type,
                object.getString("businessType"),
                object.getString("bu_code"),
                object.getString("bu_name"),
                object.getString("bu_type_name")
        );



        items.add(item);
        list.add(item);


//        log.w("swine",item.getOrgcode() + " " + item.getOrgname() + " " + item.getBusiness_type() + " " + item.getBu_code() + " " + item.bu_name + " " + item.bu_type_name);
    }


}