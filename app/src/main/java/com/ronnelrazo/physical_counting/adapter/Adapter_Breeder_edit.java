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
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_breeder_edit;

import java.util.List;

public class Adapter_Breeder_edit extends RecyclerView.Adapter<Adapter_Breeder_edit.ViewHolder> {
    Context mContext;
    List<model_breeder_edit> newsList;



    public Adapter_Breeder_edit(List<model_breeder_edit> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_breeder_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_breeder_edit getData = newsList.get(position);
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;
        holder.container.setBackgroundColor(ContextCompat.getColor(mContext,backgroundColor));

        holder.location.setText(getData.getLocation_Code());
        holder.farmorg.setText(getData.getFarm_Org());
        holder.farm.setText(getData.getFarm_Name());
        holder.female.setText(getData.getFemale_Stock());
        holder.male.setText(getData.getMale_Stock());
        holder.total.setText(getData.getStock_Balance());

        holder.counting.setText(getData.getCounting());
        holder.remark.setText(getData.getRemark());


    }





    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView location,farmorg,farm,female,male,total;
        public EditText counting,remark;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
             location = itemView.findViewById(R.id.location);
                farmorg = itemView.findViewById(R.id.farmorg);
                farm = itemView.findViewById(R.id.farm);
                female = itemView.findViewById(R.id.female);
                male = itemView.findViewById(R.id.male);
                total = itemView.findViewById(R.id.total);
                counting = itemView.findViewById(R.id.counting);
                remark = itemView.findViewById(R.id.remark);
                container = itemView.findViewById(R.id.container);

        }
    }
}
