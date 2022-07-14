package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.ULocale;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_Breeder extends RecyclerView.Adapter<Adapter_Breeder.ViewHolder> {
    Context mContext;
    List<model_breeder> newsList;



    public Adapter_Breeder(List<model_breeder> list, Context context) {
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
        final model_breeder getData = newsList.get(position);
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;
        holder.container.setBackgroundColor(ContextCompat.getColor(mContext,backgroundColor));

        holder.location.setText(getData.getLocation_Code());
        holder.farmorg.setText(getData.getFarm_Org());
        holder.farm.setText(getData.getFarm_Name());
        holder.female.setText(getData.getFemale_Stock());
        holder.male.setText(getData.getMale_Stock());
        holder.total.setText(getData.getStock_Balance());

        holder.counting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString().isEmpty() ? "0" : s.toString();


                boolean updateBreederList = Globalfunction.getInstance(mContext)
                        .updatebreederlist(position,getData.getOrg_Code(),getData.getFarm_code(),count,holder.remark.getText().toString(),"","","");
                if(updateBreederList){
                    Log.d("swine","update breeder details" + position);
                }
                else{
                    Log.d("swine","update breeder details" + position);
                }
            }
        });

        holder.remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean updateBreederList = Globalfunction.getInstance(mContext)
                        .updatebreederlist(position,getData.getOrg_Code(),getData.getFarm_code(),holder.counting.getText().toString(),s.toString(),"","","");

                if(updateBreederList){
                    Log.d("swine","update breeder details" + position);
                }
                else{
                    Log.d("swine","update breeder details" + position);
                }
            }
        });

        boolean save_breeder_details = Globalfunction.getInstance(mContext)
                .ADD_BREEDER_DETAILS(position,
                        getData.getOrg_Code(),
                        getData.getBu_code(),
                        getData.getBu_type(),
                        getData.getLocation_Code(),
                        getData.getFarm_code(),
                        getData.getFarm_Org(),
                        getData.farm_Name,
                        getData.getFemale_Stock().replace(",",""),
                        getData.getMale_Stock().replace(",",""),
                        getData.getStock_Balance().replace(",",""),
                        "0",
                        "",
                        "",
                        "",
                        ""
                );
        if(save_breeder_details){
            Log.d("swine","save breeder details" + position);
        }
        else{
            Log.d("swine","existing breeder details" + position);
        }

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
