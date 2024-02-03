package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.Login;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_org_code;
import com.ronnelrazo.physical_counting.model.modal_user_list;
import com.ronnelrazo.physical_counting.model.model_confirm_list;
import com.ronnelrazo.physical_counting.model.model_transaction_details;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.ronnelrazo.physical_counting.update_save_click_listener;
import com.ronnelrazo.physical_counting.user_setup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_user_list extends RecyclerView.Adapter<Adapter_user_list.ViewHolder> {
    Context mContext;
    List<modal_user_list> newsList;
    ArrayAdapter menu_Adapter;
    boolean click = true;

    BottomSheetDialog bottomSheetDialog;
    update_save_click_listener save_click_listener;

    private BottomSheetBehavior bottomSheetBehavior;
    List<String> chiplist = new ArrayList<>();
    AlertDialog listmodal;
    List<modal_org_code> listItem = new ArrayList<>();
    org_code_list_adapter  adapter;

    public Adapter_user_list(List<modal_user_list> list, Context context, update_save_click_listener save_click_listener) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.save_click_listener = save_click_listener;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_user_list getData = newsList.get(position);

        holder.ad_user.setText(Html.fromHtml(String.format("<strong>User AD : </strong>%s", getData.getAD())));
        holder.role_type.setText(Html.fromHtml(String.format("<strong>Role : </strong>%s", getData.getRole_type())));

        int labelColor = getData.getStatus().equals("Y") ? Color.parseColor("#27ae60") : Color.parseColor("#e74c3c");
        String сolorString = String.format("%X", labelColor).substring(2); // !!strip alpha value!!

        holder.status.setText(Html.fromHtml(String.format("<strong>Status : </strong>%s", getData.getStatus().equals("Y") ? String.format("<font color=\"#%s\">Active</font>", сolorString) : String.format("<font color=\"#%s\">Inactive</font>", сolorString) )), TextView.BufferType.SPANNABLE);



        BounceView.addAnimTo(holder.menu_access_btn);
        BounceView.addAnimTo(holder.org_code_btn);
        BounceView.addAnimTo(holder.menu);
        BounceView.addAnimTo(holder.disabled);

        int drawableicon = getData.getStatus().equals("Y") ? R.drawable.icons8_unavailable : R.drawable.ic_icons8_done;
        String drawableColor = getData.getStatus().equals("Y") ? "#e74c3c" : "#ff669900";
        holder.menu.setImageResource(drawableicon);
        holder.disabled.setBackgroundColor(Color.parseColor(drawableColor));

        holder.disabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalask(v,getData,position);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalask(v,getData,position);
            }
        });

        holder.view_menu_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        if(getData.getRole_type().equals("ADMIN")){
            holder.farm_access.setVisibility(View.VISIBLE);
        }


        holder.farm_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(v, getData);
            }
        });


