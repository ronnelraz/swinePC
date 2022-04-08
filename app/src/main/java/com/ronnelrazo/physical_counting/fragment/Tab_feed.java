package com.ronnelrazo.physical_counting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ronnelrazo.physical_counting.R;

import butterknife.ButterKnife;

public class Tab_feed extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feed,parent,false);
        ButterKnife.bind(this,view);

        return view;

    }
}
