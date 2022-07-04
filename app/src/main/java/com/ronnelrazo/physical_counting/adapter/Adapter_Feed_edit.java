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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_feed;
import com.ronnelrazo.physical_counting.model.model_feed_edit;
import com.ronnelrazo.physical_counting.tab_from;

import java.util.List;

public class Adapter_Feed_edit extends RecyclerView.Adapter<Adapter_Feed_edit.ViewHolder> {
    Context mContext;
    List<model_feed_edit> newsList;



    public Adapter_Feed_edit(List<model_feed_edit> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_feed_edit getData = newsList.get(position);
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;
        holder.container.setBackgroundColor(ContextCompat.getColor(mContext,backgroundColor));

        holder.farmorg.setText(getData.getFarmOrg());
        holder.productCode.setText(getData.getProductCode());
        holder.productName.setText(getData.getProductName());
        holder.stockUnit.setText(getData.getStockUnit());
        String getStrock = getData.getStockUnit().equals("Q") ? getData.getStockQty() : getData.getStockWgh();
        holder.stock.setText(getStrock);

        if(getData.getStockUnit().equals("Q")){
            holder.counting.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            holder.counting.setInputType(InputType.TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        String getSYSStocks_Q = getData.getStockUnit().equals("Q") ? getData.getStockQty().replace(",","") : "";
        String getSYSStocks_W = getData.getStockUnit().equals("W") ? getData.getStockWgh().replace(",","") : "";
        holder.counting.setText(getData.getCounting());
        holder.remark.setText(getData.getRemark());





    }



    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView farmorg,productCode,productName,stockUnit,stock;
        public EditText counting,remark;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
                farmorg = itemView.findViewById(R.id.farmorg);
                productCode = itemView.findViewById(R.id.productCode);
                productName = itemView.findViewById(R.id.productName);
                stockUnit = itemView.findViewById(R.id.stockUnit);
                stock = itemView.findViewById(R.id.stock);
                counting = itemView.findViewById(R.id.counting);
                remark = itemView.findViewById(R.id.remark);
                container = itemView.findViewById(R.id.container);

        }
    }
}
