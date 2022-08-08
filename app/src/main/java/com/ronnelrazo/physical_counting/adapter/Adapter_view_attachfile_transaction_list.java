package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.swipe.SwipeLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.Comman;
import com.ronnelrazo.physical_counting.PdfDocumentAdapter;
import com.ronnelrazo.physical_counting.Pdfviewer_single;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_file_upload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Adapter_view_attachfile_transaction_list extends RecyclerView.Adapter<Adapter_view_attachfile_transaction_list.ViewHolder> {
    Context mContext;
    List<model_file_upload> newsList;
    LottieAnimationView loadinglist;
    int type;


   PDFView pdfView;

    public Adapter_view_attachfile_transaction_list(List<model_file_upload> list, Context context, LottieAnimationView loadinglist,int type) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.loadinglist = loadinglist;
        this.type = type;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_attach_file_dialog__transaction_list_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_file_upload getData = newsList.get(position);

        holder.filename.setText(getData.getFile().split("/")[1]);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF(v, getData);
            }
        });

        if(type == 0){
            holder.print.setVisibility(View.GONE);
        }
        else{
            holder.print.setVisibility(View.VISIBLE);
        }

        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PDF =  config.URLBKFILE+getData.getFile();
                Globalfunction.getInstance(v.getContext()).DownloadPDFURL(PDF);
//                PrintManager printManager = (PrintManager) v.getContext().getSystemService(Context.PRINT_SERVICE);
//                try {
//                    PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(v.getContext(),PDF,getData.getFile().split("/")[1]);
//                    printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
//                }catch (Exception ex){
//                    Log.e("swine",""+ex.getMessage());
//                    Toast.makeText(v.getContext(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
//
//                }
            }
        });

    }



    public void generatePDF(View v, model_file_upload getData){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pdf_viewer,null);
        pdfView = view.findViewById(R.id.idPDFView);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.loading);
        ImageView closeModal = view.findViewById(R.id.close);

        String PDF =  config.URLBKFILE+getData.getFile();
        new Pdfviewer_single(pdfView,v.getContext(),lottieAnimationView,PDF).execute(PDF);
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder{


        public RelativeLayout item;
        public ImageView imgicon;
        public TextView filename;
        public MaterialButton print;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            imgicon = itemView.findViewById(R.id.imgicon);
            filename = itemView.findViewById(R.id.filename);
            print = itemView.findViewById(R.id.print);

        }
    }
}
