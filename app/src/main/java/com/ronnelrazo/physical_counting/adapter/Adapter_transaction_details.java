package com.ronnelrazo.physical_counting.adapter;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.airbnb.lottie.LottieAnimationView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.transition.Hold;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.Pdfviewer;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.edit_TabForm;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_feed_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_med_edit;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;
import com.ronnelrazo.physical_counting.model.model_breeder_edit;
import com.ronnelrazo.physical_counting.model.model_file_upload;
import com.ronnelrazo.physical_counting.model.model_med;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.model.model_upload_list;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Adapter_transaction_details extends RecyclerView.Adapter<Adapter_transaction_details.ViewHolder> {
    Context mContext;
    List<model_transaction_details> newsList;

    PDFView pdfView;

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<model_file_upload> list;


    public Adapter_transaction_details(List<model_transaction_details> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_transaction_details getData = newsList.get(position);

        holder.Orgcode.setText(Html.fromHtml(String.format("<strong>Org Name : </strong>%s (%s)", getData.getFarm_name(), getData.getOrg_code())));
        holder.auditNo.setText(Html.fromHtml(String.format("<strong>Audit No. : </strong>%s", getData.getAudit_no())));
        holder.Document_date.setText(Html.fromHtml(String.format("<strong>Audit Date : </strong>%s", getData.getAudit_date())));

        int labelColor = getData.getConfirm_flag().equals("Y") ? Color.parseColor("#27ae60") : Color.parseColor("#f39c12");
        String сolorString = String.format("%X", labelColor).substring(2); // !!strip alpha value!!

        holder.status.setText(Html.fromHtml(String.format("<strong>Status : </strong>%s", getData.getConfirm_flag().equals("Y") ? String.format("<font color=\"#%s\">Complete</font>", сolorString) : String.format("<font color=\"#%s\">Incomplete</font>", сolorString) )), TextView.BufferType.SPANNABLE);


        if(getData.getConfirm_flag().equals("N")){
            holder.edit.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globalfunction.getInstance(v.getContext()).intent(edit_TabForm.class,v.getContext());
                    edit_TabForm.Getorg_code = getData.getOrg_code();
                    edit_TabForm.Getfarm_code = getData.getFarm_code();
                    edit_TabForm.getAudit_no = getData.getAudit_no();
                    edit_TabForm.intentType = 1;

                    Tab_checklist_edit.AUDIT_NO = getData.getAudit_no();
                    Tab_breeder_edit.str_audit_no  = getData.getAudit_no();
                    Tab_feed_edit.str_audit_no = getData.getAudit_no();
                    Tab_med_edit.audit_no = getData.getAudit_no();
                }
            });

            holder.printpdf.setVisibility(View.GONE);
            holder.viewpdf.setVisibility(View.GONE);


        }
        else{
            holder.edit.setVisibility(View.GONE);
            holder.printpdf.setVisibility(View.VISIBLE);
            holder.viewpdf.setVisibility(View.VISIBLE);
        }

        holder.viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                generatePDF(v,getData,0);
                showBottomSheetDialog(v, getData,0);
            }
        });

        holder.printpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                generatePDF(v,getData,1);
                showBottomSheetDialog(v, getData,1);
            }
        });

    }

    private void showBottomSheetDialog(View v, model_transaction_details getData,int type) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.view_attach_file_dialog_layout,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        ImageView back = view.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            list.clear();
            bottomSheetDialog.dismiss();
        });
        LottieAnimationView loading = view.findViewById(R.id.loading);
        RecyclerView rviewbottom = view.findViewById(R.id.datalist);

        rviewbottom.setHasFixedSize(true);
        rviewbottom.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        rviewbottom.setLayoutManager(new LinearLayoutManager(v.getContext()));
        adapter = new Adapter_view_attachfile_transaction_list(list,view.getContext(),loading,type);
        rviewbottom.setAdapter(adapter);



        loadData(v,getData.getAudit_no(),loading,rviewbottom,type);

        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

    }

    private void loadData(View v,String audit,LottieAnimationView loading,RecyclerView recyclerView,int type) {
        list.clear();

        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.loading);
        loading.loop(true);
        loading.playAnimation();
        API.getClient().view_attach_file_(audit).enqueue(new Callback<Object>() {
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
                            model_file_upload item = new model_file_upload(
                                    object.getString("audit_no"),
                                    object.getString("files")

                            );

                            list.add(item);


                        }

                        adapter = new Adapter_view_attachfile_transaction_list(list,v.getContext(),loading,type);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.nodatafile);
                        loading.setVisibility(View.VISIBLE);
                        loading.loop(true);
                        loading.playAnimation();
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
                    Globalfunction.getInstance(v.getContext()).toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public void generatePDF(View v, model_transaction_details getData,int typebtn){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pdf_modal_view,null);
        TabLayout tabs = view.findViewById(R.id.tab);
        pdfView = view.findViewById(R.id.idPDFView);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.loading);
        MaterialButton print = view.findViewById(R.id.print);

        if(typebtn == 0){
            print.setVisibility(View.GONE);
        }
        else{
            print.setVisibility(View.VISIBLE);
        }



        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        ImageView closeModal = view.findViewById(R.id.close);

        String PDF =  config.URLDownload+getData.getOrg_code() + "_"+getData.getAudit_no()+"checklist.pdf";
        Log.d("razomeme",PDF);
        new Pdfviewer(pdfView,v.getContext(),print,lottieAnimationView,PDF).execute(PDF);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0){
                    String PDF =  config.URLDownload+getData.getOrg_code() + "_"+getData.getAudit_no()+"checklist.pdf";
                    Log.d("razomeme",PDF);
                    new Pdfviewer(pdfView,v.getContext(),print,lottieAnimationView,PDF).execute(PDF);

                }
                else if(tab.getPosition() == 1){
                    String PDF =  config.URLDownload+getData.getOrg_code() + "_"+getData.getAudit_no()+"breeder.pdf";
                    Log.d("razomeme",PDF);
                    new Pdfviewer(pdfView,v.getContext(),print,lottieAnimationView,PDF).execute(PDF);
                }
                else if(tab.getPosition() == 2){
                    String PDF =  config.URLDownload+getData.getOrg_code() + "_"+getData.getAudit_no()+"feed.pdf";
                    Log.d("razomeme",PDF);
                    new Pdfviewer(pdfView,v.getContext(),print,lottieAnimationView,PDF).execute(PDF);
                }
                else if(tab.getPosition() == 3){
                    String PDF =  config.URLDownload+getData.getOrg_code() + "_"+getData.getAudit_no()+"med.pdf";
                    Log.d("razomeme",PDF);
                    new Pdfviewer(pdfView,v.getContext(),print,lottieAnimationView,PDF).execute(PDF);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        dialogBuilder.setView(view);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        closeModal.setOnClickListener(v2 -> {
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        BounceView.addAnimTo(alertDialog);
        alertDialog.show();

    }




    @Override
    public int getItemCount() {
        return newsList.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Orgcode,auditNo,Document_date,status;
        public MaterialButton viewpdf,printpdf,edit;

        public ViewHolder(View itemView) {
            super(itemView);
                Orgcode = itemView.findViewById(R.id.Orgcode);
                auditNo = itemView.findViewById(R.id.auditNo);
                Document_date = itemView.findViewById(R.id.Document_date);
                viewpdf = itemView.findViewById(R.id.viewpdf);
                printpdf = itemView.findViewById(R.id.printpdf);
                edit = itemView.findViewById(R.id.edit);
                status = itemView.findViewById(R.id.status);

        }
    }
}
