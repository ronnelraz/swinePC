package com.ronnelrazo.physical_counting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.ronnelrazo.physical_counting.globalfunc.Globalfunction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Globalfunction globalfunction;
    @BindView(R.id.status)
    TextView str_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        globalfunction = new Globalfunction(this);


        //networking checker
        globalfunction.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        globalfunction.registerBind(str_status);
        globalfunction.isConnected(str_status);
        globalfunction.isDisconnected(str_status);

        globalfunction.merlin.registerConnectable(() -> {
            new Handler().postDelayed(() -> {
//                globalfunction.intent(Login.class,this); //intent
//                finish();
                Intent i = new Intent(getApplicationContext(), Login.class);
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,(findViewById(R.id.logo)),"logo");
                startActivity(i,option.toBundle());
                finish();
            },3000);
        });




    }


    @Override
    protected void onResume() {
        super.onResume();
        globalfunction.merlin.bind();
    }

    @Override
    protected void onPause() {
        globalfunction.merlin.unbind();
        super.onPause();
    }

}