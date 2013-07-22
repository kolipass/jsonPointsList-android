
package com.example.PointsGraph.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.PointsGraph.factory.FragmentFactory;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    FragmentFactory fragmentFactory;

    private String[] locations;

    public ViewPagerAdapter(FragmentManager fm, FragmentFactory fragmentFactory) {
        super(fm);
        this.fragmentFactory = fragmentFactory;
    }

    public int getCount() {
        return fragmentFactory.getCount();
    }

    public Fragment getItem(int position) {
        return fragmentFactory.getItem(position);
    }

    public FragmentFactory getFragmentFactory() {
        return fragmentFactory;
    }

    public void setFragmentFactory(FragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
    }
}

