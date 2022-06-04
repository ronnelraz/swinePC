package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;
import com.ronnelrazo.physical_counting.sharedPref.SharedPref;
import com.shuhart.stepview.StepView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Farm_setup extends AppCompatActivity {

    private Globalfunction data;
    private SharedPref sharedPref;

    @BindViews({R.id.layout1,R.id.layout2,R.id.layout3,R.id.layout4,R.id.layout5})
    View[] layoutView;
    @BindViews({R.id.previous,R.id.next})
    MaterialButton[] btn_trigger;
    @BindView(R.id.step_view)
    StepView stepView;

    private int currentSelectStepView = 0;
    private int maxSelectedStepView = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_setup);
        ButterKnife.bind(this);
        data = new Globalfunction(this);
        sharedPref = new SharedPref(this);


        step(currentSelectStepView);

        btn_trigger[1].setOnClickListener(v -> {
            if(currentSelectStepView == maxSelectedStepView){
                btn_trigger[1].setText("Save");
            }
            else{
                currentSelectStepView++;
                step(currentSelectStepView);
                Log.d("swine","count " + currentSelectStepView);
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//            layoutView[0].setVisibility(View.GONE);
//            layoutView[1].setVisibility(View.VISIBLE);
            }

        });
    }

    public void back(View view) {
        data.intent(inv_form.class,  this);
        finish();
    }

    private void step(int i){
        stepView.go(i,true);
    }
}