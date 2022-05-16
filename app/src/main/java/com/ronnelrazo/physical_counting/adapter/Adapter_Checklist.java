package com.ronnelrazo.physical_counting.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.Integration_submenu;
import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.ListItem_Checklist;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_checklist_Details;
import com.ronnelrazo.physical_counting.model.modal_checklist_SubDetails;
import com.ronnelrazo.physical_counting.model.modal_checklist_maintopic;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.ArrayList;

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


            if(details.getDetails().equals("free_text")){
                item.checklistA.setVisibility(View.GONE);
                item.freeText.setVisibility(View.VISIBLE);
            }
            else{
                item.checklistA.setVisibility(View.VISIBLE);
                item.freeText.setVisibility(View.GONE);
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
//            Log.d("swine",getCheckedvalue);
                item.itemGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    RadioButton checked = radioGroup.findViewById(i);
                    String rbcheckStatus = checked.getId() == R.id.item_yes ? "Y" :
                            (checked.getId() == R.id.item_no ? "N" :
                                    (checked.getId() == R.id.item_na ? "N/A" : null));
                    String getRemarkchangeans = item.item_remarks.getText().toString();
                    Log.d("swine",rbcheckStatus + " position:" +position);
                    boolean update_postion =  Globalfunction.getInstance(context).updatechecklist(position,tab_from.str_orgcode,tab_from.str_farmcode,rbcheckStatus,getRemarkchangeans,details.getM_code(),
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
//                    Log.d("swine",getRemark + " position:" +position + " checked :" + getCheckedvalue);
                        boolean update_postion =  Globalfunction.getInstance(context).updatechecklist(position,tab_from.str_orgcode,tab_from.str_farmcode,getCheckedvalue,getRemark,details.getM_code(),
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
                        if(update_postion){
                            Log.d("swine","update position:" +position + " value : " + getCheckedvalue + " remake: " + getRemark);
                        }


                    }
                });

            }





        }
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
        public LinearLayout checklistA;
        public VHItem(View itemView) {
            super(itemView);
            this.itemDetails = itemView.findViewById(R.id.itemDetails);
            this.itemGroup = itemView.findViewById(R.id.item_group_button);
            this.item_remarks = itemView.findViewById(R.id.item_remarks);
            this.choices = itemGroup.findViewById(itemGroup.getCheckedRadioButtonId());
            this.freeText = itemView.findViewById(R.id.freeText);
            this.checklistA = itemView.findViewById(R.id.checklistA);


        }
    }
}