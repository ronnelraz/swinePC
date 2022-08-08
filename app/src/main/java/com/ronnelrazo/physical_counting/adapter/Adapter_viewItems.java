package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;
import com.ronnelrazo.physical_counting.ItemClickListener;
import com.ronnelrazo.physical_counting.Pdfviewer;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;
import com.ronnelrazo.physical_counting.model.model_viewfile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfErrorHandler;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;


public class Adapter_viewItems extends RecyclerView.Adapter<Adapter_viewItems.ViewHolder> {
    Context mContext;
    List<model_viewfile> newsList;


    private BottomSheetBehavior bottomSheetBehavior;

    public Adapter_viewItems(List<model_viewfile> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_viewfile getData = newsList.get(position);
        String getEtx = getData.getFiles().split("\\.")[1];
        String getTitle = getData.getFiles().split("/")[1];

        if(getEtx.equals("pdf")){
            holder.img.setImageResource(R.drawable.ic_icons8_pdf);
            holder.title.setText(getTitle);
        }
        else{
            Picasso.get().load(config.URLBKFILE + getData.getFiles()).into(holder.img, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(config.URLBKFILE + getData.getFiles()).into(holder.img);
                    holder.title.setText(getTitle);
                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.drawable.ic_icons8_remove_image).into(holder.img);
                    holder.title.setText(getTitle);
                }
            });
        }

        BounceView.addAnimTo(holder.card);
        holder.card.setOnClickListener(v -> {
            Log.d("demoURL",config.URLBKFILE + getData.getFiles());
            if(getEtx.equals("pdf")){
                bottomsheet(v,config.URLBKFILE + getData.getFiles(),"pdf");
            }
            else{
                bottomsheet(v,getData.getFiles(),"img");
            }

        });

    }



    public void bottomsheet(View v,String url,String urltype){

        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.fileviewer,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);
        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);


        PDFView pdfView = view.findViewById(R.id.idPDFView);
        ImageView img = view.findViewById(R.id.img);
        ImageView close = view.findViewById(R.id.close);
        close.setOnClickListener(v1 -> {
            bottomSheetDialog.dismiss();
        });


        if(urltype.equals("pdf")){
            img.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            new Pdfviewer(pdfView,v.getContext(),null,null,null).execute(url);

        }
        else{
            pdfView.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            Picasso.get().load(config.URLBKFILE + url).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(config.URLBKFILE + url).into(img);
                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.drawable.ic_icons8_remove_image).into(img);
                }
            });
        }



        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder{


        public CardView card;
        public ImageView img;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);

        }
    }
}
