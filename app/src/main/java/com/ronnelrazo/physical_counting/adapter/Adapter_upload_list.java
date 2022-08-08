package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.modal_interface;
import com.ronnelrazo.physical_counting.model.model_cancel_list;
import com.ronnelrazo.physical_counting.model.model_file_upload;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.model.model_upload_list;
import com.ronnelrazo.physical_counting.model.model_viewfile;
import com.ronnelrazo.physical_counting.upload_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_upload_list extends RecyclerView.Adapter<Adapter_upload_list.ViewHolder> {
    Context mContext;
    List<model_upload_list> newsList;


    modal_interface modal_interface;

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<model_file_upload> list;

    public Adapter_upload_list(List<model_upload_list> list, Context context,modal_interface modal_interface) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.modal_interface = modal_interface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_upload_list getData = newsList.get(position);

        holder.Orgcode.setText(Html.fromHtml(String.format("<strong>Org Name : </strong>%s (%s)", getData.getFARM_NAME(), getData.getORG_CODE())));
        holder.auditNo.setText(Html.fromHtml(String.format("<strong>Audit No. : </strong>%s", getData.getAUDIT_NO())));
        holder.Document_date.setText(Html.fromHtml(String.format("<strong>Audit Date : </strong>%s", getData.getAUDIT_DATE())));

        if(getData.isAttachfile()){
            holder.viewAttachfile.setEnabled(true);
        }
        else{
            holder.viewAttachfile.setEnabled(false);
        }



        holder.viewAttachfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(v, getData);
            }
        });


        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal_interface.onClick(v,getData.getAUDIT_NO(),"attachfile",getData.ORG_CODE);
            }
        });

        holder.checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal_interface.onClick(v,getData.getAUDIT_NO(),"checklist",getData.ORG_CODE);
            }
        });

        holder.breeder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal_interface.onClick(v,getData.getAUDIT_NO(),"breeder",getData.ORG_CODE);
            }
        });

        holder.feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal_interface.onClick(v,getData.getAUDIT_NO(),"feed",getData.ORG_CODE);
            }
        });

        holder.med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal_interface.onClick(v,getData.getAUDIT_NO(),"med",getData.ORG_CODE);
            }
        });

    }

    private void showBottomSheetDialog(View v, model_upload_list getData) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.view_attach_file_dialog_layout,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);

//        LinearLayout linearLayout = view.findViewById(R.id.root);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
//        params.height = getScreenHeight();
//        linearLayout.setLayoutParams(params);

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
        adapter = new Adapter_view_attachfile(list,view.getContext(),loading);
        rviewbottom.setAdapter(adapter);



            loadData(v,getData.getAUDIT_NO(),loading,rviewbottom);

        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

    }

    private void loadData(View v,String audit,LottieAnimationView loading,RecyclerView recyclerView) {
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

                        adapter = new Adapter_view_attachfile(list,v.getContext(),loading);
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


    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CardView card;
        public TextView Orgcode,auditNo,Document_date;
        public MaterialButton checklist,breeder,feed,med,upload,viewAttachfile;


        public ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            Orgcode = itemView.findViewById(R.id.Orgcode);
            auditNo = itemView.findViewById(R.id.auditNo);
            Document_date = itemView.findViewById(R.id.Document_date);

            checklist = itemView.findViewById(R.id.checklist);
            breeder = itemView.findViewById(R.id.breeder);
            feed = itemView.findViewById(R.id.feed);
            med = itemView.findViewById(R.id.med);
            upload = itemView.findViewById(R.id.upload);
            viewAttachfile= itemView.findViewById(R.id.viewAttachfile);

        }
    }
}