//        if(getData.getRole_type().equals("ADMIN")){
//            holder.org_codelist.setVisibility(View.GONE);
//        }
//        else{
//            holder.org_codelist.setVisibility(View.VISIBLE);
//        }
//        holder.org_codelist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(click){
//                    click = false;
//                    holder.container.setVisibility(View.VISIBLE);
//                    menu_Adapter = new Adapter_user_group_list(getData.grouplist,v.getContext(),holder.remove);
//                    holder.grouplistview.setAdapter(menu_Adapter);
//                }
//                else{
//                    click  = true;
//                    holder.container.setVisibility(View.GONE);
//                }
//            }
//        });

    }

    private void modalask(View v,modal_user_list getData,int position){
        String msg =  getData.getStatus().equals("Y") ? "Are you sure you want inactive this user?" : "Are you sure you want Active this user?";
        Globalfunction.getInstance(v.getContext()).Confirmation(v.getContext(),msg,R.drawable.ic_icons8_warning);
        Globalfunction.getInstance(v.getContext()).positive.setText("Yes");
        Globalfunction.getInstance(v.getContext()).negative.setText("No");
        Globalfunction.getInstance(v.getContext()).negative.setOnClickListener(v1 ->{
            Globalfunction.getInstance(v.getContext()).ConfirmDialog.dismiss();
        });


        Globalfunction.getInstance(v.getContext()).positive.setOnClickListener(v1 -> {
            if(getData.getStatus().equals("Y")){
                     updatestatus("N",SharedPref.getInstance(v.getContext()).getUser(),getData.getAD(),v);
                    Globalfunction.getInstance(v.getContext()).ConfirmDialog.dismiss();
                   save_click_listener.onClick(v,true);
//                    newsList.remove(position);
//                    notifyItemRemoved(position);
            }
            else{
                updatestatus("Y",SharedPref.getInstance(v.getContext()).getUser(),getData.getAD(),v);
                Globalfunction.getInstance(v.getContext()).ConfirmDialog.dismiss();
                save_click_listener.onClick(v,true);
            }
//

        });
    }

    private  void updatestatus(String status,String users,String ad,View v){
        API.getClient().update_status(status,users,ad).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Toast.makeText(v.getContext(), "Update Changes Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(v.getContext(), "error something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
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


    private void showBottomSheetDialog(View v, modal_user_list getData) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.view_user_dialog_layout,null);
        bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);


        ImageView back = view.findViewById(R.id.back);
        MaterialButton remove = view.findViewById(R.id.remove);
        MaterialButton add = view.findViewById(R.id.add);
        ListView grouplistview = view.findViewById(R.id.grouplistview);
        LinearLayout container = view.findViewById(R.id.addnew_org_Code);

        TagContainerLayout tagview = view.findViewById(R.id.tagview);
        MaterialButton btnorg_code = view.findViewById(R.id.btnorg_code);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        MaterialButton save = view.findViewById(R.id.Save);

        BounceView.addAnimTo(remove);
        BounceView.addAnimTo(back);
        BounceView.addAnimTo(add);
        BounceView.addAnimTo(cancel);
        BounceView.addAnimTo(save);
        BounceView.addAnimTo(btnorg_code);


        tagview.setBackgroundColor(Color.parseColor("#ffffff"));
        tagview.setIsTagViewSelectable(true);
        tagview.isEnableCross();
        tagview.setEnableCross(true);
        tagview.setCrossColor(Color.BLACK);
        BounceView.addAnimTo(tagview);
        tagview.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                tagview.removeTag(position);
            }
        });




        back.setOnClickListener(v1 -> {
            bottomSheetDialog.dismiss();
        });

        btnorg_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chiplist.clear();
                openAddOrgCodeModal(v,tagview);
            }
        });

        menu_Adapter = new Adapter_user_group_list(getData.grouplist,v.getContext(),remove);
        grouplistview.setAdapter(menu_Adapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click  = true;
                container.setVisibility(View.GONE);
                container.animate().alpha(0.0f);
                chiplist.clear();
                tagview.removeAllTags();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click){
                    click = false;
//                    Globalfunction.getInstance(v.getContext()).slideDown(container);
                    container.setVisibility(View.VISIBLE);
                    container.animate().alpha(1.0f);
                }
                else{
                    click  = true;
                    container.setVisibility(View.GONE);
                    container.animate().alpha(0.0f);
//                    Globalfunction.getInstance(v.getContext()).slideUp(container);
                }

