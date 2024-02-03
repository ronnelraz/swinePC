package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_menu_list;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.APIDownload;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_menu;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class inv_form extends AppCompatActivity {

    private static final int INSTALL_PERMISSION_REQUEST_CODE = 123;
    private Globalfunction data;
    private SharedPref sharedPref;



    @BindViews({R.id.username,R.id.role,R.id.org_code,R.id.farm_code})
    TextView[] JWTauth;

    @BindView(R.id.logout)
    MaterialButton logout;

    private String[] getMenuAccessArray;

    @BindView(R.id.gridlayout)
    RecyclerView gridlayout;

    List<modal_menu> menulist = new ArrayList<>();
    RecyclerView.Adapter adapter;

    String[] menu_array = new String[]{
            "TRANSACTION LIST",
            "CREATE",
            "EDIT",
            "CONFIRM",
            "CANCEL",
            "GENERATE PDF",
            "UPLOAD FILE",
            "FARM SETUP",
            "USER SETUP",
            "REPORT"
    };

    int[] menu_icon = new int[]{
            R.drawable.ic_icons8_document,R.drawable.create,
            R.drawable.edit,R.drawable.checked,R.drawable.ic_icons8_cancel,
            R.drawable.icons8_pdf_1,R.drawable.ic_icons8_import_file,
            R.drawable.icons8_smart_home_checked,R.drawable.icons8_add_administrator,
            R.drawable.ic_icons8_report_file
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        getMenuAccessArray = sharedPref.getJwtMenuAccess().split(", ");

        if( sharedPref.getJwtMenuAccess().equals("0")){
            for(int i = 0; i < menu_array.length; i++){
                modal_menu list = new modal_menu(
                        i,
                        menu_icon[i],
                        menu_array[i]
                );
                menulist.add(list);
            }
        }
        else{
            for(int i = 0; i < getMenuAccessArray.length; i++){
                int x =   Integer.parseInt(getMenuAccessArray[i].trim()) - 1;
                modal_menu list = new modal_menu(
                        x,
                        menu_icon[x],
                        menu_array[x]
                );
                menulist.add(list);
            }
        }

//

//



        GridLayoutManager layout_manager = new GridLayoutManager(this, 2);
        layout_manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0 ? 2 : 1);
            }
        });
        adapter = new Adapter_menu_list(menulist,this);
        gridlayout.setLayoutManager(layout_manager);
        gridlayout.setHasFixedSize(true);
        gridlayout.setItemViewCacheSize(999999999);
        gridlayout.setAdapter(adapter);




        JWTauth[0].setText("User : " +sharedPref.getUser());
        JWTauth[1].setText("Role : " +sharedPref.getRole() + " - " + sharedPref.getBU());
        if(sharedPref.getOrg_code().equals("0000")){
            JWTauth[2].setVisibility(View.GONE);
            JWTauth[3].setVisibility(View.GONE);
        }
        else{
            JWTauth[2].setVisibility(View.GONE);
            JWTauth[3].setVisibility(View.GONE);
            JWTauth[2].setText("Org Code : " +sharedPref.getOrg_code());
            JWTauth[3].setText("Farm Code : " +sharedPref.getFarm_code());
        }



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



    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkVersion();

    }
    protected void checkVersion() {
        Log.d("Razo","ok");
        // Check if the permission is already granted
        if (checkInstallPermission()) {
            Log.d("Razo","enabled");
            // Permission already granted, proceed with download
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File apkFile = new File(downloadsDir, "PhysicalCount.apk");

            if (apkFile.exists()) {
                boolean deleted = apkFile.delete();
                if (!deleted) {
                    Log.e("Razo", "Failed to delete existing APK file");
                    // Handle the case where deletion fails (optional)
                }
            }
            startdownloadNewUpdate();
        } else {
            Log.d("Razo","not enabled");
            // Permission not granted, show the permission dialog
            requestInstallPermission();
        }
    }

    private boolean checkInstallPermission() {
        // Check if the app can request installation of packages
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getPackageManager().canRequestPackageInstalls();
        } else {
            // If the device is running a version older than Oreo, the permission is granted by default
            return true;
        }
