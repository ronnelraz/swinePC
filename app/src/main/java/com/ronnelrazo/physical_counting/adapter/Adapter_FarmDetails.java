package com.ronnelrazo.physical_counting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ronnelrazo.physical_counting.Farm_setup;
import com.ronnelrazo.physical_counting.Login;
import com.ronnelrazo.physical_counting.MainActivity;
import com.ronnelrazo.physical_counting.R;
import com.ronnelrazo.physical_counting.model.modal_Farm_setup_details;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.List;
import java.util.Locale;

public class Adapter_FarmDetails extends RecyclerView.Adapter<Adapter_FarmDetails.ViewHolder> {
    Context mContext;
    List<modal_Farm_setup_details> newsList;
    boolean modify;


    public Adapter_FarmDetails(List<modal_Farm_setup_details> list, Context context, boolean modify) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.modify = modify;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farm_list_detials,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final modal_Farm_setup_details getData = newsList.get(position);
        int backgroundColor = position%2==0 ? R.color.even : R.color.odd;
        holder.card.setBackgroundColor(ContextCompat.getColor(mContext,backgroundColor));
        holder.org_code.setText(getData.getORG_CODE());
        holder.org_name.setText(getData.getORG_NAME());
        holder.address.setText(getData.getADDRESS());

        holder.card.setOnClickListener(v -> {
            ((Farm_setup)v.getContext()).alertDialog.dismiss();
            Farm_setup.item_selected_org_code = getData.getORG_CODE();
            ((Farm_setup)v.getContext()).ORG_CODE_ID = getData.ORG_CODE;

            SearchableSpinner[] business_area = ((Farm_setup) v.getContext()).business_Area;

            int position_org_Code = ((Farm_setup) v.getContext()).Org_code_list.indexOf(getData.getORG_CODE() + " - " + getData.getORG_NAME());
            String demo =   ((Farm_setup) v.getContext()).Org_code_list.get(position_org_Code);
//            Toast.makeText(mContext, demo, Toast.LENGTH_SHORT).show();
            business_area[0].onSearchableItemClicked(demo,position_org_Code);
            business_area[0].setSelection(position_org_Code);

            new Handler().postDelayed(() -> {
             try {
                 String getORgName =  ((Farm_setup) v.getContext()).Org_name_list.get(0);
                 business_area[1].onSearchableItemClicked(getORgName,0);
                 business_area[1].setSelection(0);
             }catch(Exception e){
                 Log.d("Swine",e.getLocalizedMessage() + " Adapter Farm Details");
             }
            },3500);

            //form
            CheckBox[] formChecklist = ((Farm_setup)v.getContext()).FormArea;
            boolean checklist = getData.getCHECKLIST_AUDIT_FLAG().equals("Y");
            boolean breeder = getData.getBREEDER_AUDIT_FLAG().equals("Y");
            boolean feed = getData.getFEED_AUDIT_FLAG().equals("Y");
            boolean med = getData.getMED_AUDIT_FLAG().equals("Y");

            formChecklist[0].setChecked(checklist);
            formChecklist[1].setChecked(breeder);
            formChecklist[2].setChecked(feed);
            formChecklist[3].setChecked(med);


            SearchableSpinner[] address_area = ((Farm_setup) v.getContext()).address_Area;
            String[] address = getData.getADDRESS().split(", ");

            new Handler().postDelayed(() -> {
                try{
                    int position_province_name = ((Farm_setup) v.getContext()).province_name.indexOf(address[2].trim().toUpperCase(Locale.ROOT));
                    String position_province = ((Farm_setup) v.getContext()).province_name.get(position_province_name);
                    address_area[0].onSearchableItemClicked(position_province,position_province_name);
                    address_area[0].setSelection(position_province_name);
                }catch (Exception e){
                    Toast.makeText(v.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            },3000);

            new Handler().postDelayed(() -> {
                try{
                    int position_municipal_name = ((Farm_setup) v.getContext()).municipal_name.indexOf(address[1].trim().toUpperCase(Locale.ROOT));
                    address_area[1].onSearchableItemClicked(address[1].trim(),position_municipal_name);
                    address_area[1].setSelection(position_municipal_name);
                }catch (Exception e){
                    Toast.makeText(v.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            },3000);
            new Handler().postDelayed(() -> {
                try{
                    int position_barangay_name = ((Farm_setup) v.getContext()).barangay_name.indexOf(address[0].trim());
                    address_area[2].onSearchableItemClicked(address[0].trim(),position_barangay_name);
                    address_area[2].setSelection(position_barangay_name);
                }catch (Exception e){
                    Toast.makeText(v.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            },3000);




            EditText[] address_Area_edit = ((Farm_setup)v.getContext()).address_Area_edit;
            address_Area_edit[0].setText(getData.getZIP_CODE());
            address_Area_edit[1].setText(getData.getSTR_LAT());
            address_Area_edit[2].setText(getData.getSTR_LONG());


            EditText[] farmManager = ((Farm_setup)v.getContext()).farmManager;
            farmManager[0].setText(getData.getFARM_MANAGER_CODE());
            farmManager[1].setText(getData.getFARM_MANAGER_NAME());
            farmManager[2].setText(getData.getFARM_MANAGER_CONTACT_NO());
            farmManager[3].setText(getData.getFARM_MANAGER_EMAIL());

            EditText[] clerkManager = ((Farm_setup)v.getContext()).clerkManager;
            clerkManager[0].setText(getData.getFARM_CLERK_CODE());
            clerkManager[1].setText(getData.getFARM_CLERK_NAME());
            clerkManager[2].setText(getData.getFARM_CLERK_CONTACT_NO());
            clerkManager[3].setText(getData.getFARM_CLERK_EMAIL());


        });

    }



    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView org_code,org_name,address;
        public CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            org_code = itemView.findViewById(R.id.org_code);
            org_name = itemView.findViewById(R.id.org_name);
            address = itemView.findViewById(R.id.address);
            card = itemView.findViewById(R.id.card);

        }
    }
}
