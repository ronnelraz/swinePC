package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
 
public class Login extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;
    @BindView(R.id.permission)
    TextView str_permision;
    @BindView(R.id.login)
    MaterialButton login;

    @BindView(R.id.verison)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        permission();

        try {
            version.setText("Version : "+this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(sharedPref.checkAuto_login_auth().equals("true")){
            Log.d("swine",sharedPref.checkAuto_login_auth() + " ");
            data.intent(inv_form.class, this);
            finish();
        }
        else{
            Log.d("swine","manual login");
        }
    }




    protected void permission(){

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            str_permision.setText("check Permission : granted");
                            str_permision.setTextColor(Color.parseColor("#2ecc71"));
                            login.setEnabled(true);

                        }
                        // check for permanent denial of any permission
                        else if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            str_permision.setText("check Permission : not ok, permission is denied permenantly.");
                            str_permision.setTextColor(Color.parseColor("#e74c3c"));
                            login.setEnabled(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        str_permision.setText("Check Permission : Error occurred! ");
                        str_permision.setTextColor(Color.parseColor("#e74c3c"));
                        login.setEnabled(false);
                    }
                })

                .onSameThread()
                .check();
    }

    @BindViews({R.id.username,R.id.password})
    TextInputEditText[] input;
    @BindViews({R.id.username_layout,R.id.password_layout})
    TextInputLayout[] input_layout;
    @BindView(R.id.keepme)
    CheckBox keepme;
    public void login(View view) {
        String getusername = input[0].getText().toString();
        String getPassword = input[1].getText().toString();
        String keepmelogin =  keepme.isChecked() ? "true" : "false";

        if(getusername.isEmpty()){
            input_layout[0].setError("Invalid User ID");
            data.inputwatcher(input[0],input_layout[0],"Invalid User ID");
            input[0].requestFocus();
        }
        else if(getPassword.isEmpty()){
            input_layout[1].setError("Invalid Password");
            data.inputwatcher(input[1],input_layout[1],"Invalid Password");
            input[1].requestFocus();
        }
        else{
          data.Preloader(this,"Please wait...");
            API.getClient().loginAPI(getusername.toLowerCase(),getPassword).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean SUCCESS = jsonResponse.getBoolean("success");
                        String MESSAGE = jsonResponse.getString("message");
                        String TOKEN = jsonResponse.getString("token");


                        if(SUCCESS){
                            Log.d("swine",TOKEN);
                            Log.d("swine","Keep me signed in : " + keepmelogin);
                            Log.d("swine",MESSAGE);
                            data.decodeToken(TOKEN,sharedPref);
                            sharedPref.set_login_auth(TOKEN,keepmelogin);
                            data.toast(R.raw.checked,MESSAGE, Gravity.TOP|Gravity.CENTER,0,50); //50
                            data.loaddialog.dismiss();
                            data.intent(inv_form.class,  Login.this);
                            finish();

//                            try {
//                                DecodedJWT jwt = JWT.decode(TOKEN);
//                                String getpayload = jwt.getPayload();
//
//
//                            } catch (JWTDecodeException exception){
//                                //Invalid token
//                                data.toast(R.raw.checked,"Invalid Token", Gravity.TOP|Gravity.CENTER,0,50); //50
//                            }

                        }
                        else{
                            data.toast(R.raw.error,MESSAGE, Gravity.TOP|Gravity.CENTER,0,50); //50
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


    public void loginlocal(View view) {
        Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
    }
}