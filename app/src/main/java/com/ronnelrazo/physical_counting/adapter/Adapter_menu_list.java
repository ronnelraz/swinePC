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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.Cancel;
import com.ronnelrazo.physical_counting.Confirm;
import com.ronnelrazo.physical_counting.Edit_pdf;
import com.ronnelrazo.physical_counting.Farm_categories;
import com.ronnelrazo.physical_counting.Farm_setup;
import com.ronnelrazo.physical_counting.Pdf_record_list;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_menu;
import com.ronnelrazo.physical_counting.model.model_breeder;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.ronnelrazo.physical_counting.transaction;
import com.ronnelrazo.physical_counting.upload_file;
import com.ronnelrazo.physical_counting.user_setup;

import java.util.List;

public class Adapter_menu_list extends RecyclerView.Adapter<Adapter_menu_list.ViewHolder> {
    Context mContext;
    List<modal_menu> newsList;

    private String[] getMenuAccessArray;

    public Adapter_menu_list(List<modal_menu> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_menu getData = newsList.get(position);
        BounceView.addAnimTo(holder.menu);


        holder.menu.setText(getData.getTitle());
        holder.menu.setIconResource(getData.getIcon());

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getData.getPosition() == 0){
                    Globalfunction.getInstance(v.getContext()).intent(transaction.class,v.getContext());
                }
                else if(getData.getPosition() == 1){
                    Globalfunction.getInstance(v.getContext()).intent(Farm_categories.class,v.getContext());
                }
                else if(getData.getPosition() == 2){
                    Globalfunction.getInstance(v.getContext()).intent(Edit_pdf.class,v.getContext());
                }
                else if(getData.getPosition() == 3){
                    Globalfunction.getInstance(v.getContext()).intent(Confirm.class,v.getContext());
                }
                else if(getData.getPosition() == 4){
                    Globalfunction.getInstance(v.getContext()).intent(Cancel.class,v.getContext());
                }
                else if(getData.getPosition() == 5){
                    Globalfunction.getInstance(v.getContext()).intent(Pdf_record_list.class,v.getContext());
                }
                else if(getData.getPosition() == 6){
                    Globalfunction.getInstance(v.getContext()).intent(upload_file.class,v.getContext());
                }
                else if(getData.getPosition() == 7){
                    Globalfunction.getInstance(v.getContext()).intent(Farm_setup.class,v.getContext());
                }
                else if(getData.getPosition() == 8){
                    Globalfunction.getInstance(v.getContext()).intent(user_setup.class,v.getContext());
                }
                else if(getData.getPosition() == 9){
//                    Globalfunction.getInstance(v.getContext()).intent(report.class,v.getContext());
                    Toast.makeText(v.getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }





    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialButton menu;

        public ViewHolder(View itemView) {
            super(itemView);
             menu = itemView.findViewById(R.id.menu_item);


        }
    }
}
