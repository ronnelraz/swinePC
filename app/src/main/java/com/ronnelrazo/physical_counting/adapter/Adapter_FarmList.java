package com.ronnelrazo.physical_counting.adapter;


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
import com.ronnelrazo.physical_counting.model.model_Farmlist;
import com.ronnelrazo.physical_counting.model.model_farm;
import com.ronnelrazo.physical_counting.model.model_header_farm_org;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.ArrayList;

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
                Globalfunction.getInstance(v.getContext()).intent(tab_from.class,v.getContext());
                tab_from.str_types = "Integration";
                Tab_checklist.str_bu_Type = "SWINT_1";
                tab_from.str_orgcode = orgData.getOrgcode();
                tab_from.str_orgname = orgData.getOrgname();
                tab_from.str_farmcode = orgData.getFarmcode();
                tab_from.str_farmname = orgData.getFarmname();

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