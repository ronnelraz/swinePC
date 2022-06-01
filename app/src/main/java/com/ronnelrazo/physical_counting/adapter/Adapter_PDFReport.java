package com.ronnelrazo.physical_counting.adapter;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ronnelrazo.physical_counting.ItemClickListener;

import com.ronnelrazo.physical_counting.R;

import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfErrorHandler;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;


public class Adapter_PDFReport extends RecyclerView.Adapter<Adapter_PDFReport.ViewHolder> implements DownloadFile.Listener {
    Context mContext;
    List<modal_pdf_report> newsList;
    ItemClickListener itemClickListener;
    int selectedPostion = -1;


    LinearLayout root;
    RemotePDFViewPager remotePDF;
    PDFPagerAdapter adapter;


    public Adapter_PDFReport(List<modal_pdf_report> list, Context context, ItemClickListener itemClickListener) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.itemClickListener = itemClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_listreport,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_pdf_report getData = newsList.get(position);

        holder.org_name.setText(getData.getOrg_name() + " - " + getData.getOrg_code());
        holder.farm_name.setText(getData.getFarm_name() + " - " + getData.getFarm_code());
        holder.audi_No.setText("Audit No. : "+getData.getAudit_no());
        holder.audi_date.setText("Audit Date : "+getData.getAudit_date());


        Webhook("https://agro.cpf-phil.com/swinePC/api/checklistPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","checklistPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/BreederPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","BreederPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/FeedPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","FeedPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/MedPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","MedPDF backup");


        holder.pdfbtn.setOnClickListener(v-> {
            String getOrgCode = getData.getOrg_code();
            String getFarmCode = getData.getFarm_code();
            String getauditNo = getData.getAudit_no();




            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pdf_modal,null);

            TabLayout tabs = view.findViewById(R.id.tab);

            root = view.findViewById(R.id.remote_pdf_root);



            tabs.addTab(tabs.newTab().setText("Checklist"));
            tabs.addTab(tabs.newTab().setText("Breeder"));
            tabs.addTab(tabs.newTab().setText("Feed"));
            tabs.addTab(tabs.newTab().setText("Med"));
            tabs.setTabGravity(TabLayout.GRAVITY_FILL);




            PDFViewPager pdfViewPager = view.findViewById(R.id.datapdf);
            ImageView closeModal = view.findViewById(R.id.close);

            //CHECKLIST
            String PDF_Path = getData.getUrl()+getData.getPath()+"checklist.pdf";
            Log.d("Swine","URL : ->" + PDF_Path.trim());
            setDownload(PDF_Path,R.id.datapdf);




            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if(tab.getPosition() == 0){

                        Toast.makeText(mContext, tab.getText(), Toast.LENGTH_SHORT).show();
                        String PDF_Path = getData.getUrl()+getData.getPath()+"checklist.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Checklist");
                        Log.d("Swine","URL : ->" + PDF_Path.trim());
                        setDownload(PDF_Path,R.id.datapdf);

                    }
                    else if(tab.getPosition() == 1){
                        Toast.makeText(mContext, tab.getText(), Toast.LENGTH_SHORT).show();
                        String PDF_Path = getData.getUrl()+getData.getPath()+"breeder.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Breeder");
                        Log.d("Swine","URL : ->" + PDF_Path.trim());
                        setDownload(PDF_Path,R.id.datapdf);



                    }
                    else if(tab.getPosition() == 2){
                        Toast.makeText(mContext, tab.getText(), Toast.LENGTH_SHORT).show();
                        String PDF_Path = getData.getUrl()+getData.getPath()+"feed.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Feed");
                        Log.d("Swine","URL : ->" + PDF_Path.trim());
                        setDownload(PDF_Path,R.id.datapdf);
                    }
                    else if(tab.getPosition() == 3){
                        Toast.makeText(mContext, tab.getText(), Toast.LENGTH_SHORT).show();
                        String PDF_Path = getData.getUrl()+getData.getPath()+"med.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Med");
                        Log.d("Swine","URL : ->" + PDF_Path.trim());
                        setDownload(PDF_Path,R.id.datapdf);

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
            alertDialog.setCanceledOnTouchOutside(true);
            closeModal.setOnClickListener(v2 -> {
                alertDialog.dismiss();
            });
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            BounceView.addAnimTo(alertDialog);
            alertDialog.show();

        });


        holder.radioButton.setChecked(position == selectedPostion);
        holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
             if(isChecked){
                holder.pdfbtn.setVisibility(View.VISIBLE);
                holder.container.setBackgroundColor(Color.parseColor("#ecf0f1"));
             }
             else{
                 holder.pdfbtn.setVisibility(View.GONE);
                 holder.container.setBackgroundColor(Color.parseColor("#FFFFFF"));
             }
             selectedPostion = holder.getAdapterPosition();
             itemClickListener.onClick(getData.getFarm_name());
        });

    }

    public boolean exists(String url){
        try {
            URL u = new URL(url);
            HttpURLConnection http = (HttpURLConnection) u.openConnection();
            http.connect();
            return true;
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    protected void Webhook(String urlString,String type) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d("swine",type +" Webhook : -> Started");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("swine",type +" Success" + " " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("swine",type +" Failed" + " " + errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d("swine",type +" retry ->" + retryNo);
            }
        });
    }
    protected void setDownload(String url,int id) {
        final DownloadFile.Listener listener = this;
        remotePDF = new RemotePDFViewPager(mContext, url.trim(), listener);
        remotePDF.setId(id);
    }

    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDF,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    protected void ErrorhandlingPDF(String url,PDFViewPager pdfViewPager,View v,String tabname){


        adapter = new PDFPagerAdapter.Builder(v.getContext())
                .setErrorHandler(new PdfErrorHandler() {
                    @Override
                    public void onPdfError(Throwable t) {
                        Globalfunction.getInstance(v.getContext()).toast(R.raw.error,"Sorry PDF not found in this Page " + tabname, Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                })
                .setPdfPath(url.trim())
                .create();

//        pdfViewPager.setAdapter(adapter);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(mContext, FileUtil.extractFileNameFromURL(url));
        remotePDF.setAdapter(adapter);
        updateLayout();

    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public RadioButton radioButton;
        public TextView org_name,farm_name,audi_No,audi_date;
        public MaterialButton pdfbtn;
        public RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radio_btn);
            org_name = itemView.findViewById(R.id.org_name);
            farm_name = itemView.findViewById(R.id.farm_name);
            audi_No = itemView.findViewById(R.id.audi_No);
            pdfbtn = itemView.findViewById(R.id.pdfbtn);
            audi_date = itemView.findViewById(R.id.audi_date);
            container = itemView.findViewById(R.id.container);


        }
    }
}
