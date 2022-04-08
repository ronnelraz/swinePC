package com.ronnelrazo.physical_counting.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
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

    public Adapter_Checklist(ArrayList<ListItem_Checklist> items) {
        this.items = items;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHMain) {
            modal_checklist_maintopic main = (modal_checklist_maintopic) items.get(position);
            VHMain mainTopic = (VHMain)holder;
            mainTopic.MainTopic.setText(main.getMainTopic());
        } else if(holder instanceof VHSubDetails) {
            modal_checklist_SubDetails subDetails = (modal_checklist_SubDetails) items.get(position);
            VHSubDetails sub = (VHSubDetails)holder;
            sub.sub_details_header.setText(subDetails.getSub_details());

        }
        else if(holder instanceof VHItem) {
            modal_checklist_Details details = (modal_checklist_Details) items.get(position);
            VHItem item = (VHItem)holder;
            item.itemDetails.setText(details.getDetails());


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
        public VHMain(View itemView) {
            super(itemView);
            this.MainTopic = itemView.findViewById(R.id.maintopic);
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
        public VHItem(View itemView) {
            super(itemView);
            this.itemDetails = itemView.findViewById(R.id.itemDetails);
            this.itemGroup = itemView.findViewById(R.id.item_group_button);
            this.item_remarks = itemView.findViewById(R.id.item_remarks);

        }
    }
}