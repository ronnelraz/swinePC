package com.ronnelrazo.physical_counting.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.ListItem_Checklist;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist_edit;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_checklist_Details;
import com.ronnelrazo.physical_counting.model.modal_checklist_Details_Edit;
import com.ronnelrazo.physical_counting.model.modal_checklist_SubDetails;
import com.ronnelrazo.physical_counting.model.modal_checklist_maintopic;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_Checklist_edit extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ListItem_Checklist> items;
    private Context context;
    private String audit_no;

    long delay = 3000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();


    public Adapter_Checklist_edit(ArrayList<ListItem_Checklist> items, Context context,String audit_no) {
        this.items = items;
        this.context = context;
        this.audit_no = audit_no;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ListItem_Checklist.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_details_main_header, parent, false);
            return  new VHSubDetails(v);
        } else if(viewType == ListItem_Checklist.TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_checklist_items, parent, false);
            return new VHItem(v);
        }
        else if(viewType == ListItem_Checklist.TYPE_MAIN) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_topic, parent, false);
            return new VHMain(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(holder instanceof VHMain) {
            modal_checklist_maintopic main = (modal_checklist_maintopic) items.get(position);
            VHMain mainTopic = (VHMain)holder;
            if(position == 0){
                mainTopic.MainTopic.setText(main.getMainTopic());
            }
            else{
                if(main.getMainTopic().isEmpty()){
                    mainTopic.MainTopic.setVisibility(View.GONE);
                }
                else{
                    mainTopic.annexHeader.setVisibility(View.VISIBLE);
                    mainTopic.MainTopic.setText(main.getMainTopic());
                }

            }
        } else if(holder instanceof VHSubDetails) {
            modal_checklist_SubDetails subDetails = (modal_checklist_SubDetails) items.get(position);
            VHSubDetails sub = (VHSubDetails)holder;
            sub.sub_details_header.setText(subDetails.getSub_details());

        }
        else if(holder instanceof VHItem) {
            modal_checklist_Details_Edit details = (modal_checklist_Details_Edit) items.get(position);
            VHItem item = (VHItem)holder;

            if(!details.getCheck_item().equals("null")){
                int rbutonid = details.getCheck_item().equals("Y") ? R.id.item_yes : (details.getCheck_item().equals("N") ? R.id.item_no : R.id.item_na);
                item.itemGroup.check(rbutonid);
            }
            item.item_remarks.setText(details.getRemark());



            if(details.getDetails().equals("text_view")){
                item.a1_form.setVisibility(View.VISIBLE);
                item.checklistA.setVisibility(View.GONE);
                item.freeText.setVisibility(View.GONE);
                getA1_1Form(Tab_checklist_edit.str_orgcode,item);
                item.audit_date.setText("Date of Audit:  " + Tab_checklist_edit.str_audit_date);


            }

            else if(details.getDetails().equals("null")){
                item.checklistA.setVisibility(View.GONE);
                item.freeText.setVisibility(View.VISIBLE);
                item.a1_form.setVisibility(View.GONE);
                item.freeText.setText(details.getRemark());

                item.freeText.setOnTouchListener((v, event) -> {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                });
                item.freeText.setMovementMethod(new ScrollingMovementMethod());





            }
            else{
                item.checklistA.setVisibility(View.VISIBLE);
                item.freeText.setVisibility(View.GONE);
                item.a1_form.setVisibility(View.GONE);
                item.itemDetails.setText(Globalfunction.createIndentedText(details.getDetails(),10,50));
                String getRemark = item.item_remarks.getText().toString();
                String getCheckedvalue =  item.itemGroup.getCheckedRadioButtonId() == R.id.item_na ? "N/A" :
                        (item.itemGroup.getCheckedRadioButtonId() == R.id.item_yes ? "Y" :
                                (item.itemGroup.getCheckedRadioButtonId() == R.id.item_no ? "N" : "N/A"));


                item.itemGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    RadioButton checked = radioGroup.findViewById(i);
                    String rbcheckStatus = checked.getId() == R.id.item_yes ? "Y" :
                            (checked.getId() == R.id.item_no ? "N" :
                                    (checked.getId() == R.id.item_na ? "N/A" : null));
                    String getRemarkchangeans = item.item_remarks.getText().toString();
                    Log.d("swine",rbcheckStatus + " position:" +position);
                    updateChanges(rbcheckStatus,getRemarkchangeans,audit_no,details.getM_code(),details.getS_code(),details.getDetails_code());



                    if(rbcheckStatus.equals("N")){
                        item.item_remarks.requestFocus();
                        Toast.makeText(context, "Please State the Reason", Toast.LENGTH_SHORT).show();
                        item.item_remarks.setText("");
                    }
                    else{
                        item.item_remarks.clearFocus();
                    }


                });



                item.item_remarks.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        String getRemark = editable.toString();
                        String getCheckedvalue =  item.itemGroup.getCheckedRadioButtonId() == R.id.item_na ? "N/A" :
                                (item.itemGroup.getCheckedRadioButtonId() == R.id.item_yes ? "Y" :
                                        (item.itemGroup.getCheckedRadioButtonId() == R.id.item_no ? "N" : "N/A"));
                        if (editable.length() > 0) {
                            last_text_edit = System.currentTimeMillis();
                             handler.postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                         updateChanges(getCheckedvalue,getRemark,audit_no,details.getM_code(),details.getS_code(),details.getDetails_code());
                                     }
                                 }
                             },delay);
                        } else {

                        }




                    }
                });


            }





        }
    }


    protected void updateChanges(String check,String remark,String audit_no,String main_code,String sub_code,String details_code){
        API.getClient().update_checklist(check,remark,audit_no,main_code,sub_code,details_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Log.d("swine","save");
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Globalfunction.getInstance(context).toast(R.raw.error,"Invalid Params checklist adapter", Gravity.TOP|Gravity.CENTER,0,50); //50
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Sorry Farm Details not yet setup, Please setup up first to complete the information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    protected void getA1_1Form(String BU_CODE,VHItem item){
        API.getClient().A1_1Form(BU_CODE).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray data = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            item.business_farmname.setText("Business/Farm Name:   "+Html.fromHtml("<b>"+object.getString("ORG_NAME")+"</b>"));
                            item.businessAddress.setText("Business address:   " +object.getString("ADDRESS"));
                            item.farmManager.setText("Farm Manager:   " +object.getString("FARM_MANAGER_NAME"));
                            item.headAnimalHusbandman.setText("Head Animal Husbandman:  " +object.getString("FARM_CLERK_NAME"));
                            item.contact.setText("Contact Number:   " +object.getString("FARM_MANAGER_CONTACT_NO"));
                        }

                    }
                    else{
                        Globalfunction.getInstance(context).toast(R.raw.error,"Invalid Params Checklist Form", Gravity.TOP|Gravity.CENTER,0,50); //50
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Sorry Farm Details not yet setup, Please setup up first to complete the information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    class VHMain extends RecyclerView.ViewHolder{
        public TextView MainTopic;
        public LinearLayout annexHeader;
        public VHMain(View itemView) {
            super(itemView);
            this.MainTopic = itemView.findViewById(R.id.maintopic);
            this.annexHeader = itemView.findViewById(R.id.headerAnnex);
        }
    }

    class VHSubDetails extends RecyclerView.ViewHolder{
        public TextView sub_details_header;
        public VHSubDetails(View itemView) {
            super(itemView);
            this.sub_details_header = itemView.findViewById(R.id.sub_details_header);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        public TextView itemDetails;
        public RadioGroup itemGroup;
        public EditText item_remarks;
        public RadioButton choices;
        public EditText freeText;
        public LinearLayout checklistA,a1_form;
        public ScrollView scroller;

        public TextView business_farmname,businessAddress,farmManager,headAnimalHusbandman,contact,audit_date;
        public VHItem(View itemView) {
            super(itemView);
            this.itemDetails = itemView.findViewById(R.id.itemDetails);
            this.itemGroup = itemView.findViewById(R.id.item_group_button);
            this.item_remarks = itemView.findViewById(R.id.item_remarks);
            this.choices = itemGroup.findViewById(itemGroup.getCheckedRadioButtonId());
            this.freeText = itemView.findViewById(R.id.freeText);
            this.checklistA = itemView.findViewById(R.id.checklistA);
            this.a1_form = itemView.findViewById(R.id.a1_form);

            this.business_farmname = itemView.findViewById(R.id.business_farmname);
            this.businessAddress = itemView.findViewById(R.id.businessAddress);
            this.farmManager = itemView.findViewById(R.id.farmManager);
            this.headAnimalHusbandman = itemView.findViewById(R.id.headAnimalHusbandman);
            this.contact = itemView.findViewById(R.id.contact);
            this.audit_date = itemView.findViewById(R.id.audit_dateA1);
            this.scroller = itemView.findViewById(R.id.bodyScroller);


        }
    }
}