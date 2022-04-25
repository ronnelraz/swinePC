package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class inv_form extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindViews({R.id.create,R.id.edit})
    MaterialButton[] menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
        BounceView.addAnimTo(menus[0]);
        BounceView.addAnimTo(menus[1]);
    }

    public void create(View view) {
        data.intent(Farm_categories.class,  this);
    }
}