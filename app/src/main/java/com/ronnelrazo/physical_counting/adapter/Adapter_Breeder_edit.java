package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.connection.API;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.model.model_breeder_edit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_Breeder_edit extends RecyclerView.Adapter<Adapter_Breeder_edit.ViewHolder> {
    Context mContext;
    List<model_breeder_edit> newsList;
    String audit_no;
    String farm_code;

    long delay = 3000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();




    public Adapter_Breeder_edit(List<model_breeder_edit> list, Context context,String audit_no,String farm_code) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.audit_no = audit_no;
        this.farm_code = farm_code;


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

        holder.counting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String getCount = editable.toString();
                String getRemark = holder.remark.getText().toString();

                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                updateChanges(getCount,getRemark,getData.getOrg_Code(),audit_no,getData.getFarm_Org());
                            }
                        }
                    }, delay);
                }
            }
        });

        holder.remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String getCount = holder.counting.getText().toString();
                String getRemark = editable.toString();

                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                updateChanges(getCount,getRemark,getData.getOrg_Code(),audit_no,getData.getFarm_Org());
                            }
                        }
                    }, delay);
                }
            }
        });


    }



    protected void updateChanges(String counting,String remark,String org_code,String audit_no,String farm_org){

        API.getClient().update_breeder_idana(counting,remark,org_code,audit_no,farm_org).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    String data = jsonResponse.getString("data");
                    if(success){

                        Log.d("swine",data);
                    }
                    else{
                        Globalfunction.getInstance(mContext).toast(R.raw.error,"Invalid Params", Gravity.TOP|Gravity.CENTER,0,50); //50
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(mContext, "Sorry Farm Details not yet setup, Please setup up first to complete the information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
