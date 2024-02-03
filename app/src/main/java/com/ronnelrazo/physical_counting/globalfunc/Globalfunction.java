package com.ronnelrazo.physical_counting.globalfunc;


import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.novoda.merlin.Merlin;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;
import com.ronnelrazo.physical_counting.Database.AUDIT_USER_AUTHORIZE_OFFLINE;
import com.ronnelrazo.physical_counting.Database.MyDatabaseHelper;
import com.ronnelrazo.physical_counting.Database.TABLE_BREEDER_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_FEED_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_HEADER;
import com.ronnelrazo.physical_counting.Database.TABLE_HEADER_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_MED_DETAILS;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_GET;
import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class Globalfunction {

    public static final String TAG = "Swine";
    private static Globalfunction application;
    private static Context cont;
    public SweetAlertDialog pDialog;
    public final Calendar calendar = Calendar.getInstance();

    //dialog
    public MaterialAlertDialogBuilder Materialdialog;
    public AlertDialog loaddialog;
    public  TextView content;
    public long selectedCurrentDate;
    //end dialog

    //confirm
    public AlertDialog ConfirmDialog;
    public MaterialButton positive,negative,breeder,feed,med,checklist;

    //audit dialog
    public MaterialAlertDialogBuilder AuditDialog;
    public AlertDialog Auditalert;
    public MaterialButton audit_save,audit_cancel;
    public TextView currentDate,auditDate;


    //observe network conenction
    public Merlin merlin;

    public String fileName;
    public URL url = null;



    public Globalfunction(Context context){
        cont = context;
    }

    private MyDatabaseHelper databaseHelper = new MyDatabaseHelper(cont);

    public static synchronized Globalfunction getInstance(Context context){
        if(application == null){
            application = new Globalfunction(context);
        }
        return application;
    }


    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public void registerBind(TextView textView){
        merlin.registerBindable(networkStatus -> {
            boolean isconnected = networkStatus.isAvailable();
            Log.d(TAG,isconnected ? "network Available" : "Network Not Available");
            String status = isconnected ? "Connected" : "Network not available";
            String color = isconnected ? "#2ecc71" : "#e55039";
            textView.setText(status);
            textView.setTextColor(Color.parseColor(color));
        });


    }

    public void isConnected(TextView textView){
        merlin.registerConnectable(() -> {
            Log.d(TAG,"Connected");
            String status = "Connected";
            textView.setText(status);
            textView.setTextColor(Color.parseColor("#2ecc71"));
        });

    }



    public void isDisconnected(TextView textView){
        merlin.registerDisconnectable(() -> {
            Log.d(TAG,"Disconnected");
            String status = "Disconnect";
            textView.setText(status);
            textView.setTextColor(Color.parseColor("#e55039"));
        });
    }

//    intent

    public void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }

    //input watcher
    public void inputwatcher(TextInputEditText inputEditText, TextInputLayout layout,String msg){
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(i >= 1){
                   layout.setErrorEnabled(false);
               }
               else{
                   layout.setErrorEnabled(true);
                   layout.setErrorIconDrawable(null);
                   layout.setError(msg);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    //plugin
    public void loading(String msg){
        pDialog = new SweetAlertDialog(cont, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.purple_700);
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);
        pDialog.show();
    }
    //sakin
    public void Preloader(Context context,String msg){
        Materialdialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.custom_loading,null);
        content = v.findViewById(R.id.content);
        content.setText(msg);
        Materialdialog.setView(v);

        loaddialog = Materialdialog.create();
        loaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(loaddialog);
        loaddialog.show();
    }

    //toast
    public void toast(int raw,String body,int postion,int x ,int y){
        Toast toast = new Toast(cont);
        View vs = LayoutInflater.from(cont).inflate(R.layout.custom_toast, null);
        LottieAnimationView icon = vs.findViewById(R.id.icon);
        TextView msg = vs.findViewById(R.id.body);

        icon.setAnimation(raw);
        icon.loop(false);
        icon.playAnimation();
        msg.setText(body);
        toast.setGravity(postion, x, y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(vs);
        toast.show();
    }

//
//    public void fancyToast(String title,String msg,Context contexts){
//        new FancyGifDialog.Builder(contexts)
//                .setTitle(title)
//                .setMessage(msg)
//                .setTitleTextColor(R.color.black)
//                .setDescriptionTextColor(R.color.black)
//                .setPositiveBtnText("Ok")
//                .setPositiveBtnBackground(R.color.purple_500)
//                .setGifResource(R.drawable.nofiles)   //Pass your Gif here
//                .isCancellable(true)
//                .OnPositiveClicked(new FancyGifDialogListener() {
//                    @Override
//                    public void OnClick() {
//
//                    }
//                })
//                .build();
//    }

    //login checker


    public void decodeToken(String token, SharedPref sharedPref){
        Base64.Decoder decoder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoder = Base64.getUrlDecoder();
        }
        String[] parts = token.split("\\."); // split out the "parts" (header, payload and signature)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String headerJson = new String(decoder.decode(parts[0]));
            Log.d("swine",headerJson);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String payloadJson = new String(decoder.decode(parts[1]));

            try {
                JSONObject jsonResponse = new JSONObject(payloadJson);
                String iss = jsonResponse.getString("iss");

                JSONObject dataobj = new JSONObject(jsonResponse.getString("data"));
                String username = dataobj.getString("username");
                String role = dataobj.getString("role");
                String role_id = dataobj.getString("role_id");
                String buisness = dataobj.getString("business");
                String menu_access = dataobj.getString("menu_access");
                JSONArray master = dataobj.getJSONArray("master");

                List<String> org_code_arr = new ArrayList<>();
                List<String> farm_code_arr = new ArrayList<>();


                for (int i = 0; i < master.length(); i++) {
                    JSONObject object = master.getJSONObject(i);
                    org_code_arr.add(object.getString("org_code_arr"));
                    farm_code_arr.add(object.getString("farm_code_arr"));
                }

                String str_org_code = org_code_arr.toString().replaceAll("\\[", "'").replaceAll("\\]","'").replaceAll(",","','").replaceAll(" ","");
                String str_farm_code = farm_code_arr.toString().replaceAll("\\[", "'").replaceAll("\\]","'").replaceAll(",","','").replaceAll(" ","");

                Log.d("inaaa",str_org_code);
                Log.d("inaaa",str_farm_code);


                sharedPref.setJWTData(username,role,buisness, str_org_code,str_farm_code,role_id,menu_access);





                Log.d("swine", "role: " + sharedPref.getRole() + " buisness: " + sharedPref.getBU() + " user: " + sharedPref.getUser());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //String signatureJson = new String(decoder.decode(parts[2]));
    }



    public void PDFURLCHECKER(String URL,View v){
        API_GET.getClient().PDFURLCHECKER(config.URLDownload+URL).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(!success){
                        Toast.makeText(v.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.isSuccessful()){
                        Toast.makeText(v.getContext(), "error", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
                }
            }
        });
    }

    public void DownloadPDFURL(String URL){

        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        fileName = url.getPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileNamex = folder.getPath() + "/" + fileName;
        File myFile = new File(fileNamex);
        if(myFile.exists()) {
            System.out.println("exists");
            myFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL + ""));
        request.setTitle(fileName);
        request.setMimeType("applcation/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        isFileExists(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager dm = (DownloadManager) cont.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);



    }



    boolean ifdownloadtrue = false;
    public boolean DownloadPrintPDF(String URL){

        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        fileName = url.getPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileNamex = folder.getPath() + "/" + fileName;
        File myFile = new File(fileNamex);
        if(myFile.exists()) {
            System.out.println("exists");
            myFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL + ""));
        request.setTitle(fileName);
        request.setMimeType("applcation/pdf");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(false);
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        isFileExists(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager dm = (DownloadManager) cont.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);

        long downloadId = 100;

        Cursor cursor = dm.query(new DownloadManager.Query().setFilterById(downloadId));

        if (cursor != null && cursor.moveToNext()) {
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();
//
//            if (status == DownloadManager.STATUS_FAILED) {
//                // do something when failed
//                ifdownloadtrue =  false;
//            }
//            else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
//                // do something pending or paused
//                ifdownloadtrue =  false;
//            }
//            else if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                // do something when successful
//                ifdownloadtrue = true;
//            }
//            else if (status == DownloadManager.STATUS_RUNNING) {
//                // do something when running
//            }
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }
        }
        return true;

    }

    private boolean isFileExists(String filename){
        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filename);
        return folder1.exists();
    }

    private boolean deleteFile( String filename){
        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filename);
        return folder1.delete();
    }

    public void ViewPDFView(String path,View v){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+path);
        Uri uri= FileProvider.getUriForFile(v.getContext(),"com.ronnelrazo.physical_counting"+".provider",file);
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(uri,"application/pdf");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        v.getContext().startActivity(i);
    }






    public void PDFEfitor(String Uriparse,View v){
        final Uri uri = Uri.parse(Uriparse);
        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(v.getContext()).build();
        PdfActivity.showDocument(v.getContext(), uri, config);
    }
    //custom alert dialog confirmation
    public void Confirmation(Context context,String msg,int resicon){
        Materialdialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.custom_confirmation,null);
        TextView content = v.findViewById(R.id.content);
        ImageView icon = v.findViewById(R.id.icon);
        positive = v.findViewById(R.id.positive);
        negative = v.findViewById(R.id.negative);
        icon.setImageResource(resicon);
        content.setText(msg);
        content.setText(msg);
        Materialdialog.setView(v);

        ConfirmDialog = Materialdialog.create();
        ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(ConfirmDialog);
        ConfirmDialog.show();
    }

    //custom alert dialog confirmation
    public void Confirmation_upload(Context context,String msg,int resicon){
        Materialdialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.custom_uploadmodal,null);
        TextView content = v.findViewById(R.id.content);
        ImageView icon = v.findViewById(R.id.icon);
        checklist = v.findViewById(R.id.checklist);
        breeder = v.findViewById(R.id.breeder);
        feed = v.findViewById(R.id.feed);
        med = v.findViewById(R.id.med);
        negative = v.findViewById(R.id.negative);
        icon.setImageResource(resicon);
        content.setText(msg);
        content.setText(msg);
        Materialdialog.setView(v);

        ConfirmDialog = Materialdialog.create();
        ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(ConfirmDialog);
        ConfirmDialog.show();
    }

    //textview indent
    public static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }



    //date get 1st date of month
    public String getMonth(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("MM/01/yyyy ", d.getTime());
        return String.valueOf(s);
    }




    //audit dialog

    private String auditCurrentMonth_plus(){
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 1);
        d = c.getTime();
        CharSequence s  = DateFormat.format("MM/dd/yyyy ", c.getTime());
        return String.valueOf(s);
    }

    private String auditCurrentMonth(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("MM/dd/yyyy ", d.getTime());
        return String.valueOf(s);
    }


    public String transaction_date(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("dd/MM/yyyy ", d.getTime());
        return String.valueOf(s);
    }

    public DatePickerDialog.OnDateSetListener getDateto(TextView textView){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Formatter(textView);
            selectedCurrentDate = calendar.getTimeInMillis();


        };
        return date;
    }



    private void Formatter(TextView textView) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(sdf.format(calendar.getTime()));
    }



    public void auditDialog(Context context){
        AuditDialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.modal_audit_dialog,null);
        currentDate = v.findViewById(R.id.currentDate);
        currentDate.setText(auditCurrentMonth());
        auditDate = v.findViewById(R.id.auditDate);
