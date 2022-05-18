package com.ronnelrazo.physical_counting.globalfunc;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.novoda.merlin.Merlin;
import com.ronnelrazo.physical_counting.Database.MyDatabaseHelper;
import com.ronnelrazo.physical_counting.Database.TABLE_BREEDER_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_FEED_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_HEADER;
import com.ronnelrazo.physical_counting.Database.TABLE_HEADER_DETAILS;
import com.ronnelrazo.physical_counting.Database.TABLE_MED_DETAILS;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    //end dialog

    //confirm
    public AlertDialog ConfirmDialog;
    public MaterialButton positive,negative;

    //audit dialog
    public MaterialAlertDialogBuilder AuditDialog;
    public AlertDialog Auditalert;
    public MaterialButton audit_save,audit_cancel;
    public TextView currentDate,auditDate;


    //observe network conenction
    public Merlin merlin;



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
        pDialog.setCancelable(false);
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
                String buisness = dataobj.getString("business");

                sharedPref.setJWTData(username,role,buisness);


                Log.d("swine", "role: " + sharedPref.getRole() + " buisness: " + sharedPref.getBU() + " user: " + sharedPref.getUser());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //String signatureJson = new String(decoder.decode(parts[2]));
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

    private String auditCurrentMonth(){
        Date d = new Date();
        CharSequence s  = DateFormat.format("MM/dd/yyyy ", d.getTime());
        return String.valueOf(s);
    }

    public DatePickerDialog.OnDateSetListener getDateto(){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Formatter();
        };
        return date;
    }

    private void Formatter() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        auditDate.setText(sdf.format(calendar.getTime()));
    }

    public void auditDialog(Context context){
        AuditDialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.modal_audit_dialog,null);
        currentDate = v.findViewById(R.id.currentDate);
        currentDate.setText(auditCurrentMonth());
//
        auditDate = v.findViewById(R.id.auditDate);
        auditDate.setOnClickListener(v1 -> {
            new DatePickerDialog(v1.getContext(),R.style.picker,getDateto(), calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
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


    //save header_details
    public boolean ADD_BREEDER_DETAILS(int position,String org_code,String bucode,
                                       String bu_type,String location,String farm_code,
                                       String farm_org,String farm_name,String female_stock,
                                       String male_Stock,String total_Stock,String counting_Stock,
                                       String remark){
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
    public boolean updatebreederlist(int pos,String org_code,String farm_code,String counting,String remark){
        TABLE_BREEDER_DETAILS column = new TABLE_BREEDER_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
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
                                       String remark){
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
        long result = db.insert(column.TABLE_FEED_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateFeedlist(int pos,String org_code,String farm_code,String counting,String remark){
        TABLE_FEED_DETAILS column = new TABLE_FEED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
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
                                    String remark){
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
        long result = db.insert(column.TABLE_MED_DETAILS,null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateMedlist(int pos,String org_code,String farm_code,String counting,String remark){
        TABLE_MED_DETAILS column = new TABLE_MED_DETAILS();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column.COUNTING_STOCK,counting);
        cv.put(column.REMARK,remark);
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


    public Cursor getMedListDetails(String org_code,String farm_code){
        String query = "SELECT *  FROM table_med_details where org_code = '"+org_code+"' and farm_code = '"+farm_code+"'";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    public long tabUsed(String tablename){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tablename);
        db.close();
        return count;
    }


}
