package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.adapter.Adapter_menu_list;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.model.modal_menu;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class inv_form extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;



    @BindViews({R.id.username,R.id.role,R.id.org_code,R.id.farm_code})
    TextView[] JWTauth;

    @BindView(R.id.logout)
    MaterialButton logout;

    private String[] getMenuAccessArray;

    @BindView(R.id.gridlayout)
    RecyclerView gridlayout;

    List<modal_menu> menulist = new ArrayList<>();
    RecyclerView.Adapter adapter;

    String[] menu_array = new String[]{
            "TRANSACTION LIST",
            "CREATE",
            "EDIT",
            "CONFIRM",
            "CANCEL",
            "GENERATE PFG",
            "UPLOAD FILE",
            "FARM SETUP",
            "USER SETUP",
            "REPORT"
    };

    int[] menu_icon = new int[]{
            R.drawable.ic_icons8_document,R.drawable.create,
            R.drawable.edit,R.drawable.checked,R.drawable.ic_icons8_cancel,
            R.drawable.icons8_pdf_1,R.drawable.ic_icons8_import_file,
            R.drawable.icons8_smart_home_checked,R.drawable.icons8_add_administrator,
            R.drawable.ic_icons8_report_file
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        getMenuAccessArray = sharedPref.getJwtMenuAccess().split(", ");

        if( sharedPref.getJwtMenuAccess().equals("0")){
            for(int i = 0; i < menu_array.length; i++){
                modal_menu list = new modal_menu(
                        i,
                        menu_icon[i],
                        menu_array[i]
                );
                menulist.add(list);
            }
        }
        else{
            for(int i = 0; i < getMenuAccessArray.length; i++){
                int x =   Integer.parseInt(getMenuAccessArray[i].trim()) - 1;
                modal_menu list = new modal_menu(
                        x,
                        menu_icon[x],
                        menu_array[x]
                );
                menulist.add(list);
            }
        }

//

//



        GridLayoutManager layout_manager = new GridLayoutManager(this, 2);
        layout_manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0 ? 2 : 1);
            }
        });
        adapter = new Adapter_menu_list(menulist,this);
        gridlayout.setLayoutManager(layout_manager);
        gridlayout.setHasFixedSize(true);
        gridlayout.setItemViewCacheSize(999999999);
        gridlayout.setAdapter(adapter);




        JWTauth[0].setText("User : " +sharedPref.getUser());
        JWTauth[1].setText("Role : " +sharedPref.getRole() + " - " + sharedPref.getBU());
        if(sharedPref.getOrg_code().equals("0000")){
            JWTauth[2].setVisibility(View.GONE);
            JWTauth[3].setVisibility(View.GONE);
        }
        else{
            JWTauth[2].setVisibility(View.GONE);
            JWTauth[3].setVisibility(View.GONE);
            JWTauth[2].setText("Org Code : " +sharedPref.getOrg_code());
            JWTauth[3].setText("Farm Code : " +sharedPref.getFarm_code());
        }



        logout.setOnClickListener(v -> {
            data.Confirmation(v.getContext(),"Are you sure you want to sign out your account?",R.drawable.ic_icons8_warning);
            data.positive.setText("Logout");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                data.ConfirmDialog.dismiss();
                sharedPref.signout("false");
                data.intent(Login.class,v1.getContext());
                finish();
            });
        });
    }


}