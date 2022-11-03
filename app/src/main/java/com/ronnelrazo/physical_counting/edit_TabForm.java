package com.ronnelrazo.physical_counting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_editPDF;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter_edit;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder_edit;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_edit_list;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class edit_TabForm extends AppCompatActivity {


    public static String Getorg_code,Getfarm_code;
    public static String getAudit_no;

    public static int intentType = 0;

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;



    @BindViews({R.id.types,R.id.orgcode,R.id.orgname,R.id.farmcode,R.id.farmname,R.id.docDate,R.id.auditDate})
    TextView[] headerDetails;

    @BindViews({R.id.save,R.id.cancel})
    public MaterialButton[] btn_func;


    private int RowCounter = 0;

    public int REQ_CODE_IMG = 2021;
    public String Attach_Type = "";
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tab_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        getHeader(Getorg_code,Getfarm_code);


//        tabs.addTab(tabs.newTab().setText("Contact"));
        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        API.getClient().getfarmname_header(Getorg_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");
                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            headerDetails[4].setText(object.getString("FARM_NAME")); //HERE

                        }

                    }
                    else{
                        Toast.makeText(edit_TabForm.this, "Connection error", Toast.LENGTH_SHORT).show();
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



        TabLayoutAdapter_edit adapter = new TabLayoutAdapter_edit(this,getSupportFragmentManager(),tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(5);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private String PDFtoString2(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(pdfBytes,Base64.DEFAULT);
    }

    public String getBitmap(Uri uri){
        String result = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void getHeader(String org_code,String farm_code) {
        Log.d("swine",org_code + " " + farm_code);
        API.getClient().edit_header(org_code,farm_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("header");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            headerDetails[0].setText(object.getString("type_type"));
                            headerDetails[1].setText(object.getString("org_code"));
                            headerDetails[2].setText(object.getString("org_name"));
                            headerDetails[3].setText(object.getString("farm_code"));
                            Tab_breeder_edit.Farm_code = object.getString("farm_code");
//                            headerDetails[4].setText(object.getString("farm_name"));
                            headerDetails[5].setText(object.getString("doc_date"));
                            headerDetails[6].setText(object.getString("audit_date"));


                        }


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

    public void savechanges(View view) {
        if(intentType == 1){
            data.intent(transaction.class,view.getContext());
            finish();
        }
        else{
            data.intent(Edit_pdf.class,view.getContext());
            finish();
        }


//        data.Confirmation_upload(view.getContext(),"Do you want to upload Attach Files?",R.drawable.ic_icons8_warning);
//        data.negative.setOnClickListener(v1 ->{
//            data.ConfirmDialog.dismiss();
//        });
//        data.breeder.setOnClickListener(v1 -> {
//            Attach_Type = "breeder";
//            Intent intent = new Intent( );
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//        });
//        data.feed.setOnClickListener(v1 -> {
//            Attach_Type = "feed";
//            Intent intent = new Intent( );
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//        });
//        data.med.setOnClickListener(v1 -> {
//            Attach_Type = "med";
//            Intent intent = new Intent( );
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//        });
//        data.negative.setText("No");
//        data.negative.setOnClickListener(v1 -> {
//            data.intent(Edit_pdf.class,view.getContext());
//            finish();
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent d) {
        super.onActivityResult(requestCode, resultCode, d);
        if (resultCode == RESULT_OK) {
            data.Preloader(this,"File Uploading.\nPlease wait");
            switch (requestCode) {
                case 2021:
                    if (d != null) {
                        if (d.getClipData() != null){
                            for (int i = 0; i < d.getClipData().getItemCount(); i++) {
                                Uri uri = d.getClipData().getItemAt(i).getUri();
                                File myFile = new File(uri.toString());
                                getBitmap(uri);
                                String path = myFile.getAbsolutePath();
                                String pdfName = getFileName(uri);
                                String pdfToString2 = PDFtoString2(bitmap);
                                if(!(i + 1 < d.getClipData().getItemCount())) {
                                    UploadPDFFile(getAudit_no,pdfName,pdfToString2,"last",Attach_Type);
                                }
                                else{
                                    UploadPDFFile(getAudit_no,pdfName,pdfToString2,"more",Attach_Type);
                                }


                            }

                        }else{
                            Uri uri = d.getData();
                            File myFile = new File(uri.toString());
                            getBitmap(uri);
                            String path = myFile.getAbsolutePath();
                            String pdfName = getFileName(uri);
                            String pdfToString2 = PDFtoString2(bitmap);
                            UploadPDFFile(getAudit_no,pdfName,pdfToString2,"last",Attach_Type);
                        }
                    }
                    break;


            }
        }
    }




    public void UploadPDFFile(String audit, String name, String file, String last,String attach_Type){
        API.getClient().uploadPDFFiles(audit,name,file,sharedPref.getUser(),attach_Type).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        if(last.equals("last")){
                            data.loaddialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Successfully!", Toast.LENGTH_SHORT).show();
                            data.ConfirmDialog.dismiss();
                        }
                        else{

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(intentType == 1){
            Globalfunction.getInstance(this).intent(transaction.class,this);
            finish();
        }
    }

    public void cancel(View view) {

        if(intentType == 1){
            data.Confirmation(view.getContext(),"Are you sure you want to exit this Transaction?",R.drawable.ic_icons8_warning);
            data.positive.setText(" Yes");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
                data.intent(transaction.class,this);
                finish();
            });

        }
        else{
            data.Confirmation(view.getContext(),"Are you sure you want to exit this Transaction?",R.drawable.ic_icons8_warning);
            data.positive.setText(" Yes");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
//           data.flag(getAudit_no,"C");
//            data.toast(R.raw.checked,"Transaction NO : " + getAudit_no + " has been Cancelled.", Gravity.TOP|Gravity.CENTER,0,50);
                data.intent(inv_form.class,v1.getContext());
                finish();
            });
        }


    }
}