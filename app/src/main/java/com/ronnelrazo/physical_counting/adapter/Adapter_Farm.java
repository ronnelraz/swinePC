package com.ronnelrazo.physical_counting.adapter;


import android.app.DatePickerDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.ronnelrazo.physical_counting.Integration_submenu;
import com.ronnelrazo.physical_counting.ListItem;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.inv_form;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.ArrayList;
import java.util.Calendar;

public class Adapter_Farm extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<ListItem> items;

    public Adapter_Farm(ArrayList<ListItem> items) {
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
            model_farm orgData = (model_farm) items.get(position);
            VHItem VHitem = (VHItem)holder;
            BounceView.addAnimTo(VHitem.card);
            VHitem.org_code.setText(orgData.getOrgcode());
            VHitem.org_name.setText(orgData.getOrgname());
            VHitem.card.setOnClickListener(v -> {

                if(orgData.getCompanyType().equals("0")){
                            Globalfunction.getInstance(v.getContext()).auditDialog(v.getContext());
                            Globalfunction.getInstance(v.getContext()).audit_save.setOnClickListener(v1 -> {

                                String getCurrentDate = Globalfunction.getInstance(v1.getContext()).currentDate.getText().toString();
                                String getAuditDate = Globalfunction.getInstance(v1.getContext()).auditDate.getText().toString();


                                if(getCurrentDate.isEmpty()){
                                    Globalfunction.getInstance(v1.getContext()).toast(R.raw.error,"Invalid Date", Gravity.TOP|Gravity.CENTER,0,50); //50
                                }
                                else if(getAuditDate.isEmpty()){
                                    Globalfunction.getInstance(v1.getContext()).toast(R.raw.error,"Invalid Audit Date", Gravity.TOP|Gravity.CENTER,0,50); //50
                                    new DatePickerDialog(v1.getContext(),R.style.picker,Globalfunction.getInstance(v1.getContext()).getDateto(), Globalfunction.getInstance(v1.getContext()).calendar
                                            .get(Calendar.YEAR), Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.MONTH),
                                            Globalfunction.getInstance(v1.getContext()).calendar.get(Calendar.DAY_OF_MONTH)).show();
                                }
                                else{
                                    Globalfunction.getInstance(v1.getContext()).Auditalert.dismiss();
                                    Globalfunction.getInstance(v1.getContext()).intent(tab_from.class,v1.getContext());
                                    tab_from.str_types = "Company Farm";
                                    Tab_checklist.str_bu_Type = "SWCOM_1";
                                    tab_from.str_orgcode = orgData.getOrgcode();
                                    tab_from.str_orgname = orgData.getOrgname();
                                    tab_from.str_farmcode = orgData.getOrgcode();
                                    tab_from.str_farmname = orgData.getOrgname();
                                    tab_from.business_type = orgData.getBusiness_type();
                                    tab_from.bu_code = orgData.getBu_code();
                                    tab_from.bu_name = orgData.getBu_name();
                                    tab_from.bu_type_name = orgData.getBu_type_name();
                                    tab_from.doc_date = getCurrentDate;
                                    tab_from.audit_date = getAuditDate;
                                }

                            });
                            Globalfunction.getInstance(v.getContext()).audit_cancel.setOnClickListener(v1 -> {
                                Globalfunction.getInstance(v1.getContext()).Auditalert.dismiss();
                            });



                        }
                        else{
                            Globalfunction.getInstance(v.getContext()).intent(Integration_submenu.class,v.getContext());
                            Integration_submenu.Integration = "integration";
                            Integration_submenu.orgname = orgData.getOrgname();
                            Integration_submenu.orgcode = orgData.getOrgcode();
                        }



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