//        auditDate.setText(auditCurrentMonth_plus());



        auditDate.setOnClickListener(v1 -> {


           DatePickerDialog datePickerDialog = new DatePickerDialog(v1.getContext(),R.style.picker,getDateto(auditDate), calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH) + 1);
           datePickerDialog.getDatePicker().setMinDate(selectedCurrentDate);
           datePickerDialog.show();


        });



        currentDate.setOnClickListener(v1 -> {
            DatePickerDialog datePickerDialog =  new DatePickerDialog(v1.getContext(),R.style.picker,getDateto(currentDate), calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        audit_save = v.findViewById(R.id.positive);
        audit_cancel = v.findViewById(R.id.negative);

        AuditDialog.setView(v);

        Auditalert = AuditDialog.create();
        Auditalert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(Auditalert);
        Auditalert.show();
    }




    //set header
    public boolean ADD_CHECKLIST_HEADER(String str_orgcode, String str_farmcode,String str_orgname,String str_farmname,String doc_date,String audit_date,String str_types,String bu_Type,String bu_code,String bu_name,String bu_type_name){

        try{
            TABLE_HEADER column = new TABLE_HEADER();
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(column.ORG_CODE, str_orgcode);
            cv.put(column.FARM_CODE,str_farmcode);
            cv.put(column.ORG_NAME,str_orgname);
            cv.put(column.FARM_NAME,str_farmname);
            cv.put(column.DOC_DATE, doc_date);
            cv.put(column.AUDIT_DATE, audit_date);
            cv.put(column.TYPE, str_types);
            cv.put(column.BU_TYPE,bu_Type);
            cv.put(column.BU_CODE,bu_code);
            cv.put(column.BU_NAME,bu_name);
            cv.put(column.BU_TYPE_NAME,bu_type_name);
            long result = db.insert(column.TABLE_CHECKLIST_HEADER,null, cv);
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }catch (SQLiteConstraintException e){
            Log.d("swine",e.getMessage());
            return false;
        }

    }

    //save header_details
    public boolean ADD_CHECKLIST_HEADER_DETAILS(int position,String org_code,String farm_code,String type, String check_item,String remark,
                                                String MAINCODE, String MAINDESC, String MAINSEQ, String SUBCODE, String SUBDESC, String SUBSEQ, String DETAILSCODE,
                                                String DETAILSDESC, String DETAILSSEQ, String BUCODE, String BU_TYPE_CODE){
        TABLE_HEADER_DETAILS column = new TABLE_HEADER_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.POSITION, position);
        cv.put(column.ORG_CODE, org_code);
        cv.put(column.FARM_CODE,farm_code);
        cv.put(column.TYPE, type);
        cv.put(column.CHECK_ITEM, check_item);
        cv.put(column.REMARK,remark);
        cv.put(column.MAINCODE, MAINCODE);
        cv.put(column.MAINDESC, MAINDESC);
        cv.put(column.MAINSEQ,MAINSEQ);
        cv.put(column.SUBCODE, SUBCODE);
        cv.put(column.SUBDESC, SUBDESC);
        cv.put(column.SUBSEQ,SUBSEQ);
        cv.put(column.DETAILSCODE, DETAILSCODE);
        cv.put(column.DETAILSDESC,DETAILSDESC);
        cv.put(column.DETAILSSEQ, DETAILSSEQ);
        cv.put(column.BUCODE, BUCODE);
        cv.put(column.BU_TYPE_CODE,BU_TYPE_CODE);

        long result = db.insert(column.TABLE_CHECKLIST_HEADER_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //updateChecklist
    public boolean updatechecklist(int pos,String org_code,String farm_code,String checked_value,String remark){
        TABLE_HEADER_DETAILS column = new TABLE_HEADER_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.CHECK_ITEM,checked_value);
        cv.put(column.REMARK,remark);
//        cv.put(column.MAINCODE, MAINCODE);
//        cv.put(column.MAINDESC, MAINDESC);
//        cv.put(column.MAINSEQ,MAINSEQ);
//        cv.put(column.SUBCODE, SUBCODE);
//        cv.put(column.SUBDESC, SUBDESC);
//        cv.put(column.SUBSEQ,SUBSEQ);
//        cv.put(column.DETAILSCODE, DETAILSCODE);
//        cv.put(column.DETAILSDESC,DETAILSDESC);
//        cv.put(column.DETAILSSEQ, DETAILSSEQ);
//        cv.put(column.BUCODE, BUCODE);
//        cv.put(column.BU_TYPE_CODE,BU_TYPE_CODE);
        int save = db.update(
                column.TABLE_CHECKLIST_HEADER_DETAILS,
                cv,
                column.POSITION+" = ? and " +
                            column.ORG_CODE+" = ? and " +
                            column.FARM_CODE+" = ?", new String[] { String.valueOf(pos),org_code,farm_code } );
        if(save == 1){
            return false;
        }
        else{
            return true;
        }
    }



    //header send to server
    public Cursor getHeader(String org_code,String farm_code){
        String query = "SELECT *  FROM table_header where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //details checcklist send to server
    public Cursor getChecklistDetails(String org_code,String farm_code){
        String query = "SELECT *  FROM table_header_details where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //getcountrow
//    public long getChecklistCount(String org_code,String farm_code,String tablename){
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        long count = DatabaseUtils.queryNumEntries(db, tablename);
//        db.close();
//        return count;
//    }

    //cancel_function
    public boolean clearAll(String org_code,String farm_code){
        TABLE_HEADER column_header_checklist = new TABLE_HEADER();

        TABLE_HEADER_DETAILS column_details_checklist = new TABLE_HEADER_DETAILS();
        TABLE_BREEDER_DETAILS column_details_breeder = new TABLE_BREEDER_DETAILS();
        TABLE_FEED_DETAILS column_details_feed = new TABLE_FEED_DETAILS();
        TABLE_MED_DETAILS column_details_med = new TABLE_MED_DETAILS();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        db.delete(column_details_checklist.TABLE_CHECKLIST_HEADER_DETAILS,column_details_checklist.ORG_CODE + " = ? and " + column_details_checklist.FARM_CODE + " = ?",new String[]{org_code,farm_code} );
//        db.delete(column_header_checklist.TABLE_CHECKLIST_HEADER,column_header_checklist.ORG_CODE + " = ? and " + column_header_checklist.FARM_CODE + " = ?",new String[]{org_code,farm_code} );

        //checklist
        db.delete(column_details_checklist.TABLE_CHECKLIST_HEADER_DETAILS,null,null);
        db.delete(column_header_checklist.TABLE_CHECKLIST_HEADER,null,null);
        //breeder
        db.delete(column_details_breeder.TABLE_BREEDER_DETAILS,null,null);
        //feed
        db.delete(column_details_feed.TABLE_FEED_DETAILS,null,null);
        db.delete(column_details_med.TABLE_MED_DETAILS,null,null);



        return true;
    }


    /**
     * Add table_bneeder details
     * all breeder list add here
     * */
    public Cursor getBreederDetails(String org_code,String farm_code){
        String query = "SELECT *  FROM table_breeder_details where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor offline_authorize_checker(String username,String password){
        String query = "SELECT *  FROM AUDIT_USER_AUTHORIZE_OFFLINE where USERNAME = '"+username+"' and PASSWORD = '"+password+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean Save_offline_authorize(String username,String password,String group,String login,String role,String role_id,String business,String menu_access){
        AUDIT_USER_AUTHORIZE_OFFLINE column = new AUDIT_USER_AUTHORIZE_OFFLINE();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.USERNAME, username);
        cv.put(column.PASSWORD, password);
        cv.put(column.GROUP, group);
        cv.put(column.LOGIN, login);
        cv.put(column.ROLE, role);
        cv.put(column.ROLE_ID, role_id);
        cv.put(column.BUSINESS, business);
        cv.put(column.MENU_ACCESS, menu_access);
        long result = db.insert(column.AUDIT_USER_AUTHORIZE_OFFLINE,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    //save header_details
    public boolean ADD_BREEDER_DETAILS(int position,String org_code,String bucode,
                                       String bu_type,String location,String farm_code,
                                       String farm_org,String farm_name,String female_stock,
                                       String male_Stock,String total_Stock,String counting_Stock,
                                       String remark,String var, String unpost,String active_var){
        TABLE_BREEDER_DETAILS column = new TABLE_BREEDER_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.POSITION, position);
        cv.put(column.ORG_CODE, org_code);
        cv.put(column.BUCODE, bucode);
        cv.put(column.BU_TYPE_CODE, bu_type);
        cv.put(column.LOCATION, location);
        cv.put(column.FARM_CODE, farm_code);
        cv.put(column.FARM_ORG, farm_org);
        cv.put(column.FARM_NAME, farm_name);
        cv.put(column.FEMALE_STOCK, female_stock);
        cv.put(column.MALE_STOCK, male_Stock);
        cv.put(column.TOTAL_STOCK, total_Stock);
        cv.put(column.COUNTING_STOCK, counting_Stock);
        cv.put(column.REMARK, remark);
        cv.put(column.VARIANCE, var);
        cv.put(column.UNPOST, unpost);
        cv.put(column.ACTIVE_VAR, active_var);
        long result = db.insert(column.TABLE_BREEDER_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * updateBreeder count
     * **/
    public boolean updatebreederlist(int pos,String org_code,String farm_code,String counting,String remark,String var, String unpost,String active_var){
        TABLE_BREEDER_DETAILS column = new TABLE_BREEDER_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
        cv.put(column.VARIANCE,var);
        cv.put(column.UNPOST,unpost);
        cv.put(column.ACTIVE_VAR,active_var);
        int save = db.update(
                column.TABLE_BREEDER_DETAILS,
                cv,
                column.POSITION+" = ? and " +
                        column.ORG_CODE+" = ? and " +
                        column.FARM_CODE+" = ?", new String[] { String.valueOf(pos),org_code,farm_code } );
        if(save == 1){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Feed add feed
     * **/
    public boolean ADD_FEED_DETAILS(int position,String org_code,String bucode,
                                       String bu_type,String farm_code,
                                       String farm_org,String farm_name,String feed_code,String feed_name,
                                       String feed_stock_qty,String feed_stock_wgh,String stock_unit,String counting_Stock,
                                       String remark,String variance,String unpost,String activevar){
        TABLE_FEED_DETAILS column = new TABLE_FEED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.POSITION, position);
        cv.put(column.ORG_CODE, org_code);
        cv.put(column.BUCODE, bucode);
        cv.put(column.BU_TYPE_CODE, bu_type);
        cv.put(column.FARM_CODE, farm_code);
        cv.put(column.FARM_ORG, farm_org);
        cv.put(column.FARM_NAME, farm_name);
        cv.put(column.FEED_CODE, feed_code);
        cv.put(column.FEED_NAME, feed_name);
        cv.put(column.SYS_FEED_STOCK_QTY, feed_stock_qty);
        cv.put(column.SYS_FEED_STOCK_WGH, feed_stock_wgh);
        cv.put(column.STOCK_UNIT, stock_unit);
        cv.put(column.COUNTING_STOCK,counting_Stock);
        cv.put(column.REMARK, remark);
        cv.put(column.VARIANCE, variance);
        cv.put(column.UNPOST,unpost);
        cv.put(column.ACTIVE_VAR, activevar);
        long result = db.insert(column.TABLE_FEED_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateFeedlist(int pos,String org_code,String farm_code,String counting,String remark,String variance,String unpost,String activevar){
        TABLE_FEED_DETAILS column = new TABLE_FEED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
        cv.put(column.VARIANCE,variance);
        cv.put(column.UNPOST,unpost);
        cv.put(column.ACTIVE_VAR,activevar);
        int save = db.update(
                column.TABLE_FEED_DETAILS,
                cv,
                column.POSITION+" = ? and " +
                        column.ORG_CODE+" = ? and " +
                        column.FARM_CODE+" = ?", new String[] { String.valueOf(pos),org_code,farm_code } );
        if(save == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Cursor getFeedListDetails(String org_code,String farm_code){
        String query = "SELECT *  FROM table_feed_details where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    /**
     * med
     * **/

    public boolean ADD_MED_DETAILS(int position,String org_code,String bucode,
                                    String bu_type,String farm_code,
                                    String farm_org,String farm_name,String med_code,String med_name,
                                    String med_stock_qty,String med_stock_wgh,String stock_unit,String counting_Stock,
                                    String remark,String variance,String unpost,String active_var){
        TABLE_MED_DETAILS column = new TABLE_MED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.POSITION, position);
        cv.put(column.ORG_CODE, org_code);
        cv.put(column.BUCODE, bucode);
        cv.put(column.BU_TYPE_CODE, bu_type);
        cv.put(column.FARM_CODE, farm_code);
        cv.put(column.FARM_ORG, farm_org);
        cv.put(column.FARM_NAME, farm_name);
        cv.put(column.MED_CODE, med_code);
        cv.put(column.MED_NAME, med_name);
        cv.put(column.SYS_MED_STOCK_QTY, med_stock_qty);
        cv.put(column.SYS_MED_STOCK_WGH, med_stock_wgh);
        cv.put(column.STOCK_UNIT, stock_unit);
        cv.put(column.COUNTING_STOCK,counting_Stock);
        cv.put(column.REMARK, remark);
        cv.put(column.VARIANCE, variance);
        cv.put(column.UNPOST,unpost);
        cv.put(column.ACTIVE_VAR, active_var);
        long result = db.insert(column.TABLE_MED_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateMedlist(int pos,String org_code,String farm_code,String counting,String remark,String varience,String unpost,String active_var){
        TABLE_MED_DETAILS column = new TABLE_MED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
        cv.put(column.VARIANCE,varience);
        cv.put(column.UNPOST,unpost);
        cv.put(column.ACTIVE_VAR,active_var);
        int save = db.update(
                column.TABLE_MED_DETAILS,
                cv,
                column.POSITION+" = ? and " +
                        column.ORG_CODE+" = ? and " +
                        column.FARM_CODE+" = ?", new String[] { String.valueOf(pos),org_code,farm_code } );
        if(save == 1){
            return true;
        }
        else{
            return false;
        }
    }


//    public Cursor getMedListDetails(String org_code,String farm_code){
//        String query = "SELECT *  FROM table_med_details where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
//        SQLiteDatabase db = databaseHelper.getReadableDatabase();
//        Cursor cursor = null;
//        if(db != null){
//            cursor = db.rawQuery(query, null);
//        }
//        return cursor;
//    }
    public Cursor getMedListDetails(String org_code, String farm_code) {
        String query = "SELECT * FROM table_med_details WHERE org_code = '" + org_code + "' AND farm_code = '" + farm_code + "'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);

            if (cursor == null || cursor.getCount() == 0) {
                // First query returned no results, perform the second query here
                String secondQuery = "SELECT * FROM table_med_details WHERE org_code = '" + org_code + "' AND farm_code = '" + org_code + "'";
                cursor = db.rawQuery(secondQuery, null);
            }
        }

        return cursor;
    }




    public long tabUsed(String tablename){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tablename);
        db.close();
        return count;
    }


    public void flag(String audit,String flag,String user){

        API.getClient().flag(audit,flag,user).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                       Log.d("swine","updated");
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
                    Toast.makeText(cont, "Sorry Farm Details not yet setup, Please setup up first to complete the information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*Error response*/
    public String Errorvolley(VolleyError volleyError){
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }



}
