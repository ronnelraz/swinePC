package com.ronnelrazo.physical_counting.adapter;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
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
import com.ronnelrazo.physical_counting.model.model_feed_edit;
import com.ronnelrazo.physical_counting.model.model_med;
import com.ronnelrazo.physical_counting.model.model_med_edit;
import com.ronnelrazo.physical_counting.tab_from;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_Med_edit extends RecyclerView.Adapter<Adapter_Med_edit.ViewHolder> {
    Context mContext;
    List<model_med_edit> newsList;
    public String audit_no;


    long delay = 3000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();


    public Adapter_Med_edit(List<model_med_edit> list, Context context, String audit_no) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.audit_no = audit_no;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_med_edit getData = newsList.get(position);
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

        holder.counting.setText(getData.getCounting());
        holder.remark.setText(getData.getRemark());

        String getSYSStocks_Q = getData.getStockUnit().equals("Q") ? getData.getStockQty().replace(",","") : "";
        String getSYSStocks_W = getData.getStockUnit().equals("W") ? getData.getStockWgh().replace(",","") : "";

        holder.counting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                updateChanges(s.toString(),holder.remark.getText().toString(),getData.getOrgCode(),audit_no,getData.getFarmOrg(),getData.getProductCode());
                            }
                        }
                    }, delay);
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
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                                updateChanges(holder.counting.getText().toString(), s.toString(), getData.getOrgCode(), audit_no, getData.getFarmOrg(), getData.getProductCode());
                            }
                        }
                    }, delay);
                }
            }
        });




    }

    protected void updateChanges(String counting,String remark,String org_code,String audit_no,String farm_org,String med_code){

        API.getClient().update_med(counting,remark,org_code,audit_no,farm_org,med_code).enqueue(new Callback<Object>() {
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