//                click = !click;
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tagview.size() == 0){
                    Toast.makeText(v.getContext(), "Please Select Org Code", Toast.LENGTH_SHORT).show();
                    btnorg_code.performClick();
                }
                else{
                    for(int i = 0; i < tagview.size(); i++){
                        String getOrg_code = tagview.getTagText(i);
                        if(!(i + 1 < tagview.size())){
//                            Toast.makeText(v.getContext(), getOrg_code, Toast.LENGTH_SHORT).show();
                            Add_user_setup(getData.getAD(),getData.getRole_id(),getOrg_code, 0,v);

                        }
                        else{
                            Add_user_setup(getData.getAD(),getData.getRole_id(),getOrg_code, 1,v);
//                            Toast.makeText(v.getContext(), getOrg_code, Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });



        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();

    }

    protected void Add_user_setup(String ad,String role,String org_code,int last,View v){
        API.getClient().adduser_master(ad,role,org_code, SharedPref.getInstance(mContext).getUser()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        if(last == 0){
                            Globalfunction.getInstance(mContext).toast(R.raw.checked,"Updated Successfully!" , Gravity.BOTTOM|Gravity.CENTER,0,50);
                            bottomSheetDialog.dismiss();
                            save_click_listener.onClick(v,true);
                        }
                    }
                    else{
//                        bottomSheetDialog.dismiss();
//                        Globalfunction.getInstance(mContext).toast(R.raw.error,"Org Code : " + org_code + " already Exist" , Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Globalfunction.getInstance(mContext).toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
                }
            }
        });
    }


    protected void openAddOrgCodeModal(View view, TagContainerLayout tagview){
        MaterialAlertDialogBuilder Materialdialog = new MaterialAlertDialogBuilder(view.getContext());
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.org_code_list_modal,null);
        EditText search = v.findViewById(R.id.search);
        ListView listView = v.findViewById(R.id.listData);
        MaterialButton cancel = v.findViewById(R.id.cancel);
        MaterialButton done = v.findViewById(R.id.done);
        LottieAnimationView lottieAnimationView = v.findViewById(R.id.loading);

        listData(listView,view,lottieAnimationView,done);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listmodal.dismiss();
                listItem.clear();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<modal_org_code> items = new org_code_list_adapter(listItem,v.getContext(),done).getSelected();
                for(int i = 0; i <items.size(); i++){
                    if(!(i + 1 < items.size())) {
//                        Toast.makeText(user_setup.this, items.get(i).getOrg_code(), Toast.LENGTH_SHORT).show();
                        listmodal.dismiss();

                        addtaglist(items.get(i).getOrg_code());
                        HashSet<String> set = new HashSet<>(chiplist);
                        ArrayList<String> result = new ArrayList<>(set);
                        tagview.setTags(result);
//                        Toast.makeText(user_setup.this, "last" + i, Toast.LENGTH_SHORT).show();

                    }
                    else{
//                        Toast.makeText(user_setup.this, items.get(i).getOrg_code(), Toast.LENGTH_SHORT).show();
                        addtaglist(items.get(i).getOrg_code());
//                        Toast.makeText(user_setup.this, "more" + i, Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<modal_org_code> newListbottom = new ArrayList<>();
                for (modal_org_code sub : listItem)
                {
                    String name = sub.getName().toLowerCase();
                    String org_code = sub.getOrg_code().toLowerCase();
                    if(name.contains(s)){
                        newListbottom.add(sub);
                    }
                    else if(org_code.contains(s)){
                        newListbottom.add(sub);
                    }
                    adapter = new org_code_list_adapter(newListbottom,view.getContext(),done);
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Materialdialog.setView(v);
        listmodal = Materialdialog.create();
        listmodal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BounceView.addAnimTo(listmodal);
        listmodal.setCancelable(false);
        listmodal.setCanceledOnTouchOutside(false);
        listmodal.show();
    }

    private void addtaglist(String org_code){
        chiplist.add(org_code);
    }

    private void listData(ListView listview,View v,LottieAnimationView loading,MaterialButton done) {
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.loading);
        loading.loop(true);
        loading.playAnimation();
        listItem.clear();
        API.getClient().org_code_list().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray company_data = jsonResponse.getJSONArray("data");

                    if(success){
                        loading.setVisibility(View.GONE);
                        loading.loop(true);
                        loading.playAnimation();
                        for (int i = 0; i < company_data.length(); i++) {
                            JSONObject object = company_data.getJSONObject(i);
                            modal_org_code item = new modal_org_code(
                                    object.getString("org_code"),
                                    object.getString("org_name"),
                                    false

                            );

                            listItem.add(item);
                        }

                        adapter = new org_code_list_adapter(listItem,v.getContext(),done);
                        listview.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.nodatafile);
                        loading.setVisibility(View.VISIBLE);
                        loading.loop(true);
                        loading.playAnimation();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
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
                    loading.setAnimation(R.raw.nodatafile);
                    loading.setVisibility(View.VISIBLE);
                    loading.loop(true);
                    loading.playAnimation();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ad_user,role_type,org_codelist,menu_access_btn,org_code_btn,status;
        public LinearLayout container;
        public MaterialButton remove,view_menu_access,farm_access;
        public ListView grouplistview;
        public ImageView menu;
        public LinearLayout disabled;
        public ViewHolder(View itemView) {
            super(itemView);
            view_menu_access = itemView.findViewById(R.id.view_menu_access);
            farm_access = itemView.findViewById(R.id.farm_access);
            ad_user = itemView.findViewById(R.id.ad_user);
            role_type = itemView.findViewById(R.id.role_type);
            org_codelist = itemView.findViewById(R.id.org_codelist);
            container = itemView.findViewById(R.id.container);
            remove = itemView.findViewById(R.id.remove);
            grouplistview = itemView.findViewById(R.id.grouplistview);
            menu_access_btn = itemView.findViewById(R.id.menu_access_btn);
            org_code_btn = itemView.findViewById(R.id.org_code_btn);
            status = itemView.findViewById(R.id.status);
            menu = itemView.findViewById(R.id.menu);
            disabled = itemView.findViewById(R.id.disabled);


        }
    }
}
