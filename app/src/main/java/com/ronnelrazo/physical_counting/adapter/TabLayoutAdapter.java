package com.ronnelrazo.physical_counting.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ronnelrazo.physical_counting.fragment.Tab_breeder;
import com.ronnelrazo.physical_counting.fragment.Tab_checklist;
import com.ronnelrazo.physical_counting.fragment.Tab_contract;
import com.ronnelrazo.physical_counting.fragment.Tab_feed;
import com.ronnelrazo.physical_counting.fragment.Tab_med;

public class TabLayoutAdapter extends FragmentPagerAdapter {


    Context mContext;
    int mTotalTabs;

    public TabLayoutAdapter(Context context , FragmentManager fragmentManager , int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                return new Tab_contract();
            case 0:
                return new Tab_checklist();
            case 1:
                return new Tab_breeder();
            case 2:
                return new Tab_feed();
            case 3:
                return new Tab_med();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}