//        return false;
    }



    private void requestInstallPermission() {
        // If the permission is not granted, show the permission dialog
        Intent installPermissionIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(installPermissionIntent, INSTALL_PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INSTALL_PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // The user has granted the permission
                // You can perform additional actions here if needed
                Log.d("InstallPermission", "Permission granted");
                startdownloadNewUpdate();
            } else {
                // The user has not granted the permission
                // You can handle this case as needed
                Log.d("InstallPermission", "Permission not granted");

            }
        }
    }



    protected void startdownloadNewUpdate(){
        Log.d("razo", "call startdownloadNewUpdate");
        APIDownload.getClient().download().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    int version = jsonResponse.getInt("version");
                    String url = jsonResponse.getString("url");



                    if(success){

                        int newversion = version;
                        int appversion = BuildConfig.VERSION_CODE;

                        if (newversion > appversion) {
                            Log.d("razo", newversion + " " + appversion);
                            Log.d("razo", "new update");
                            new UpdateTask().execute(url);
                        }
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
                    data.loaddialog.dismiss();
                }
            }
        });


    }




    private class UpdateTask extends AsyncTask<String, Integer, File> {
        AlertDialog.Builder builder;
        ProgressBar progressBar;

        AlertDialog progressDialog;
        private int fileLength = -1;

        TextView textViewPercentage;

        private static final int INSTALL_PERMISSION_REQUEST_CODE = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder = new AlertDialog.Builder(inv_form.this);
            LayoutInflater inflater = getLayoutInflater();
            View customView = inflater.inflate(R.layout.custom_progress_dialog, null);

            progressBar = customView.findViewById(R.id.progressBar);
            TextView textViewMessage = customView.findViewById(R.id.textViewMessage);
            textViewMessage.setText("Downloading new update...");
            textViewPercentage = customView.findViewById(R.id.textViewPercentage);


            builder.setView(customView);
            builder.setCancelable(false);

            progressDialog = builder.create();
            progressDialog.show();
        }

        @Override
        protected File doInBackground(String... urls) {
            if (urls.length == 0) {
                Log.e("UpdateTask", "No URL provided for download");
                return null; // No URL provided
            }

            try {
                Log.d("UpdateTask", "Start download from URL: " + urls[0]);

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true); // Follow redirects
                connection.setConnectTimeout(Integer.MAX_VALUE);
                connection.setReadTimeout(Integer.MAX_VALUE);
                connection.connect();

                fileLength = connection.getContentLength();

                if (fileLength != -1) {
                    progressBar.setMax(100);
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Ensure the Downloads directory exists
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (downloadsDir == null) {
                        Log.e("UpdateTask", "Downloads directory is null");
                        return null;
                    }


                    File apkFile = new File(downloadsDir, "PhysicalCount.apk");

                    if (apkFile.exists()) {
                        boolean deleted = apkFile.delete();
                        if (!deleted) {
                            Log.e("UpdateTask", "Failed to delete existing APK file");
                            // Handle the case where deletion fails (optional)
                        }
                    }

                    Log.d("UpdateTask", "APK File Path: " + apkFile.getAbsolutePath());

                    FileOutputStream fos = new FileOutputStream(apkFile);

                    InputStream is = connection.getInputStream();
//                    byte[] buffer = new byte[1024];
//                    byte[] buffer = new byte[8192];
                    // Use a larger buffer size, for example, 16384 (16 KB)
                    byte[] buffer = new byte[16384];
                    int len;
                    int total = 0;

                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;

                        // Calculate the percentage progress
                        int progress = (fileLength != -1) ? Math.round(((float) total / fileLength) * 100) : total;

                        // Always publish progress when the loop completes
//                        Log.d("progressbar", "loading:" +progress);
                        publishProgress(progress);
                    }

                    fos.close();
                    is.close();

                    return apkFile;
                } else {
                    Log.e("UpdateTask", "HTTP response code is not OK: " + connection.getResponseCode());
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("UpdateTask", "Failed to download update: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Check if progressDialog is not null before updating progress
            if (progressBar != null) {
                int progress = values[0];

                // If fileLength is known, use it for percentage calculation
                if (fileLength != -1) {
                    textViewPercentage.setText(progress+ "%");
                    progressBar.setProgress(progress);
                } else {
                    // If fileLength is unknown, increment progress by a fixed amount
                    progressBar.incrementProgressBy(progress - progressBar.getProgress());
                    textViewPercentage.setText(progress - progressBar.getProgress());
                }
            }
        }

        @Override
        protected void onPostExecute(File apkFile) {
            super.onPostExecute(apkFile);

            // Check if progressDialog is not null and the activity is not finishing before dismissing
            if (progressBar != null && !isFinishing()) {
                progressDialog.dismiss();
            }

            if (apkFile != null) {
                Log.d("UpdateTask", "Download completed. Installing APK.");
                installApk(apkFile,getApplicationContext());
            } else {
                Log.e("UpdateTask", "Failed to download update");
                Toast.makeText(getApplicationContext(), "Failed to download update", Toast.LENGTH_SHORT).show();
            }
        }

        private void installApk(File apkFile, Context context) {
            Uri apkUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", apkFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (context.getPackageManager().canRequestPackageInstalls()) {
                    context.startActivity(intent);
                } else {
                    requestInstallPermission(context); // Request permission to install from unknown sources
                }
            } else {
                context.startActivity(intent);
            }
        }

        private void requestInstallPermission(Context context) {
            Uri packageUri = Uri.parse("package:" + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
            if (context instanceof inv_form) {
                ((inv_form) context).startActivityForResult(intent, INSTALL_PERMISSION_REQUEST_CODE);
            }
        }

    }




}