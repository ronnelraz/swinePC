package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class inv_form extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindViews({R.id.create,R.id.edit,R.id.pdf,R.id.FarmSetup,R.id.report,R.id.confirm})
    MaterialButton[] menus;

    @BindViews({R.id.username,R.id.role})
    TextView[] JWTauth;

    @BindView(R.id.logout)
    MaterialButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        BounceView.addAnimTo(menus[0]);
        BounceView.addAnimTo(menus[1]);
        BounceView.addAnimTo(menus[2]);
        BounceView.addAnimTo(menus[3]);
        BounceView.addAnimTo(menus[4]);
        BounceView.addAnimTo(menus[5]);

        JWTauth[0].setText("User : " +sharedPref.getUser());
        JWTauth[1].setText("Role : " +sharedPref.getRole() + " - " + sharedPref.getBU());


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

    public void create(View view) {
        data.intent(Farm_categories.class,  this);
    }

    public void pdf(View view) {
        data.intent(Pdf_record_list.class,  this);
    }

    public void FarmSetup(View view) {
        data.intent(Farm_setup.class,  this);
    }

    public void edit(View view) {
        data.intent(Edit_pdf.class,  this);
    }

    public void confirm(View view) {
        data.intent(Confirm.class,view.getContext());
    }

    public void cancel(View view) {
        data.intent(Cancel.class,view.getContext());
    }

    public void upload(View view) {
        data.intent(upload_file.class,view.getContext());
    }
}