package com.ronnelrazo.physical_counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.model.modal_menu_access;
import com.ronnelrazo.physical_counting.model.modal_org_code;

import java.util.ArrayList;
import java.util.List;

public class menu_access_adapter extends ArrayAdapter<modal_menu_access>{

    List<modal_menu_access> list;
    Context context;
    MaterialButton done;
    CheckBox all;



    // View lookup cache
    private static class ViewHolder {
        CheckBox item;
    }

    public menu_access_adapter(List<modal_menu_access> data, Context context, MaterialButton done,CheckBox all) {
        super(context, R.layout.menu_list_item, data);
        this.list = data;
        this.context=context;
        this.done = done;
        this.all = all;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final modal_menu_access getData = list.get(position);
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.menu_list_item, parent, false);

        holder.item = convertView.findViewById(R.id.item);

        holder.item.setText(getData.getMene());
        holder.item.setChecked(getData.isCheck());

        holder.item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(getData.isCheck()){
                getData.setCheck(false);
//                all.setChecked(false);
                if(getSelected().size() == 0){
                    done.setEnabled(false);
                }
                else{
                    done.setEnabled(true);
                }

            }
            else{
                getData.setCheck(true);
                done.setEnabled(true);
            }

        });
        
        return convertView;
    }

    public List<modal_menu_access> getSelected(){
        List<modal_menu_access> selected = new ArrayList<>();
        for(modal_menu_access list : list){
            if(list.isCheck()){
                selected.add(list);

            }

        }
        return selected;
    }


}
