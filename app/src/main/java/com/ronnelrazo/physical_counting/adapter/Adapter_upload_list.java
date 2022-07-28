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
import androidx.recyclerview.widget.GridLayoutManager;
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

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<model_viewfile> list;

    public String Attach_Type = "";
    public int REQ_CODE_IMG = 2021;
    public int REQ_CODE_PDF = 2022;
    public Bitmap bitmap;
    public Intent intent;


    AlertDialog ConfirmDialog;

    public modal_interface modal_interface;

    public Adapter_upload_list(List<model_upload_list> list, Context context,Intent intent,modal_interface modal_interface) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.intent = intent;
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
        holder.audit.setText(String.format("%s (%s)", getData.getFarm(), getData.getAudit_no()));
        BounceView.addAnimTo(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomsheet_items,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);
            LinearLayout linearLayout = view.findViewById(R.id.root);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height = getScreenHeight();
            linearLayout.setLayoutParams(params);

            MaterialButton back = view.findViewById(R.id.back);
            MaterialButton update = view.findViewById(R.id.update);
            back.setOnClickListener(v1 -> {
                list.clear();
                bottomSheetDialog.dismiss();
            });


            update.setOnClickListener(v1 -> {
                Globalfunction data = new Globalfunction(v1.getContext());
                MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
                View views = LayoutInflater.from(view.getContext()).inflate(R.layout.uploadfile_modal,null);

                AutoCompleteTextView audit_no = views.findViewById(R.id.audit_no);
                audit_no.setVisibility(View.GONE);
                TextView titlem = views.findViewById(R.id.titlem);
                titlem.setVisibility(View.GONE);
                MaterialButton uplaodbtn = views.findViewById(R.id.uploadimg);
                MaterialButton uplaodbtnpdf = views.findViewById(R.id.uploadpdf);


                uplaodbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modal_interface.onClick(v,getData.getAudit_no(),"img",list,adapter);
                    }
                });

                uplaodbtnpdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modal_interface.onClick(v,getData.getAudit_no(),"pdf",list,adapter);
                    }
                });


                Materialdialog.setView(views);
                ConfirmDialog = Materialdialog.create();
                ConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                BounceView.addAnimTo(ConfirmDialog);
                ConfirmDialog.show();


            });



            RecyclerView rviewbottom = view.findViewById(R.id.data);
            SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);


            rviewbottom.setHasFixedSize(true);
            rviewbottom.setItemViewCacheSize(999999999);

            list = new ArrayList<>();
            rviewbottom.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            adapter = new Adapter_viewItems(list,view.getContext());
            rviewbottom.setAdapter(adapter);
            swipe.setOnRefreshListener(() -> {
                list.clear();
                loadData(getData.getAudit_no(),view,rviewbottom);
                swipe.setRefreshing(false);
            });

            swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            loadData(getData.audit_no,view,rviewbottom);

            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetDialog.show();

        });

    }

    private void loadData(String audit,View v,RecyclerView recyclerView){
        list.clear();
        adapter.notifyDataSetChanged();
        API.getClient().ViewFiles(audit).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            model_viewfile item = new model_viewfile(
                                    object.getString("files")
                            );
                            list.add(item);


                        }

                        adapter = new Adapter_viewItems(list,v.getContext());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        list.clear();
                        Toast.makeText(v.getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {

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


        public ImageView imageView;
        public TextView audit;

        public ViewHolder(View itemView) {
            super(itemView);
                imageView = itemView.findViewById(R.id.folder);
                audit = itemView.findViewById(R.id.audit);

        }
    }
}
