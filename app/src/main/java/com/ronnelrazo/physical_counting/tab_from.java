package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.ronnelrazo.physical_counting.adapter.TabLayoutAdapter;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder;
import com.ronnelrazo.physical_counting.fragment.Tab_feed;
import com.ronnelrazo.physical_counting.fragment.Tab_med;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class tab_from extends AppCompatActivity {


    public static String str_types,str_orgcode,str_orgname,str_farmcode,str_farmname,doc_date,audit_date;
    public static String business_type,bu_code,bu_name,bu_type_name;

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;



    @BindViews({R.id.types,R.id.orgcode,R.id.orgname,R.id.farmcode,R.id.farmname,R.id.docDate,R.id.auditDate})
    TextView[] headerDetails;

    @BindViews({R.id.save,R.id.cancel})
    public MaterialButton[] btn_func;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_from);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);

        headerDetails[0].setText(str_types);
        headerDetails[1].setText(str_orgcode);
        headerDetails[2].setText(str_orgname);
        headerDetails[3].setText(str_farmcode);
        headerDetails[4].setText(str_farmname);
        headerDetails[5].setText(doc_date);
        headerDetails[6].setText(audit_date);



        Log.d("swine",business_type);
        Log.d("swine",bu_code);
        Log.d("swine",bu_name);
        Log.d("Swine",bu_type_name);




        //SAVE HEADER
        boolean setChecklistHeader =  data.ADD_CHECKLIST_HEADER(str_orgcode, str_farmcode,str_orgname,str_farmname,doc_date,audit_date,str_types,business_type,bu_code,bu_name,bu_type_name);
        if(setChecklistHeader){
            Log.d("swine","save header");
        }
        else{
            Log.d("swine","error header");
        }


        //breeder
        Tab_breeder.orgCode = str_orgcode;
        Tab_breeder.farmOrg = str_farmcode;
        //feed
        Tab_feed.orgCode = str_orgcode;
        Tab_feed.farmOrg = str_farmcode;

        //Med
        Tab_med.orgCode = str_orgcode;
        Tab_med.farmOrg = str_farmcode;


        tabs.addTab(tabs.newTab().setText("Contact"));
        tabs.addTab(tabs.newTab().setText("Checklist"));
        tabs.addTab(tabs.newTab().setText("Breeder"));
        tabs.addTab(tabs.newTab().setText("Feed"));
        tabs.addTab(tabs.newTab().setText("Med"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter adapter = new TabLayoutAdapter(this,getSupportFragmentManager(),tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btn_func[0].setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "save", Toast.LENGTH_SHORT).show();

        });

        btn_func[1].setOnClickListener(v -> {
            data.Confirmation(v.getContext(),"Are you sure you want to cancel " + str_farmname + "?",R.drawable.ic_icons8_warning);
            data.positive.setText("confirm");
            data.negative.setOnClickListener(v1 ->{
                data.ConfirmDialog.dismiss();
            });
            data.positive.setOnClickListener(v1 -> {
                boolean result = data.clearAll(str_orgcode,str_farmcode);
                if(result){
                    Log.d("swine", "cancel transaction : " + result);
                    data.ConfirmDialog.dismiss();
                    data.intent(Farm_categories.class,v1.getContext());
                    finish();
                }

            });
        });


    }
}