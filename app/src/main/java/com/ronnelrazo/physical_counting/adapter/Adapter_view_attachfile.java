package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.swipe.SwipeLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.Pdfviewer;
import com.ronnelrazo.physical_counting.Pdfviewer_single;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.config;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_file_upload;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.model.model_viewfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class Adapter_view_attachfile extends RecyclerView.Adapter<Adapter_view_attachfile.ViewHolder> {
    Context mContext;
    List<model_file_upload> newsList;
    LottieAnimationView loadinglist;


   PDFView pdfView;

    public Adapter_view_attachfile(List<model_file_upload> list, Context context,LottieAnimationView loadinglist) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.loadinglist = loadinglist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_attach_file_dialog_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_file_upload getData = newsList.get(position);

        holder.filename.setText(getData.getFile().split("/")[1]);
        holder.swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF(v, getData);
            }
        });

        holder.trash.setOnClickListener(v -> {
            String getAudit = getData.getAudit_no();
            String getPath = getData.getFile();
            removeItem(position,v,getAudit,getPath);
        });

    }

    protected void removeItem(int position,View v,String audit,String path){
        loadinglist.setVisibility(View.VISIBLE);
        loadinglist.setAnimation(R.raw.loading);
        loadinglist.loop(true);
        loadinglist.playAnimation();
        API.getClient().remove_attach_file(audit,path).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        loadinglist.setVisibility(View.GONE);
                        loadinglist.loop(true);
                        loadinglist.playAnimation();
                        removeAt(position);
                    }
                    else{
                        loadinglist.setVisibility(View.GONE);
                        loadinglist.loop(true);
                        loadinglist.playAnimation();
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");

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


    public void removeAt(int position) {
        newsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, newsList.size());
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
        public SwipeLayout swipe;
        public LinearLayout trash;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            imgicon = itemView.findViewById(R.id.imgicon);
            filename = itemView.findViewById(R.id.filename);
            swipe = itemView.findViewById(R.id.swipe);
            trash = itemView.findViewById(R.id.cancelitem);

        }
    }
}
