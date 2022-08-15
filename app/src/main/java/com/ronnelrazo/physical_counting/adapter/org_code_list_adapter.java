package com.ronnelrazo.physical_counting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.model.modal_org_code;

import java.util.ArrayList;
import java.util.List;

public class org_code_list_adapter extends ArrayAdapter<modal_org_code>{

    List<modal_org_code> list;
    Context context;
    MaterialButton done;


    // View lookup cache
    private static class ViewHolder {
        TextView org_Code;
        TextView name;
        CheckBox item;
    }

    public org_code_list_adapter(List<modal_org_code> data, Context context,MaterialButton done) {
        super(context, R.layout.org_code_list_item, data);
        this.list = data;
        this.context=context;
        this.done = done;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final modal_org_code getData = list.get(position);
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.org_code_list_item, parent, false);

        holder.org_Code = convertView.findViewById(R.id.org_code);
        holder.name = convertView.findViewById(R.id.farmName);
        holder.item = convertView.findViewById(R.id.item);


        holder.org_Code.setText(getData.getOrg_code());
        holder.name.setText(getData.getName());
        holder.item.setText(getData.getOrg_code());
        holder.item.setChecked(getData.isCheck());

        holder.item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(getData.isCheck()){
                getData.setCheck(false);
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

    public List<modal_org_code> getSelected(){
        List<modal_org_code> selected = new ArrayList<>();
        for(modal_org_code list : list){
            if(list.isCheck()){
                selected.add(list);

            }
        }
        return selected;
    }


}
