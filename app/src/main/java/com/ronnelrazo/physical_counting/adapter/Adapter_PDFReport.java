package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;
import com.ronnelrazo.physical_counting.ItemClickListener;

import com.ronnelrazo.physical_counting.R;

import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cz.msebera.android.httpclient.Header;
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

    AlertDialog alertDialogPDF;


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

    protected void Checker(String urls,TextView textView,String title){
        try{
            URL url = new URL(urls);
            executeReq(url);
            textView.setText(title);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_ok_small, 0);

        }
        catch(Exception e){
            textView.setText(title);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_error_small, 0);
        }


    }

    private void executeReq(URL urlObject) throws IOException{
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) urlObject.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream response =conn.getInputStream();
        Log.d("Response:", response.toString());
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_pdf_report getData = newsList.get(position);

        holder.org_name.setText(String.format("%s - %s", getData.getOrg_name(), getData.getOrg_code()));
        holder.farm_name.setText(String.format("%s - %s", getData.getFarm_name(), getData.getFarm_code()));
        holder.audi_No.setText(String.format("Audit No. : %s", getData.getAudit_no()));
        holder.audi_date.setText(String.format("Audit Date : %s", getData.getAudit_date()));


        Webhook("https://agro.cpf-phil.com/swinePC/api/checklistPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","checklistPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/BreederPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","BreederPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/FeedPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","FeedPDF backup");
        Webhook("https://agro.cpf-phil.com/swinePC/api/MedPDF?ORG_CODE="+getData.getOrg_code()+"&AUDIT_NO="+getData.getAudit_no()+"&FARM_ORG=","MedPDF backup");


        holder.pdfbtn.setOnClickListener(v-> {
            String getOrgCode = getData.getOrg_code();
            String getFarmCode = getData.getFarm_code();
            String getauditNo = getData.getAudit_no();

            MaterialAlertDialogBuilder GEneratingPDF = new MaterialAlertDialogBuilder(v.getContext());
            View viewPDF = LayoutInflater.from(v.getContext()).inflate(R.layout.loading_generate_pdf,null);
            TextView checklist_t = viewPDF.findViewById(R.id.checklist_t);


            String message[] = {
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Please wait PDF File Generating...",
                    "Generate File"
            };

            Handler handler1 = new Handler();
            for(int i = 0; i < 8; i++){
                int finalI = i;
             handler1.postDelayed(new Runnable() {
                 @Override
                 public void run() {

                     if(finalI == 8-1){
                         alertDialogPDF.dismiss();
                         generatePDF(viewPDF, getData);
                     }
                     else{
                         checklist_t.setText(message[finalI]);
                     }

                 }
             },3000 * i);
            }

            GEneratingPDF.setView(viewPDF);
            alertDialogPDF = GEneratingPDF.create();
            alertDialogPDF.setCanceledOnTouchOutside(false);
            alertDialogPDF.setCancelable(false);
            alertDialogPDF.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            BounceView.addAnimTo(alertDialogPDF);
            alertDialogPDF.show();

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


    public void generatePDF(View v, modal_pdf_report getData){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pdf_modal,null);
        TabLayout tabs = view.findViewById(R.id.tab);

        try {
            Globalfunction.getInstance(v.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"checklist.pdf");
            Globalfunction.getInstance(v.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"breeder.pdf");
            Globalfunction.getInstance(v.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"feed.pdf");
            Globalfunction.getInstance(v.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"med.pdf");
        }catch (Exception e){
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        root = view.findViewById(R.id.remote_pdf_root);
        MaterialButton downloadPDF = view.findViewById(R.id.downloadPDF);
        MaterialButton moreOption = view.findViewById(R.id.moreOption);
        downloadPDF.setOnClickListener(v2-> {
            try {

                Globalfunction.getInstance(v2.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"checklist.pdf");
                Globalfunction.getInstance(v2.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"breeder.pdf");
                Globalfunction.getInstance(v2.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"feed.pdf");
                Globalfunction.getInstance(v2.getContext()).DownloadPDFURL(config.URLDownload+getData.getPath()+"med.pdf");
            }catch (Exception e){
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //default open more option
        MenuOption(moreOption,getData.getPath()+"checklist.pdf");






        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);



        ImageView closeModal = view.findViewById(R.id.close);

        //CHECKLIST
        String PDF_Path = getData.getUrl()+getData.getPath()+"checklist.pdf";
        Log.d("Swine","URL : ->" + PDF_Path.trim());
        setDownload(PDF_Path,R.id.datapdf);
//                Globalfunction.getInstance(v1.getContext()).ViewPDFView(getData.getPath()+"checklist.pdf",v1);
//                Globalfunction.getInstance(v1.getContext()).ViewPDFView(getData.getPath()+"breeder.pdf",v1);
//                Globalfunction.getInstance(v1.getContext()).ViewPDFView(getData.getPath()+"feed.pdf",v1);
//                Globalfunction.getInstance(v1.getContext()).ViewPDFView(getData.getPath()+"med.pdf",v1);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0){
                    String PDF_Path = getData.getUrl()+getData.getPath()+"checklist.pdf";
                    Log.d("Swine","URL : ->" + PDF_Path.trim());
                    setDownload(PDF_Path,R.id.datapdf);
                    MenuOption(moreOption,getData.getPath()+"checklist.pdf");

                }
                else if(tab.getPosition() == 1){
                    String PDF_Path = getData.getUrl()+getData.getPath()+"breeder.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Breeder");
                    Log.d("Swine","URL : ->" + PDF_Path.trim());
                    setDownload(PDF_Path,R.id.datapdf);
                    MenuOption(moreOption,getData.getPath()+"breeder.pdf");

                }
                else if(tab.getPosition() == 2){
                    String PDF_Path = getData.getUrl()+getData.getPath()+"feed.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Feed");
                    Log.d("Swine","URL : ->" + PDF_Path.trim());
                    setDownload(PDF_Path,R.id.datapdf);
                    MenuOption(moreOption,getData.getPath()+"feed.pdf");
                }
                else if(tab.getPosition() == 3){
                    String PDF_Path = getData.getUrl()+getData.getPath()+"med.pdf";
//                        ErrorhandlingPDF(PDF_Path,pdfViewPager,v,"Med");
                    Log.d("Swine","URL : ->" + PDF_Path.trim());
                    setDownload(PDF_Path,R.id.datapdf);
                    MenuOption(moreOption,getData.getPath()+"med.pdf");

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

    protected void SysPDFEditor(View v,String path){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+path);
        Uri uri= FileProvider.getUriForFile(v.getContext(),"com.ronnelrazo.physical_counting"+".provider",file);
        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(v.getContext()).build();
        config.getConfiguration().allowMultipleBookmarksPerPage();
        config.getConfiguration().animateScrollOnEdgeTaps();
        config.getConfiguration().getDefaultSigner();
        config.getConfiguration().getEnabledShareFeatures();
        config.getConfiguration().getFitMode();
        PdfActivity.showDocument(v.getContext(), uri, config);
    }

    protected void MenuOption(MaterialButton moreOption,String path){
        moreOption.setOnClickListener(v1 -> {
            PopupMenu popupMenu = new PopupMenu(v1.getContext(),moreOption);
            popupMenu.inflate(R.menu.more_option);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.edit:
                        SysPDFEditor(v1,path);
                        return true;
                    case  R.id.openWith:
                        Globalfunction.getInstance(v1.getContext()).ViewPDFView(path,v1);
                        return true;
                    default:
                        return false;
                }

            });
            popupMenu.show();
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
