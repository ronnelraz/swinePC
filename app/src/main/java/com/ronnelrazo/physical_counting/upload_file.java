package com.ronnelrazo.physical_counting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.adapter.Adapter_Cancel_list;
import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.adapter.Adapter_upload_list;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_cancel_list;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.model.model_upload_list;
import com.ronnelrazo.physical_counting.model.model_viewfile;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class upload_file extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.checkedall)
    CheckBox checkedAll;


    @BindView(R.id.org_code_filter)
    AutoCompleteTextView org_code_filter;
    @BindView(R.id.audit_date_filter)
    TextInputEditText audit_date_filter;
    List<String> autocompletelist = new ArrayList<>();

    List<String> autocompletelist_audit = new ArrayList<>();


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<model_upload_list> list =  new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    ArrayAdapter<String> adapterx;

    private String auditno = "";
    private String auditnof = "";


    public int REQ_CODE_IMG = 2021;
    public int REQ_CODE_PDF = 2022;
    public Bitmap bitmap;
    public String Attach_Type = "";

    public Intent intent;
    public boolean ifupdate = false;


    AutoCompleteTextView audit_no;
    AlertDialog ConfirmDialog;

    @BindView(R.id.uploadimg)
    ExtendedFloatingActionButton uploadimg;

    @BindView(R.id.loading)
    LottieAnimationView loading;

    modal_interface modal_interface;
    String adapter_audit_no,adapter_org_code;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        uploadimg.shrink();

        recyclerView = findViewById(R.id.data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_upload_list(list,this,modal_interface);
        recyclerView.setAdapter(adapter);
        String getorg_codes = org_code_filter.getText().toString();
        String getAudit_dates = audit_date_filter.getText().toString();
        LoadFolders(getorg_codes,getAudit_dates);


        AutoCompleteCode(sharedPref.getUser());
        org_code_filter.setOnTouchListener((v, event) -> {
            org_code_filter.showDropDown();
            return false;
        });

        org_code_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getPostion = autocompletelist.get(position).split("\\(")[1];
                String getaudit = getPostion.split("\\)")[0];
                auditnof = getaudit;
            }
        });

        audit_date_filter.setOnClickListener(v-> {
            new DatePickerDialog(v.getContext(),R.style.picker,getDateto(), data.calendar
                    .get(Calendar.YEAR),data.calendar.get(Calendar.MONTH),
                    data.calendar.get(Calendar.DAY_OF_MONTH)).show();


        });


    }

    public DatePickerDialog.OnDateSetListener getDateto(){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            data.calendar.set(Calendar.YEAR, year);
            data.calendar.set(Calendar.MONTH, monthOfYear);
            data.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Formatter();
        };
        return date;
    }

    private void Formatter() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        audit_date_filter.setText(sdf.format(data.calendar.getTime()));
    }

    private void AutoCompleteCode(String user) {
        autocompletelist.clear();
        API.getClient().autoComplete_audit(user,"filter").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            autocompletelist.add(object.getString("org_code"));
                        }

                    }
                    else{

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item ,R.id.autoCompleteItem,autocompletelist);
        org_code_filter.setThreshold(1);
        org_code_filter.setAdapter(adapter);



        modal_interface = new modal_interface() {
            @Override
            public void onClick(View v, String audit, String type, String Org_code) {
                if(type.equals("attachfile")){
                    Intent  filesIntent = new Intent();
                    Attach_Type = type;
                    adapter_audit_no = audit;
                    adapter_org_code = Org_code;
                    filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.setType("application/pdf");
                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(filesIntent, REQ_CODE_PDF);
                }

                else if(type.equals("checklist")){
                    Intent  filesIntent = new Intent();
                    Attach_Type = type;
                    adapter_audit_no = audit;
                    adapter_org_code = Org_code;
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.setType("application/pdf");
                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(filesIntent, REQ_CODE_PDF);
                }
                else if(type.equals("breeder")){
                    Intent  filesIntent = new Intent();
                    Attach_Type = type;
                    adapter_audit_no = audit;
                    adapter_org_code = Org_code;
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.setType("application/pdf");
                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(filesIntent, REQ_CODE_PDF);
                }
                else if(type.equals("feed")){
                    Intent  filesIntent = new Intent();
                    Attach_Type = type;
                    adapter_audit_no = audit;
                    adapter_org_code = Org_code;
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.setType("application/pdf");
                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(filesIntent, REQ_CODE_PDF);
                }
                else if(type.equals("med")){
                    Intent  filesIntent = new Intent();
                    Attach_Type = type;
                    adapter_audit_no = audit;
                    adapter_org_code = Org_code;
                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    filesIntent.setType("application/pdf");
                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(filesIntent, REQ_CODE_PDF);
                }
            }
        };

    }



    public void back(View view) {
        data.intent(inv_form.class,view.getContext());
        finish();
    }

    private void LoadFolders(String org_code,String date) {
        list.clear();
        adapter.notifyDataSetChanged();
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.loading);
        loading.loop(true);
        loading.playAnimation();
        API.getClient().LoadUploadFile(sharedPref.getUser(),org_code,date).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        loading.loop(true);
                        loading.playAnimation();
                        for (int i = 0; i < result.length(); i++) {

                            JSONObject object = result.getJSONObject(i);
                            model_upload_list item = new model_upload_list(
                                    object.getString("ORG_CODE"),
                                    object.getString("FARM_CODE"),
                                    object.getString("FARM_NAME"),
                                    object.getString("AUDIT_NO"),
                                    object.getString("AUDIT_DATE"),
                                    object.getBoolean("haveAttach")
                            );

                            list.add(item);


                        }

                        adapter = new Adapter_upload_list(list,getApplicationContext(),modal_interface);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.nodatafile);
                        loading.setVisibility(View.VISIBLE);
                        loading.loop(true);
                        loading.playAnimation();
                        list.clear();
                        Toast.makeText(getApplicationContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");
                    loading.setAnimation(R.raw.nodatafile);
                    loading.setVisibility(View.VISIBLE);
                    loading.loop(true);
                    loading.playAnimation();

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


    public void search(View view) {
        String getorg_code = auditnof.trim();
        String getAudit_date = audit_date_filter.getText().toString();
        list.clear();
        LoadFolders(getorg_code,getAudit_date);

    }


    public void clear(View view) {
        list.clear();
        auditnof = "";
        audit_date_filter.setText("");
        org_code_filter.setText("");
        LoadFolders("","");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent d) {
        super.onActivityResult(requestCode, resultCode, d);
        if (resultCode == RESULT_OK) {
            data.Preloader(this,"File Uploading.\nPlease wait");
            String getaudit =  adapter_audit_no.trim();
            String getorg_code = auditnof.trim();
            String getAudit_date = audit_date_filter.getText().toString();
//            Toast.makeText(this, getaudit, Toast.LENGTH_SHORT).show();

            switch (requestCode) {
//                case 2021:
//                    if (d != null) {
//                        if (d.getClipData() != null){
//                            for (int i = 0; i < d.getClipData().getItemCount(); i++) {
//                                Uri uri = d.getClipData().getItemAt(i).getUri();
//                                File myFile = new File(uri.toString());
//                                getBitmap(uri);
//                                String path = myFile.getAbsolutePath();
//                                String pdfName = getFileName(uri);
//                                String pdfToString2 = PDFtoString2(bitmap);
//                                if(!(i + 1 < d.getClipData().getItemCount())) {
//                                    UploadPDFFile(getaudit,pdfName,pdfToString2,"last",ConfirmDialog);
//                                }
//                                else{
//                                    UploadPDFFile(getaudit,pdfName,pdfToString2,"more",ConfirmDialog);
//                                }
//
//
//                            }
//
//                        }else{
//                            Uri uri = d.getData();
//                            File myFile = new File(uri.toString());
//                            getBitmap(uri);
//                            String path = myFile.getAbsolutePath();
//                            String pdfName = getFileName(uri);
//                            String pdfToString2 = PDFtoString2(bitmap);
//                            UploadPDFFile(getaudit,pdfName,pdfToString2,"last",ConfirmDialog);
//                        }
//                    }
//                    break;
                case 2022:
                    if(d != null){
                        if(d.getClipData() != null){
                            for (int i = 0; i < d.getClipData().getItemCount(); i++) {
                                Uri uri = d.getClipData().getItemAt(i).getUri();
                                File myFile = new File(uri.toString());
                                getBitmap(uri);
                                String path = myFile.getAbsolutePath();
                                String pdfName = getFileName(uri);
                                String pdfToString = PDFToString(uri);

                                if(!(i + 1 < d.getClipData().getItemCount())) {
                                    Log.d("UploadPDF", adapter_org_code+ "_"+adapter_audit_no+Attach_Type);
                                    Log.d("UploadPDF",pdfToString);
//                                    data.loaddialog.dismiss();
//                                    uploadModifyPDF(adapter_org_code+ "_"+adapter_audit_no+Attach_Type,pdfToString,data.loaddialog);
                                    UploadPDFFile(getaudit,pdfName,pdfToString,"last",getorg_code,getAudit_date);
//                                    Toast.makeText(this, pdfName, Toast.LENGTH_SHORT).show();
                                }
                                else{
//                                    Toast.makeText(this, pdfName, Toast.LENGTH_SHORT).show();
                                    UploadPDFFile(getaudit,pdfName,pdfToString,"more",getorg_code,getAudit_date);
                                }

                            }
                        }
                        else{
                            Uri uri = d.getData();
                            File myFile = new File(uri.toString());
                            getBitmap(uri);
                            String path = myFile.getAbsolutePath();
                            String pdfName = getFileName(uri);
                            String pdfToString = PDFToString(uri);

                            Log.d("UploadPDF", adapter_org_code+ "_"+adapter_audit_no+Attach_Type);
                            Log.d("UploadPDF",pdfToString);
                            Toast.makeText(this, pdfName, Toast.LENGTH_SHORT).show();
//                            uploadModifyPDF(adapter_org_code+ "_"+adapter_audit_no+Attach_Type,pdfToString,data.loaddialog);
                            UploadPDFFile(getaudit,pdfName,pdfToString,"last",getorg_code,getAudit_date);
                        }
                        break;
                    }

            }
        }
    }




    public void uploadModifyPDF(String filename,String PDFfile,AlertDialog dialog){
//        Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
        API.getClient().updatePDFFile(filename+".pdf",PDFfile).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        dialog.dismiss();
                        Toast.makeText(upload_file.this, "Upload File Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dialog.dismiss();
                        Toast.makeText(upload_file.this, "error", Toast.LENGTH_SHORT).show();
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


    public void UploadPDFFile(String audit,String name,String file,String last,String org_code,String date){
        API.getClient().uploadPDFFiles(audit,name,file,sharedPref.getUser(),Attach_Type).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        if(last.equals("last")){
                            if(!ifupdate){
                                data.loaddialog.dismiss();
                            }
//                            data.loaddialog.dismiss();
                            Toast.makeText(upload_file.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                            LoadFolders(org_code,date);
                        }
                        else{

                        }

                    }
                    else{
                        Toast.makeText(upload_file.this, "error", Toast.LENGTH_SHORT).show();
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

    public String PDFToString (Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
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

//    public void uplaod(View view) {
//        ifupdate = false;
//        MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
//        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.uploadfile_modal,null);
//
//         audit_no = v.findViewById(R.id.audit_no);
//         MaterialButton uplaodbtn = v.findViewById(R.id.uploadimg);
//        MaterialButton uplaodbtnpdf = v.findViewById(R.id.uploadpdf);
//
//
//         uplaodbtn.setOnClickListener(v1 -> {
//                String getaudit_no = auditno.trim();
//
//                if(getaudit_no.isEmpty()){
//                    Toast.makeText(this, "Select Audit No", Toast.LENGTH_SHORT).show();
//                    audit_no.performClick();
//                }else{
//
//
//                    data.Confirmation_upload(view.getContext(),"Do you want to upload Attach Files?",R.drawable.ic_icons8_warning);
//                    data.negative.setOnClickListener(v2 ->{
//                        data.ConfirmDialog.dismiss();
//                    });
//                    data.breeder.setOnClickListener(v2 -> {
//                        Attach_Type = "breeder";
//                        Intent intent = new Intent( );
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                        startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//                    });
//                    data.feed.setOnClickListener(v2 -> {
//                        Attach_Type = "feed";
//                        Intent intent = new Intent( );
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                        startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//                    });
//                    data.med.setOnClickListener(v2 -> {
//                        Attach_Type = "med";
//                        Intent intent = new Intent( );
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                        startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//                    });
//                    data.negative.setText("No");
//                    data.negative.setText("Cancel");
//                    data.negative.setOnClickListener(v2 -> {
//                      data.ConfirmDialog.dismiss();
//                    });
//
//
//
//
//                }
//         });
//
//        uplaodbtnpdf.setOnClickListener(v1 -> {
//            String getaudit_no = auditno.trim();
//
//            if(getaudit_no.isEmpty()){
//                Toast.makeText(this, "Select Audit No", Toast.LENGTH_SHORT).show();
//                audit_no.performClick();
//            }else{
//                data.Confirmation_upload(view.getContext(),"Do you want to upload Attach Files?",R.drawable.ic_icons8_warning);
//                data.negative.setOnClickListener(v2 ->{
//                    data.ConfirmDialog.dismiss();
//                });
//                data.breeder.setOnClickListener(v2 -> {
//                    Attach_Type = "breeder";
//                    Intent  filesIntent = new Intent();
//                    filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    filesIntent.setType("application/pdf");
//                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(filesIntent, REQ_CODE_PDF);
//                });
//                data.feed.setOnClickListener(v2 -> {
//                    Attach_Type = "feed";
//                    Intent  filesIntent = new Intent();
//                    filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    filesIntent.setType("application/pdf");
//                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(filesIntent, REQ_CODE_PDF);
//                });
//                data.med.setOnClickListener(v2 -> {
//                    Attach_Type = "med";
//                    Intent  filesIntent = new Intent();
//                    filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    filesIntent.setType("application/pdf");
//                    filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(filesIntent, REQ_CODE_PDF);
//                });
//                data.negative.setText("No");
//                data.negative.setText("Cancel");
//                data.negative.setOnClickListener(v2 -> {
//                    data.ConfirmDialog.dismiss();
//                });
//
//            }
//        });
//
//        AutoCompleteCode_audit(sharedPref.getUser(),audit_no);
//        audit_no.setOnTouchListener((v1, event) -> {
//            audit_no.showDropDown();
//            return false;
//        });
//
//        audit_no.setOnItemClickListener((parent, view1, position, id) -> {
//            String getPostion = autocompletelist_audit.get(position).split("\\(")[1];
//            String getaudit = getPostion.split("\\)")[0];
//            auditno = getaudit;
//
//        });
//
//        Materialdialog.setView(v);
//        ConfirmDialog = Materialdialog.create();
//        ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        BounceView.addAnimTo(ConfirmDialog);
//        ConfirmDialog.show();
//
//    }

    private void AutoCompleteCode_audit(String user,AutoCompleteTextView autoCompleteTextView) {
        autocompletelist_audit.clear();
        API.getClient().autoComplete_audit(user,"modal").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            autocompletelist_audit.add(object.getString("org_code"));
                        }

                    }
                    else{

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


        adapterx = new ArrayAdapter<>(this, R.layout.dropdown_item ,R.id.autoCompleteItem,autocompletelist_audit);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapterx);
    }

//    public  void  adapterfunc_b(View view,String audit){
//        String getaudit_no = audit;
//
//        if(getaudit_no.isEmpty()){
//            Toast.makeText(view.getContext(), "Select Audit No", Toast.LENGTH_SHORT).show();
//        }else{
//            data.Confirmation_upload(view.getContext(),"Do you want to upload Attach Files?",R.drawable.ic_icons8_warning);
//            data.negative.setOnClickListener(v3 ->{
//                data.ConfirmDialog.dismiss();
//            });
//            data.breeder.setOnClickListener(v3 -> {
//                Attach_Type = "breeder";
//                Intent  filesIntent = new Intent();
//                filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                filesIntent.setType("application/pdf");
//                filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(filesIntent, REQ_CODE_PDF);
//            });
//            data.feed.setOnClickListener(v3 -> {
//                Attach_Type = "feed";
//                Intent  filesIntent = new Intent();
//                filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                filesIntent.setType("application/pdf");
//                filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(filesIntent, REQ_CODE_PDF);
//            });
//            data.med.setOnClickListener(v3 -> {
//                Attach_Type = "med";
//                Intent  filesIntent = new Intent();
//                filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                filesIntent.setType("application/pdf");
//                filesIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(filesIntent, REQ_CODE_PDF);
//            });
//            data.negative.setText("No");
//            data.negative.setText("Cancel");
//            data.negative.setOnClickListener(v3 -> {
//                data.ConfirmDialog.dismiss();
//            });
//
//        }
//    }
//
//    public  void adapterfunc_a(View view,String audit){
//        String getaudit_no = audit;
//
//        if(getaudit_no.isEmpty()){
//            Toast.makeText(view.getContext(), "Select Audit No", Toast.LENGTH_SHORT).show();
//        }else{
//            data.Confirmation_upload(view.getContext(),"Do you want to upload Attach Files?",R.drawable.ic_icons8_warning);
//            data.negative.setOnClickListener(v3 ->{
//                data.ConfirmDialog.dismiss();
//            });
//            data.breeder.setOnClickListener(v3 -> {
//                Attach_Type = "breeder";
//                Intent intent = new Intent( );
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//            });
//            data.feed.setOnClickListener(v3 -> {
//                Attach_Type = "feed";
//                Intent intent = new Intent( );
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//               startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//            });
//            data.med.setOnClickListener(v3 -> {
//                Attach_Type = "med";
//                Intent intent = new Intent( );
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(Intent.createChooser(intent,"select multiple images"), REQ_CODE_IMG);
//            });
//            data.negative.setText("No");
//            data.negative.setText("Cancel");
//            data.negative.setOnClickListener(v3 -> {
//                data.ConfirmDialog.dismiss();
//            });
//
//
//
//
//        }
//    }


    public void upload(View view) {
        if(uploadimg.isExtended()){
            uploadimg.shrink();
        }
        else{
            uploadimg.extend();
        }
    }
}