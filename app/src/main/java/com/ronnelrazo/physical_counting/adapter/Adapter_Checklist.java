package com.ronnelrazo.physical_counting.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.Integration_submenu;
import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.ListItem_Checklist;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.connection.API_;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_checklist_Details;
import com.ronnelrazo.physical_counting.model.modal_checklist_SubDetails;
import com.ronnelrazo.physical_counting.model.modal_checklist_maintopic;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_Checklist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ListItem_Checklist> items;
    private Context context;


    public Adapter_Checklist(ArrayList<ListItem_Checklist> items,Context context) {
        this.items = items;
        this.context = context;

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
            modal_checklist_Details details = (modal_checklist_Details) items.get(position);
            VHItem item = (VHItem)holder;

            if(details.getDetails().equals("text_view")){
                item.a1_form.setVisibility(View.VISIBLE);
                item.checklistA.setVisibility(View.GONE);
                item.freeText.setVisibility(View.GONE);
                getA1_1Form(tab_from.str_orgcode,item);
                item.audit_date.setText(Html.fromHtml(" <strong>Date of Audit : </strong>" + tab_from.audit_date));

                boolean save_details = Globalfunction.getInstance(context)
                        .ADD_CHECKLIST_HEADER_DETAILS(
                                position,
                                tab_from.str_orgcode,
                                tab_from.str_farmcode,
                                tab_from.str_types,
                                null,
                                null,
                                details.getM_code(),
                                details.getM_desc(),
                                details.getM_seq(),
                                details.getS_code(),
                                details.getS_desc(),
                                details.getS_seq(),
                                details.getDetails_code(),
                                details.getDetails(),
                                details.getDetails_seq(),
                                details.getBu_code(),
                                details.getBu_type());
                if(save_details){
                    Log.d("swine","save header" + details.getDetails());
                }
                else{
                    Log.d("swine","existing header" +  details.getDetails());
                }

            }

            else if(details.getDetails().equals("null")){
                item.checklistA.setVisibility(View.GONE);
                item.freeText.setVisibility(View.VISIBLE);
                item.a1_form.setVisibility(View.GONE);

                item.freeText.setOnTouchListener((v, event) -> {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                });
                item.freeText.setMovementMethod(new ScrollingMovementMethod());


                boolean save_details = Globalfunction.getInstance(context)
                        .ADD_CHECKLIST_HEADER_DETAILS(
                                position,
                                tab_from.str_orgcode,
                                tab_from.str_farmcode,
                                tab_from.str_types,
                                "",
                                "",
                                details.getM_code(),
                                details.getM_desc(),
                                details.getM_seq(),
                                details.getS_code(),
                                details.getS_desc(),
                                details.getS_seq(),
                                details.getDetails_code(),
                                details.getDetails(),
                                details.getDetails_seq(),
                                details.getBu_code(),
                                details.getBu_type());
                if(save_details){
                    Log.d("swine","save freeText" + position);
                }
                else{
                    Log.d("swine","existing freeText" + position);
                }

                item.freeText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        boolean update_postion =  Globalfunction.getInstance(context).updatechecklist(position,tab_from.str_orgcode,tab_from.str_farmcode,"",s.toString());
                        if(update_postion){
                            Log.d("swine","update position:" +position + " value : " + s);
                        }
                    }
                });


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

                boolean save_details = Globalfunction.getInstance(context)
                        .ADD_CHECKLIST_HEADER_DETAILS(
                                position,
                                tab_from.str_orgcode,
                                tab_from.str_farmcode,
                                tab_from.str_types,
                                getCheckedvalue,
                                getRemark,
                                details.getM_code(),
                                details.getM_desc(),
                                details.getM_seq(),
                                details.getS_code(),
                                details.getS_desc(),
                                details.getS_seq(),
                                details.getDetails_code(),
                                details.getDetails(),
                                details.getDetails_seq(),
                                details.getBu_code(),
                                details.getBu_type());
                if(save_details){
                    Log.d("swine","save header" + getCheckedvalue);
                }
                else{
                    Log.d("swine","existing header" + getCheckedvalue);
                }
                item.itemGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    RadioButton checked = radioGroup.findViewById(i);
                    String rbcheckStatus = checked.getId() == R.id.item_yes ? "Y" :
                            (checked.getId() == R.id.item_no ? "N" :
                                    (checked.getId() == R.id.item_na ? "N/A" : null));
                    String getRemarkchangeans = item.item_remarks.getText().toString();
                    Log.d("swine",rbcheckStatus + " position:" +position);
                    if(rbcheckStatus.equals("N")){
                        item.item_remarks.requestFocus();
                        Toast.makeText(context, "Please State the Reason", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        item.item_remarks.clearFocus();
                    }
                    boolean update_postion =  Globalfunction.getInstance(context).updatechecklist(position,tab_from.str_orgcode,tab_from.str_farmcode,rbcheckStatus,getRemarkchangeans);
                    if(update_postion){
                        Log.d("swine","update position:" +position + " value : " + rbcheckStatus + " remake: " + getRemarkchangeans);
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
                        boolean update_postion =  Globalfunction.getInstance(context).updatechecklist(position,tab_from.str_orgcode,tab_from.str_farmcode,getCheckedvalue,getRemark);
                        if(update_postion){
                            Log.d("swine","update position:" +position + " value : " + getCheckedvalue + " remake: " + getRemark);
                        }


                    }
                });


            }





        }
    }


    protected void getA1_1Form(String BU_CODE,VHItem item){
        Log.d("A1_form",BU_CODE);
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
                            item.business_farmname.setText(Html.fromHtml("<strong>Business/Farm Name : </strong>" +object.getString("ORG_NAME")));
                            item.businessAddress.setText(Html.fromHtml("<strong>Business address :  </strong>" +object.getString("ADDRESS")));
                            item.farmManager.setText(Html.fromHtml("<strong>Farm Manager :  </strong>" +object.getString("FARM_MANAGER_NAME")));
                            item.headAnimalHusbandman.setText(Html.fromHtml("<strong>Head Animal Husbandman :  </strong>" +object.getString("FARM_CLERK_NAME")));
                            item.contact.setText(Html.fromHtml("<strong>Contact Number :   </strong>" +object.getString("FARM_MANAGER_CONTACT_NO")));
                        }

                    }
                    else{
                        Globalfunction.getInstance(context).toast(R.raw.error,"Invalid Params", Gravity.TOP|Gravity.CENTER,0,50); //50
                        ((tab_from)context).btn_func[0].setEnabled(false);
                        ((tab_from)context).btn_func[1].setEnabled(false);
                        item.business_farmname.setTextColor(Color.parseColor("#e74c3c"));
                        item.businessAddress.setTextColor(Color.parseColor("#e74c3c"));
                        item.farmManager.setTextColor(Color.parseColor("#e74c3c"));
                        item.headAnimalHusbandman.setTextColor(Color.parseColor("#e74c3c"));
                        item.contact.setTextColor(Color.parseColor("#e74c3c"));

                        item.business_farmname.setText("Business/Farm Name:   *");
                        item.businessAddress.setText("Business address:  *");
                        item.farmManager.setText("Farm Manager:   *");
                        item.headAnimalHusbandman.setText("Head Animal Husbandman: *");
                        item.contact.setText("Contact Number:   *");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                }
            }


//            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Globalfunction.getInstance(context).toast(R.raw.error,"Please Setup Farm Information first to continue this form. Thank you [Error Code A1.1]", Gravity.TOP|Gravity.CENTER,0,50);
                    ((tab_from)context).btn_func[0].setEnabled(false);
                    ((tab_from)context).btn_func[1].setEnabled(false);
                    item.business_farmname.setTextColor(Color.parseColor("#e74c3c"));
                    item.businessAddress.setTextColor(Color.parseColor("#e74c3c"));
                    item.farmManager.setTextColor(Color.parseColor("#e74c3c"));
                    item.headAnimalHusbandman.setTextColor(Color.parseColor("#e74c3c"));
                    item.contact.setTextColor(Color.parseColor("#e74c3c"));

                    item.business_farmname.setText("Business/Farm Name:   *");
                    item.businessAddress.setText("Business address:  *");
                    item.farmManager.setText("Farm Manager:   *");
                    item.headAnimalHusbandman.setText("Head Animal Husbandman: *");
                    item.contact.setText("Contact Number:   *");
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