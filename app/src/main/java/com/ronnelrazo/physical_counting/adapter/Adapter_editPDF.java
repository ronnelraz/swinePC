package com.ronnelrazo.physical_counting.adapter;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.edit_TabForm;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_feed_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_med_edit;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_edit_list;
import com.ronnelrazo.physical_counting.model.model_feed;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.List;

public class Adapter_editPDF extends RecyclerView.Adapter<Adapter_editPDF.ViewHolder> {
    Context mContext;
    List<model_edit_list> newsList;



    public Adapter_editPDF(List<model_edit_list> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_pdf_list_detials,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_edit_list getData = newsList.get(position);

        holder.org_name.setText(String.format("%s (%s)", getData.getOrg_name(), getData.getOrg_code()));
        holder.farm_name.setText(String.format("%s (%s)", getData.getFarm_name(), getData.getFarm_code()));
        holder.audit_no.setText(getData.getAudit_no());
        holder.audit_date.setText(getData.getDoc_date());
        holder.card.setOnClickListener(v -> {
          Log.d("swine",getData.getOrg_code() + " " + getData.getFarm_code());
             Globalfunction.getInstance(v.getContext()).intent(edit_TabForm.class,v.getContext());
            edit_TabForm.Getorg_code = getData.getOrg_code();
            edit_TabForm.Getfarm_code = getData.getFarm_code();
            edit_TabForm.getAudit_no = getData.getAudit_no();
            Tab_checklist_edit.AUDIT_NO = getData.getAudit_no();
            Tab_checklist_edit.str_orgcode = getData.getOrg_code();
            Tab_checklist_edit.str_audit_date = getData.getAudit_date();
            Tab_breeder_edit.str_audit_no = getData.getAudit_no();
            Tab_feed_edit.str_audit_no = getData.getAudit_no();
            Tab_med_edit.audit_no = getData.getAudit_no();
        });




    }



    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView org_name,farm_name,audit_no,audit_date;
        public CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            org_name = itemView.findViewById(R.id.org_name);
            farm_name = itemView.findViewById(R.id.farm_name);
            audit_no = itemView.findViewById(R.id.audit_no);
            audit_date = itemView.findViewById(R.id.audit_date);
            card = itemView.findViewById(R.id.card);

        }
    }
}
