package com.ronnelrazo.physical_counting.adapter;


import android.app.DatePickerDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.ronnelrazo.physical_counting.Integration_submenu;
import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_Farmlist;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.ArrayList;
import java.util.Calendar;

public class Adapter_FarmList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ListItem> items;

    public Adapter_FarmList(ArrayList<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ListItem.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return  new VHHeader(v);
        } else if(viewType == ListItem.TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHHeader) {
            model_header_farm_org header = (model_header_farm_org) items.get(position);
            VHHeader VHheader = (VHHeader)holder;
            VHheader.tvName.setText(header.getHeader());
        } else if(holder instanceof VHItem) {
            model_Farmlist orgData = (model_Farmlist) items.get(position);
            VHItem VHitem = (VHItem)holder;
            BounceView.addAnimTo(VHitem.card);
            VHitem.org_code.setText(orgData.getFarmcode());
            VHitem.org_name.setText(orgData.getFarmname());
            VHitem.card.setOnClickListener(v -> {

                Globalfunction.getInstance(v.getContext()).auditDialog(v.getContext());
                Globalfunction.getInstance(v.getContext()).audit_save.setOnClickListener(v1 -> {

                    String getCurrentDate = Globalfunction.getInstance(v1.getContext()).currentDate.getText().toString();
                    String getAuditDate = Globalfunction.getInstance(v1.getContext()).auditDate.getText().toString();


                    if(getCurrentDate.isEmpty()){
//                        Globalfunction.getInstance(v1.getContext()).toast(R.raw.error,"Invalid Date", Gravity.TOP|Gravity.CENTER,0,50); //50
                        Globalfunction.getInstance(v1.getContext()).toast(R.raw.error,"Invalid Audit Date", Gravity.TOP|Gravity.CENTER,0,50); //50
                        new DatePickerDialog(v1.getContext(),R.style.picker,Globalfunction.getInstance(v1.getContext()).getDateto(Globalfunction.getInstance(v1.getContext()).currentDate), Globalfunction.getInstance(v1.getContext()).calendar
                                .get(Calendar.YEAR), Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.MONTH),
                                Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                    else if(getAuditDate.isEmpty()){
                        Globalfunction.getInstance(v1.getContext()).toast(R.raw.error,"Invalid Audit Date", Gravity.TOP|Gravity.CENTER,0,50); //50
                        new DatePickerDialog(v1.getContext(),R.style.picker,Globalfunction.getInstance(v1.getContext()).getDateto(Globalfunction.getInstance(v1.getContext()).auditDate), Globalfunction.getInstance(v1.getContext()).calendar
                                .get(Calendar.YEAR), Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.MONTH),
                                Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                    else{
                        Globalfunction.getInstance(v1.getContext()).Auditalert.dismiss();
                        Globalfunction.getInstance(v.getContext()).intent(tab_from.class,v.getContext());
                        tab_from.str_types = "Integration";
                        Tab_checklist.str_bu_Type = "SWINT_1";
                        tab_from.str_orgcode = orgData.getOrgcode();
                        tab_from.str_orgname = orgData.getOrgname();
                        tab_from.str_farmcode = orgData.getFarmcode();
                        tab_from.str_farmname = orgData.getFarmname();
                        tab_from.doc_date = getCurrentDate;
                        tab_from.audit_date = getAuditDate;

                        tab_from.bu_code = orgData.getBu_code();
                        tab_from.bu_name = orgData.getBu_name();
                        tab_from.bu_type_name = orgData.getBu_type_name();
                        tab_from.business_type = orgData.getBu_type_code();

                        Tab_breeder.BU_CODE = "SWINT_1";
                        Tab_breeder.BU_TYPE = "SW";
                    }

                });
                Globalfunction.getInstance(v.getContext()).audit_cancel.setOnClickListener(v1 -> {
                    Globalfunction.getInstance(v1.getContext()).Auditalert.dismiss();
                });





            });
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

    class VHHeader extends RecyclerView.ViewHolder{
        public TextView tvName;
        public VHHeader(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.header_title);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView org_code,org_name;
        public VHItem(View itemView) {
            super(itemView);
            this.card = itemView.findViewById(R.id.card);
            this.org_code = itemView.findViewById(R.id.org_code);
            this.org_name = itemView.findViewById(R.id.org_name);
        }
    }
}