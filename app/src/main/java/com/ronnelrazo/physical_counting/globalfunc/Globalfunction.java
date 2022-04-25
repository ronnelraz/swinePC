package com.ronnelrazo.physical_counting.globalfunc;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        TextView content = v.findViewById(R.id.content);
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





}
