package com.ronnelrazo.physical_counting.adapter;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ronnelrazo.physical_counting.ItemClickListener;
import com.ronnelrazo.physical_counting.Pdf_record_list;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_pdf_report;
import com.ronnelrazo.physical_counting.model.model_feed;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adapter_PDFReport extends RecyclerView.Adapter<Adapter_PDFReport.ViewHolder> {
    Context mContext;
    List<modal_pdf_report> newsList;
    ItemClickListener itemClickListener;
    int selectedPostion = -1;




    public Adapter_PDFReport(List<modal_pdf_report> list, Context context, ItemClickListener itemClickListener) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.itemClickListener = itemClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_listreport,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_pdf_report getData = newsList.get(position);

        holder.org_name.setText(getData.getOrg_name() + " - " + getData.getOrg_code());
        holder.farm_name.setText(getData.getFarm_name() + " - " + getData.getFarm_code());
        holder.audi_No.setText("Audit No. : "+getData.getAudit_no());
        holder.audi_date.setText("Audit Date : "+getData.getAudit_date());


        holder.pdfbtn.setOnClickListener(v-> {
            String getOrgCode = getData.getOrg_code();
            String getFarmCode = getData.getFarm_code();

//            Toast.makeText(v.getContext(), getOrgCode +  " " + getFarmCode, Toast.LENGTH_SHORT).show();
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext(),R.style.full_screen_dialog);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.pdf_modal,null);

            dialogBuilder.setView(view);

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            BounceView.addAnimTo(alertDialog);
            alertDialog.show();

        });


        holder.radioButton.setChecked(position == selectedPostion);
        holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
             if(isChecked){
                holder.pdfbtn.setVisibility(View.VISIBLE);
                holder.container.setBackgroundColor(Color.parseColor("#ecf0f1"));
             }
             else{
                 holder.pdfbtn.setVisibility(View.GONE);
                 holder.container.setBackgroundColor(Color.parseColor("#FFFFFF"));
             }
             selectedPostion = holder.getAdapterPosition();
             itemClickListener.onClick(getData.getFarm_name());
        });

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

        public RadioButton radioButton;
        public TextView org_name,farm_name,audi_No,audi_date;
        public MaterialButton pdfbtn;
        public RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radio_btn);
            org_name = itemView.findViewById(R.id.org_name);
            farm_name = itemView.findViewById(R.id.farm_name);
            audi_No = itemView.findViewById(R.id.audi_No);
            pdfbtn = itemView.findViewById(R.id.pdfbtn);
            audi_date = itemView.findViewById(R.id.audi_date);
            container = itemView.findViewById(R.id.container);


        }
    }
}
