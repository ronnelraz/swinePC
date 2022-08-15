package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.model.modal_list_group_user;
import com.ronnelrazo.physical_counting.model.modal_org_code;

import java.util.ArrayList;
import java.util.List;

public class Adapter_user_group_list extends ArrayAdapter<modal_list_group_user>{

    List<modal_list_group_user> list;
    Context context;
    MaterialButton remove;


    // View lookup cache
    private static class ViewHolder {
        TextView name;
        CheckBox item;
    }

    public Adapter_user_group_list(List<modal_list_group_user> data, Context context, MaterialButton remove) {
        super(context, R.layout.group_list_items, data);
        this.list = data;
        this.context=context;
        this.remove = remove;

    }


    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final modal_list_group_user getData = list.get(position);
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.group_list_items, parent, false);





        holder.name = convertView.findViewById(R.id.farmname);
        holder.item = convertView.findViewById(R.id.org_code);

        if(getData.getOrg_code().equals("0000")){
            holder.name.setVisibility(View.GONE);
            holder.item.setVisibility(View.GONE);
        }
        else{
            holder.name.setText(getData.getName());
            holder.item.setText(getData.getOrg_code());
        }



        holder.item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(getData.isCheck()){
                getData.setCheck(false);
//                remove.setTextColor(R.color.error);
//                remove.setIconTintResource(R.color.error);

                if(getSelected().size() == 0){
                    remove.setEnabled(false);
                    remove.setTextColor(R.color.unchecked);
                    remove.setIconTintResource(R.color.unchecked);
                }
                else{
                    remove.setEnabled(true);
                    remove.setTextColor(Color.RED);
                    remove.setIconTintResource(R.color.error);
                }

            }
            else{
                getData.setCheck(true);
                remove.setEnabled(true);
                remove.setTextColor(Color.RED);
                remove.setIconTintResource(R.color.error);
            }

        });

        return convertView;
    }

    public List<modal_list_group_user> getSelected(){
        List<modal_list_group_user> selected = new ArrayList<>();
        for(modal_list_group_user list : list){
            if(list.isCheck()){
                selected.add(list);

            }
        }
        return selected;
    }


}
