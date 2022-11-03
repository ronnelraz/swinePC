package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hariprasanths.bounceview.BounceView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_FarmDetails;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_Farm_setup_details;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.shuhart.stepview.StepView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
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
    public List<String> Org_code_list = new ArrayList<>();
    public List<String> Org_code_list_master = new ArrayList<>();
    public List<String> Selected_org_name_list = new ArrayList<>();
    public String SelectedOrg_code = null;

    @BindView(R.id.bu_type) SearchableSpinner bu_type;
    public List<String> getbu_type_list = new ArrayList<>();
    public List<String> Selected_bu_type_list_name = new ArrayList<>();


    public List<String> Org_name_list = new ArrayList<>();
    private List<String> Org_bu_type_list = new ArrayList<>();
    private List<String> Org_bu_code_list = new ArrayList<>();

    @BindViews({R.id.org_code,R.id.org_name})
    public SearchableSpinner[]  business_Area;
    @BindViews({R.id.business_type,R.id.business_code})
    TextView[] Business_area_code;
    public ArrayAdapter adapter_org_code;
    ArrayAdapter adapter_org_name;


    @BindViews({R.id.checklist,R.id.breeder,R.id.feed,R.id.med})
    public CheckBox[] FormArea;

    @BindView(R.id.org_name_text) TextView org_code_name;

    @BindViews({R.id.province,R.id.municipality,R.id.barangay})
    public SearchableSpinner[] address_Area;
    @BindViews({R.id.zipcode,R.id.lati,R.id.longti})
    public EditText[] address_Area_edit;
    public List<String> province_code = new ArrayList<>();
    public List<String>province_name = new ArrayList<>();
    public List<String> region_code = new ArrayList<>();
    ArrayAdapter province_adapter;

    public List<String> municipal_code= new ArrayList<>();
    public List<String> municipal_name= new ArrayList<>();
    ArrayAdapter municipal_adapter;

    public List<String> barangay_code= new ArrayList<>();
    public List<String> barangay_name= new ArrayList<>();
    ArrayAdapter barangay_adapter;

    @BindViews({R.id.municipalityLoading,R.id.barangayLoading}) SpinKitView[] loading;

    @BindViews({R.id.farmcode,R.id.farmname,R.id.farmcontact,R.id.farmemail}) public EditText[] farmManager;
    @BindViews({R.id.clerkcode,R.id.clerkname,R.id.clerkcontact,R.id.clerkemail}) public EditText[] clerkManager;

    private int Save = 0;
    public boolean modify = false;

    public AlertDialog alertDialog;


    //modal
    private EditText search;
    private RecyclerView farmlist;
    private RecyclerView.Adapter adapter;
    private List<modal_Farm_setup_details> list =  new ArrayList<>();

    //dynamic data depend on the item
    public static String item_selected_org_code;
    public static String ORG_CODE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_setup);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        loadFarmOrg();
        loadBuType_list();
        loadAddressProvince(); //load Province


        step(currentSelectStepView);
        btn_trigger[0].setOnClickListener(v -> {
            Save = 0;
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
                FarmManagerValidator();
            }
            else if(currentSelectStepView == 4){
                if(Save == 0){
                    ClerkManagerValidator();
                }
                else{
                        SaveFarmSetup(v);


                }
            }

        });


        stepViewClick();
    }


    protected void stepViewClick(){
        stepView.setOnStepClickListener(step -> {
               switch (step){
                   case 0:
                       businessAreaValidator();
                       break;
                   case 1:
                       FormAreaValudator();
                       break;
                   case 2:
                       AddressValidator();
                       break;
                   case 3:
                       FarmManagerValidator();
                       break;
                   case 4:
                       ClerkManagerValidator();
                       break;
               }
        });
    }

    protected void SaveFarmSetup(View v){
        String getOrgcode = SelectedOrg_code;//business_Area[0].getSelectedItem().toString();
        String getOrgName = org_code_name.getText().toString(); //business_Area[1].getSelectedItem().toString();
        String getBu_type = Business_area_code[0].getText().toString();
        String getBu_code = Business_area_code[1].getText().toString();
        String getChecklist = FormArea[0].isChecked() ? "Y" : "N";
        String getBreeder = FormArea[1].isChecked() ? "Y" : "N";
        String getFeed = FormArea[2].isChecked() ? "Y" : "N";
        String getMed = FormArea[3].isChecked() ? "Y" : "N";
        String getProvince = address_Area[0].getSelectedItem().toString();
        String getMunicipal =  address_Area[1].getSelectedItem().toString();
        String getBarangay =  address_Area[2].getSelectedItem().toString();
        String getProvinceCode = province_code.get(address_Area[0].getSelectedItemPosition());
        String getMunicipalCode =  municipal_code.get(address_Area[1].getSelectedItemPosition()).replaceFirst("C","M");
        String getBarangayCode =   barangay_code.get(address_Area[2].getSelectedItemPosition());
        String getZipCode = address_Area_edit[0].getText().toString();
        String getlati = address_Area_edit[1].getText().toString();
        String getlongi = address_Area_edit[2].getText().toString();
        String getFarmcode = farmManager[0].getText().toString();
        String getFarmName = farmManager[1].getText().toString();
        String getFarmContact = farmManager[2].getText().toString();
        String getFarmMail = farmManager[3].getText().toString();
        String getClerkcode = clerkManager[0].getText().toString();
        String getClerkName = clerkManager[1].getText().toString();
        String getClerkContact = clerkManager[2].getText().toString();
        String getClerkMail = clerkManager[3].getText().toString();

        if(modify){
            data.Confirmation(v.getContext(),"Are you sure you want to Save the new update?",R.drawable.ic_icons8_info);
            data.negative.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
            });
            data.positive.setText("Save Changes");
            data.positive.setOnClickListener(v1 -> {
                modifyFarm(getOrgcode, getOrgName, getBu_type, getBu_code, getChecklist, getBreeder, getFeed, getMed, getProvince, getMunicipal, getBarangay, getZipCode, getProvinceCode, getMunicipalCode, getBarangayCode, getlati, getlongi, getFarmcode, getFarmName, getFarmContact, getFarmMail, getClerkcode,
                        getClerkName, getClerkContact, getClerkMail,ORG_CODE_ID);
            });
        }
        else{
            Log.d("swine", getOrgcode + " " + getOrgName + " " + getBu_type + " " + getBu_code + " " + getChecklist + " " + getBreeder + " " + getFeed + " " + getMed + " " +getProvince + " " +
                getMunicipal + " " + getBarangay + " " + getZipCode + " " + getProvinceCode + " " + getMunicipalCode + " " + getBarangayCode + " " + getZipCode + " " + getlati + " " + getlongi + " " +getFarmcode + " " + getFarmName + " " + getFarmContact + " " +getFarmMail + " " + getClerkcode + " " +
                getClerkName + " " + getClerkContact + " " + getClerkMail);


            data.Confirmation(v.getContext(),"Are you sure you want to Save this new Setup?",R.drawable.ic_icons8_info);
            data.negative.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                saveFarmSetup(getOrgcode, getOrgName, getBu_type, getBu_code, getChecklist, getBreeder, getFeed, getMed, getProvince, getMunicipal, getBarangay, getZipCode, getProvinceCode, getMunicipalCode, getBarangayCode, getlati, getlongi, getFarmcode, getFarmName, getFarmContact, getFarmMail, getClerkcode,
                    getClerkName, getClerkContact, getClerkMail);
            });

        }



    }

    protected void modifyFarm(String getOrgcode, String getOrgName, String getBu_type, String getBu_code, String getChecklist, String getBreeder, String getFeed, String getMed, String getProvince, String
            getMunicipal, String getBarangay, String getZipCode, String getProvinceCode, String getMunicipalCode, String getBarangayCode, String getlati, String getlongi, String getFarmcode, String getFarmName, String getFarmContact, String getFarmMail, String getClerkcode, String
                                         getClerkName, String getClerkContact, String getClerkMail, String getORG_CODE_ID){

        data.loading("Please wait");
        API.getClient().modify_farm_details(getOrgcode,getOrgName,
                getBu_code,getBu_type,
                getChecklist,getBreeder,getFeed,getMed,
                getBarangay + ", " + getMunicipal + ", " + getProvince,getProvinceCode,getMunicipalCode,getBarangayCode,getZipCode,
                getFarmcode,getFarmName,getFarmContact,getFarmMail,
                getClerkcode,getClerkName,getClerkContact,getClerkMail,
                getlongi,getlati,sharedPref.getUser(),getORG_CODE_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Log.d("swine","Save : " + success);
                        data.toast(R.raw.checked,"Updated Successfully!", Gravity.BOTTOM|Gravity.CENTER,0,50);
                        data.ConfirmDialog.dismiss();
                        data.pDialog.dismiss();
                        new Handler().postDelayed(() -> {
                            clearData();
                        },2000);
                    }
                    else{
                        Log.d("swine","Save : " + success);
                        data.toast(R.raw.error,"Data Already Exists. Please contact IT Department For more information.", Gravity.BOTTOM|Gravity.CENTER,0,50);
                        data.ConfirmDialog.dismiss();
                        data.pDialog.dismiss();
                        clearData();

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


    protected void saveFarmSetup(String getOrgcode, String getOrgName, String getBu_type, String getBu_code, String getChecklist, String getBreeder, String getFeed, String getMed, String getProvince, String
            getMunicipal, String getBarangay, String getZipCode, String getProvinceCode, String getMunicipalCode, String getBarangayCode, String getlati, String getlongi, String getFarmcode, String getFarmName, String getFarmContact, String getFarmMail, String getClerkcode, String
                                         getClerkName, String getClerkContact, String getClerkMail){


        Log.d("save_swine",getOrgcode + " " +getOrgName + " " +
                getBu_code + " " +getBu_type + " " +
                getChecklist + " " +getBreeder + " " +getFeed + " " +getMed + " " +
                getBarangay + "  " + getMunicipal + " " + getProvince + " " +getProvinceCode + " " +getMunicipalCode + " " +getBarangayCode + " " +getZipCode + " " +
                getFarmcode + " " +getFarmName + " " +getFarmContact + " " +getFarmMail + " " +
                getClerkcode + " " +getClerkName + " " +getClerkContact + " " +getClerkMail + " " +
                getlongi + " " +getlati + " " + sharedPref.getUser() );

        data.loading("Please wait");
        API.getClient().Save_FarmSetup(getOrgcode,getOrgName,
                getBu_code,getBu_type,
                getChecklist,getBreeder,getFeed,getMed,
                getBarangay + ", " + getMunicipal + ", " + getProvince,getProvinceCode,getMunicipalCode,getBarangayCode,getZipCode,
                getFarmcode,getFarmName,getFarmContact,getFarmMail,
                getClerkcode,getClerkName,getClerkContact,getClerkMail,
                getlongi,getlati,sharedPref.getUser()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Log.d("swine","Save : " + success);
                        data.toast(R.raw.checked,"Saved Successfully!", Gravity.BOTTOM|Gravity.CENTER,0,50);
                        data.ConfirmDialog.dismiss();
                        data.pDialog.dismiss();
                        new Handler().postDelayed(() -> {
                            clearData();
                        },2000);
                    }
                    else{
                        Log.d("swine","Save : " + success);
                        data.toast(R.raw.error,"Data Already Exists. Please contact IT Department For more information.", Gravity.BOTTOM|Gravity.CENTER,0,50);
                        data.ConfirmDialog.dismiss();
                        data.pDialog.dismiss();
                        clearData();

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


    private void clearData(){
        data.intent(Farm_setup.class,this);
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
        initLoading(loading[0]);
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
                        LoadingHide(loading[0]);
                    }
                    else{
                        LoadingHide(loading[0]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                    LoadingHide(loading[0]);

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
                Log.d("swine",municipal_code.get(position));
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
        initLoading(loading[1]);
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
                            barangay_code.add(object.getString("BARANGAY_CODE"));
                            barangay_name.add(object.getString("BARANGAY_NAME"));
                        }
                        barangay_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,barangay_name);
                        SpinnerSetup(address_Area[2],barangay_adapter,"Please Select Barangay");
                        loadBarangay();
                        LoadingHide(loading[1]);

                    }
                    else{
                        LoadingHide(loading[1]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                    loading[0].setVisibility(View.GONE);
                    LoadingHide(loading[1]);

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

    protected void initLoading(SpinKitView spinKitView){
        spinKitView.setVisibility(View.VISIBLE);
        Sprite circle = new Circle();
        spinKitView.setIndeterminateDrawable(circle);
    }
    protected void LoadingHide(SpinKitView spinKitView){
        spinKitView.setVisibility(View.GONE);
    }


    private void ClerkManagerValidator(){
        clerkManager[1].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";
        String farmNamePattern = "[a-zA-Z ]+";
        if(clerkManager[0].getText().toString().isEmpty()){
            clerkManager[0].requestFocus();
            Toast.makeText(this, "Please Enter Clerk manager Code.", Toast.LENGTH_SHORT).show();
        }
        else if(clerkManager[1].getText().toString().isEmpty()){
            clerkManager[1].requestFocus();
            Toast.makeText(this, "Please Enter Clerk manager Name.", Toast.LENGTH_SHORT).show();
        }
        else if(!clerkManager[1].getText().toString().trim().matches(farmNamePattern)){
            clerkManager[1].requestFocus();
            Toast.makeText(this, "Please Enter Valid Clerk Manager Name", Toast.LENGTH_SHORT).show();
        }
        else if(clerkManager[2].getText().toString().isEmpty()){
            clerkManager[2].requestFocus();
            Toast.makeText(this, "Please Enter Clerk manager Contact.", Toast.LENGTH_SHORT).show();
        }
        else if(clerkManager[2].getText().toString().length() <= 10){
            clerkManager[2].requestFocus();
            Toast.makeText(this, "Please Invalid Clerk manager Contact.", Toast.LENGTH_SHORT).show();
        }
        else if(clerkManager[3].getText().toString().isEmpty()){
            clerkManager[3].requestFocus();
            Toast.makeText(this, "Please Enter Clerk manager Email.", Toast.LENGTH_SHORT).show();
        }
        else if(!clerkManager[3].getText().toString().trim().matches(emailPattern)){
            clerkManager[3].requestFocus();
            Toast.makeText(this, "Please Enter Valid email Address", Toast.LENGTH_SHORT).show();
        }
        else{
            ErrorMessage.setVisibility(View.GONE);
            step(5);
            currentSelectStepView++;
            ShowPager(0,View.GONE);
            ShowPager(1,View.GONE);
            ShowPager(2,View.GONE);
            ShowPager(3,View.GONE);
            ShowPager(4,View.VISIBLE);
            BounceView.addAnimTo(btn_trigger[0]);
        }

    }


    private void FarmManagerValidator(){
        farmManager[1].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";
        String farmNamePattern = "[a-zA-Z. ]+";
        if(farmManager[0].getText().toString().isEmpty()){
            farmManager[0].requestFocus();
            Toast.makeText(this, "Please Enter Farm manager Code.", Toast.LENGTH_SHORT).show();
        }
        else if(farmManager[1].getText().toString().isEmpty()){
            farmManager[1].requestFocus();
            Toast.makeText(this, "Please Enter Farm manager Name.", Toast.LENGTH_SHORT).show();
        }
        else if(!farmManager[1].getText().toString().trim().matches(farmNamePattern)){
            farmManager[1].requestFocus();
            Toast.makeText(this, "Please Enter Valid Farm Manager Name", Toast.LENGTH_SHORT).show();
        }
        else if(farmManager[2].getText().toString().isEmpty()){
            farmManager[2].requestFocus();
            Toast.makeText(this, "Please Enter Farm manager Contact.", Toast.LENGTH_SHORT).show();
        }
        else if(farmManager[2].getText().toString().length() <= 10){
            farmManager[2].requestFocus();
            Toast.makeText(this, "Please Invalid Farm manager Contact.", Toast.LENGTH_SHORT).show();
        }
        else if(farmManager[3].getText().toString().isEmpty()){
            farmManager[3].requestFocus();
            Toast.makeText(this, "Please Enter Farm manager Email.", Toast.LENGTH_SHORT).show();
        }
        else if(!farmManager[3].getText().toString().trim().matches(emailPattern)){
            farmManager[3].requestFocus();
            Toast.makeText(this, "Please Enter Valid email Address", Toast.LENGTH_SHORT).show();
        }
        else{
            ErrorMessage.setVisibility(View.GONE);
            step(4);
            currentSelectStepView++;
            ShowPager(0,View.GONE);
            ShowPager(1,View.GONE);
            ShowPager(2,View.GONE);
            ShowPager(3,View.GONE);
            ShowPager(4,View.VISIBLE);
            btn_trigger[1].setText("Save");
            Save = 1;
            BounceView.addAnimTo(btn_trigger[0]);
        }

    }



    private void AddressValidator(){
        if(address_Area[0].getSelectedItem() == null){
            address_Area[0].performClick();
            ErrorCode("Please Select Province");
            Toast.makeText(this, "Please Select Province", Toast.LENGTH_SHORT).show();

        }
        else if(address_Area[1].getSelectedItem() == null){
            address_Area[1].performClick();
            ErrorCode("Please Select Municipality");
            Toast.makeText(this, "Please Select Municipality", Toast.LENGTH_SHORT).show();
        }
        else if(address_Area[2].getSelectedItem() == null){
            address_Area[2].performClick();
            ErrorCode("Please Select Barangay");
            Toast.makeText(this, "Please Select Barangay", Toast.LENGTH_SHORT).show();
        }
        else{
          ErrorMessage.setVisibility(View.GONE);
            step(3);
            currentSelectStepView++;
            ShowPager(0,View.GONE);
            ShowPager(1,View.GONE);
            ShowPager(2,View.GONE);
            ShowPager(3,View.VISIBLE);
            ShowPager(4,View.GONE);
            BounceView.addAnimTo(btn_trigger[0]);
        }
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
//            else if(business_Area[1].getSelectedItem() == null){
//                business_Area[1].performClick();
//                ErrorCode("Please Select Org Name");
//                Toast.makeText(this, "Please Select Org Name", Toast.LENGTH_SHORT).show();
//            }
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

    //razo
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
                            Org_code_list.add(object.getString("org_code") + " - " + object.getString("org_name"));
                            Org_code_list_master.add(object.getString("org_code"));
                            Selected_org_name_list.add(object.getString("org_name"));

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


    private void loadBuType_list(){
        getbu_type_list.clear();
        Selected_bu_type_list_name.clear();
        API.getClient().getAUDIT_BUSINESS_TYPE().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            getbu_type_list.add(object.getString("code") + " - " + object.getString("name"));
                            Selected_bu_type_list_name.add(object.getString("code"));

                        }
                        adapter_org_code = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,getbu_type_list);
                        SpinnerSetup(bu_type,adapter_org_code,"Please Select Org Code");
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
        OrgOnclick_butype();
    }

    private void OrgOnclick_butype(){
        bu_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Farm_setup.this, Selected_bu_type_list_name.get(position), Toast.LENGTH_SHORT).show();
                Business_area_code[0].setText(Selected_bu_type_list_name.get(position));
//                getOrgNameListAPI(Org_code_list_master.get(position));
//                org_code_name.setText(Selected_org_name_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void OrgOnclick(){
        business_Area[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ErrorMessage.setVisibility(View.GONE);
                business_Area[1].setAdapter(null);
                Business_area_code[0].setText(null);
                Business_area_code[1].setText(null);
                SelectedOrg_code = Org_code_list_master.get(position);
                getOrgNameListAPI(Org_code_list_master.get(position));
                org_code_name.setText(Selected_org_name_list.get(position));
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






    public void menu(View v) {
        modify = true;
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.farm_detials_list,null);
        MaterialButton back = view.findViewById(R.id.back);
        back.setOnClickListener(v2 -> {
            alertDialog.dismiss();
        });
        search = view.findViewById(R.id.searchFarmlist);
        farmlist = view.findViewById(R.id.farmlistData);

        farmlist.setHasFixedSize(true);
        farmlist.setItemViewCacheSize(999999999);
        farmlist.setLayoutManager(new LinearLayoutManager(v.getContext()));
        adapter = new Adapter_FarmDetails(list,v.getContext(),modify);
        farmlist.setAdapter(adapter);

        getFarmListDetials(view);
        SearchDataModal(search,farmlist);

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        BounceView.addAnimTo(alertDialog);
        alertDialog.show();
    }

    private void SearchDataModal(EditText search,RecyclerView farmlist){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                ArrayList<modal_Farm_setup_details> newList = new ArrayList<>();
                for (modal_Farm_setup_details sub : list)
                {
                    String code = sub.getORG_CODE().toLowerCase();
                    String name = sub.getORG_NAME().toLowerCase();
                    String address = sub.getADDRESS().toLowerCase();
                    if(name.contains(s) || code.contains(s) || address.contains(s)){
                        newList.add(sub);
                    }
                    adapter = new Adapter_FarmDetails(newList, getApplicationContext(),modify);
                    farmlist.setAdapter(adapter);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getFarmListDetials(View v) {
        list.clear();
        API.getClient().get_FarmSetup().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            modal_Farm_setup_details item = new modal_Farm_setup_details(
                                    object.getString("ORG_CODE"),
                                    object.getString("ORG_NAME"),
                                    object.getString("BUSINESS_GROUP_CODE"),
                                    object.getString("BUSINESS_TYPE_CODE"),
                                    object.getString("CHECKLIST_AUDIT_FLAG"),
                                    object.getString("BREEDER_AUDIT_FLAG"),
                                    object.getString("FEED_AUDIT_FLAG"),
                                    object.getString("MED_AUDIT_FLAG"),
                                    object.getString("ADDRESS"),
                                    object.getString("PROVINCE_CODE"),
                                    object.getString("MUNICIPALITY_CODE"),
                                    object.getString("BARANGAY_CODE"),
                                    object.getString("ZIP_CODE") == null ? "none" : object.getString("ZIP_CODE"),
                                    object.getString("FARM_MANAGER_CODE"),
                                    object.getString("FARM_MANAGER_NAME"),
                                    object.getString("FARM_MANAGER_CONTACT_NO"),
                                    object.getString("FARM_MANAGER_EMAIL"),
                                    object.getString("FARM_CLERK_CODE"),
                                    object.getString("FARM_CLERK_NAME"),
                                    object.getString("FARM_CLERK_CONTACT_NO"),
                                    object.getString("FARM_CLERK_EMAIL"),
                                    object.getString("STR_LONGITUDE"),
                                    object.getString("STR_LATITUDE")
                            );

                            list.add(item);


                        }

                        adapter = new Adapter_FarmDetails(list,getApplicationContext(),modify);
                        farmlist.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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