package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.model.model_cancel_list;
import com.ronnelrazo.physical_counting.model.model_confirm_list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_Cancel_list extends RecyclerView.Adapter<Adapter_Cancel_list.ViewHolder> {
    Context mContext;
    List<model_cancel_list> newsList;
    MaterialButton confirm;
    CheckBox Checkedall;
    private ArrayList<CheckBox> checkBoxes;



    public Adapter_Cancel_list(List<model_cancel_list> list, Context context, MaterialButton confirm, CheckBox checkedall, ArrayList<CheckBox> checkBoxes) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.confirm = confirm;
        this.Checkedall = checkedall;
        this.checkBoxes = checkBoxes;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_cancel_list getData = newsList.get(position);

        holder.orgname.setText(String.format("%s (%s)", getData.getOrg_name(), getData.getOrg_code()));
        holder.farmname.setText(String.format("%s (%s)", getData.getFarm_name(), getData.getFarm_code()));
        holder.auditdate.setText(getData.getAudit_date());
        holder.auditno.setText(getData.getAudit_no());

        if(getData.getConfirm().equals("C")){
            holder.checkBox.setVisibility(View.GONE);
            holder.card.setCardBackgroundColor(Color.parseColor("#FFDEDE"));
        }
        else{
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        for(int i = 0; i <checkBoxes.size(); i++){
            holder.checkBox.setChecked(true);
        }

//        holder.card.setOnClickListener(v -> {
//            if(getData.isSelected){
//                getData.isSelected = false;
//                if(getSelected().size() == 0){
//                    confirm.setEnabled(false);
//                    holder.checkBox.setChecked(false);
//
//                }
//                else{
//                    confirm.setEnabled(true);
//                    holder.checkBox.setChecked(true);
//                }
//                holder.checkBox.setChecked(false);
//
//            }
//            else{
//                getData.isSelected = true;
//                confirm.setEnabled(true);
//                holder.checkBox.setChecked(true);
//            }
//        });

         holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(getData.isSelected){
                  getData.isSelected = false;
                  if(getSelected().size() == 0){
                      confirm.setEnabled(false);
                  }
                  else{
                      confirm.setEnabled(true);
                  }

              }
              else{
                  getData.isSelected = true;
                  confirm.setEnabled(true);
              }
          }
      });




    }

    public List<model_cancel_list> getSelected(){
        List<model_cancel_list> selected = new ArrayList<>();
        for(model_cancel_list list : newsList){
            if(list.isSelected()){
                selected.add(list);

            }
        }
        return selected;
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CheckBox checkBox;
        public TextView orgname,farmname,auditdate,auditno;
        public MaterialCardView card;
        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checklist);
            orgname = itemView.findViewById(R.id.orgname);
            farmname = itemView.findViewById(R.id.farmname);
            auditdate = itemView.findViewById(R.id.auditdate);
            auditno = itemView.findViewById(R.id.auditno);
            card = itemView.findViewById(R.id.card);

        }
    }
}
