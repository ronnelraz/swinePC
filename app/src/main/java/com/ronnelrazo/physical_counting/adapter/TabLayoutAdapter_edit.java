package com.ronnelrazo.physical_counting.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ronnelrazo.physical_counting.fragment.Tab_breeder;
import com.ronnelrazo.physical_counting.fragment.Tab_breeder_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_contract;
import com.ronnelrazo.physical_counting.fragment.Tab_feed;
import com.ronnelrazo.physical_counting.fragment.Tab_feed_edit;
import com.ronnelrazo.physical_counting.fragment.Tab_med;
import com.ronnelrazo.physical_counting.fragment.Tab_med_edit;

public class TabLayoutAdapter_edit extends FragmentPagerAdapter {


    Context mContext;
    int mTotalTabs;

    public TabLayoutAdapter_edit(Context context , FragmentManager fragmentManager , int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tab_contract();

            case 1:
                return new Tab_checklist_edit();
            case 2:
                return new Tab_breeder_edit();
            case 3:
                return new Tab_feed_edit();
            case 4:
                return new Tab_med_edit();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}