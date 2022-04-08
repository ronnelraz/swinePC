package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;

import butterknife.ButterKnife;

public class inv_form extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_form);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);
    }

    public void create(View view) {
        data.intent(Farm_categories.class,  this);
    }
}