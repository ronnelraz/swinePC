package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.shuhart.stepview.StepView;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class Farm_setup extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindViews({R.id.layout1,R.id.layout2,R.id.layout3,R.id.layout4,R.id.layout5})
    View[] layoutView;
    @BindViews({R.id.previous,R.id.next})
    MaterialButton[] btn_trigger;
    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.error) TextView ErrorMessage;

    private int currentSelectStepView = 0;
    private List<String> Org_code_list = new ArrayList<>();

    private List<String> Org_name_list = new ArrayList<>();
    private List<String> Org_bu_type_list = new ArrayList<>();
    private List<String> Org_bu_code_list = new ArrayList<>();

    @BindViews({R.id.org_code,R.id.org_name})
    SearchableSpinner[] business_Area;
    @BindViews({R.id.business_type,R.id.business_code})
    TextView[] Business_area_code;
    ArrayAdapter adapter_org_code;
    ArrayAdapter adapter_org_name;


    @BindViews({R.id.checklist,R.id.breeder,R.id.feed,R.id.med})
    CheckBox[] FormArea;


    @BindViews({R.id.province,R.id.municipality,R.id.barangay})
    SearchableSpinner[] address_Area;
    @BindViews({R.id.zipcode,R.id.lati,R.id.longti})
    EditText[] address_Area_edit;
    private List<String> province_code = new ArrayList<>();
    private List<String>province_name = new ArrayList<>();
    private List<String> region_code = new ArrayList<>();
    ArrayAdapter province_adapter;

    private List<String> municipal_code= new ArrayList<>();
    private List<String> municipal_name= new ArrayList<>();
    ArrayAdapter municipal_adapter;

    private List<String> barangay_code= new ArrayList<>();
    private List<String> barangay_name= new ArrayList<>();
    ArrayAdapter barangay_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_setup);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        loadFarmOrg();
        loadAddressProvince(); //load Province


        step(currentSelectStepView);
        btn_trigger[1].setOnClickListener(v -> {
            Log.d("swine","count " + currentSelectStepView);

            if(currentSelectStepView == 0){
                businessAreaValidator();
            }

            else if(currentSelectStepView == 1){
                FormAreaValudator();
            }
            else if(currentSelectStepView == 2){
                AddressValidator();
            }
            else if(currentSelectStepView == 3){
                ShowPager(0,View.GONE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.VISIBLE);
                ShowPager(4,View.GONE);
            }
            else if(currentSelectStepView == 4){
                ShowPager(0,View.GONE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.VISIBLE);
                btn_trigger[1].setText("Save");
            }

        });

        btn_trigger[0].setOnClickListener(v -> {
            currentSelectStepView--;
            Log.d("swine","count " + currentSelectStepView);
            step(currentSelectStepView);
            btn_trigger[1].setText("Next");
            if(currentSelectStepView == 0){
                ShowPager(0,View.VISIBLE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.GONE);
                btn_trigger[0].setVisibility(View.GONE);
            }
            else if(currentSelectStepView == 1){
                ShowPager(0,View.GONE);
                ShowPager(1,View.VISIBLE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.GONE);

            }
            else if(currentSelectStepView == 2){
                ShowPager(0,View.GONE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.VISIBLE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.GONE);
            }
            else if(currentSelectStepView == 3){
                ShowPager(0,View.GONE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.VISIBLE);
                ShowPager(4,View.GONE);
            }
            else if(currentSelectStepView == 4){
                ShowPager(0,View.GONE);
                ShowPager(1,View.GONE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.VISIBLE);

            }
        });
    }


    private void loadAddressProvince(){
        province_name.clear();
        province_code.clear();
        region_code.clear();
        API.getClient().getProvince().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            province_code.add(object.getString("province_code"));
                            province_name.add(object.getString("province_name"));
                            region_code.add(object.getString("region_code"));
                        }
                        province_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,province_name);
                        SpinnerSetup(address_Area[0],province_adapter,"Please Select Province");
                        loadMunicipality();
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

    protected void loadMunicipality(){
        address_Area[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadMunicipality_data(province_code.get(position));
//                Toast.makeText(Farm_setup.this, province_code.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    protected void loadMunicipality_data(String province_code){
        address_Area[1].setAdapter(null);
        municipal_code.clear();
        municipal_name.clear();
        API.getClient().getMunicipal(province_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            municipal_code.add(object.getString("MUNICIPAL_CODE"));
                            municipal_name.add(object.getString("MUNICIPAL_NAME"));
                        }
                        municipal_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,municipal_name);
                        SpinnerSetup(address_Area[1],municipal_adapter,"Please Select Municipality");
                        loadBarangay();
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

    protected void loadBarangay(){
        address_Area[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadbarangay_data(municipal_code.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void loadbarangay_data(String municipal_code){
        address_Area[2].setAdapter(null);
        barangay_code.clear();
        barangay_name.clear();
        API.getClient().getBarangay(municipal_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            barangay_code.add(object.getString("MUNICIPAL_CODE"));
                            barangay_name.add(object.getString("MUNICIPAL_NAME"));
                        }
                        barangay_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,barangay_name);
                        SpinnerSetup(address_Area[2],barangay_adapter,"Please Select Barangay");
                        loadBarangay();
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


    private void AddressValidator(){

    }

    private void FormAreaValudator(){

        isChecked(FormArea[0]);
        isChecked(FormArea[1]);
        isChecked(FormArea[2]);
        isChecked(FormArea[3]);


        if(FormArea[0].isChecked() || FormArea[1].isChecked() || FormArea[2].isChecked() || FormArea[3].isChecked()){
            ErrorMessage.setVisibility(View.GONE);
            step(2);
            currentSelectStepView++;
            ShowPager(0,View.GONE);
            ShowPager(1,View.GONE);
            ShowPager(2,View.VISIBLE);
            ShowPager(3,View.GONE);
            ShowPager(4,View.GONE);
            BounceView.addAnimTo(btn_trigger[0]);
        }
        else{
            ErrorCode("Please Check at least 1 of the following");
        }
    }

    protected void isChecked(CheckBox checkBox){
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                ErrorMessage.setVisibility(View.GONE);
            }
        });
    }

    private void businessAreaValidator(){
            if(business_Area[0].getSelectedItem() == null){
                business_Area[0].performClick();
                ErrorCode("Please Select Org Code");
                Toast.makeText(this, "Please Select Org Code", Toast.LENGTH_SHORT).show();

            }
            else if(business_Area[1].getSelectedItem() == null){
                business_Area[1].performClick();
                ErrorCode("Please Select Org Name");
                Toast.makeText(this, "Please Select Org Name", Toast.LENGTH_SHORT).show();
            }
            else{
                step(1);
                currentSelectStepView++;
                ShowPager(0,View.GONE);
                ShowPager(1,View.VISIBLE);
                ShowPager(2,View.GONE);
                ShowPager(3,View.GONE);
                ShowPager(4,View.GONE);
                btn_trigger[0].setVisibility(View.VISIBLE);
                BounceView.addAnimTo(btn_trigger[0]);
            }
    }

    private void ErrorCode(String err){
        ErrorMessage.setVisibility(View.VISIBLE);
        ErrorMessage.setText(err);
    }

    public void back(View view) {
        data.intent(inv_form.class,  this);
        finish();
    }

    private void step(int i){
        stepView.go(i,true);
    }

    private void ShowPager(int position,int v){
        layoutView[position].setVisibility(v);
    }


    private void loadFarmOrg(){
        data.Preloader(this,"Please wait...");
        Org_code_list.clear();
        API.getClient().getFarm_org().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            Org_code_list.add(object.getString("org_code"));
                        }
                        adapter_org_code = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,Org_code_list);
                        SpinnerSetup(business_Area[0],adapter_org_code,"Please Select Org Code");
                        data.loaddialog.dismiss();
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


    private void SpinnerSetup(SearchableSpinner spinner,ArrayAdapter adapter,String Title){
        spinner.setTitle(Title);
        spinner.setAdapter(adapter);

        OrgOnclick();
    }

    private void OrgOnclick(){
        business_Area[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ErrorMessage.setVisibility(View.GONE);
                business_Area[1].setAdapter(null);
                Business_area_code[0].setText(null);
                Business_area_code[1].setText(null);
                getOrgNameListAPI(Org_code_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getOrgNameListAPI(String org_code){
        Org_name_list.clear();
        API.getClient().getFarm_name(org_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            Org_name_list.add(object.getString("org_name"));
                            Business_area_code[0].setText(object.getString("bu_type"));
                            Business_area_code[1].setText(object.getString("bu_code"));
                            Org_bu_type_list.add(object.getString("bu_type"));
                            Org_bu_code_list.add(object.getString("bu_code"));
                        }
                        adapter_org_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,Org_name_list);
                        SpinnerSetup(business_Area[1],adapter_org_name,"Please Select Org Code");

                        business_Area[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ErrorMessage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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






}