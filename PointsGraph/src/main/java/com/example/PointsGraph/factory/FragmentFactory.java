package com.example.PointsGraph.factory;

import android.support.v4.app.Fragment;
import com.example.PointsGraph.fragment.GraphFragment;
import com.example.PointsGraph.fragment.ParamsFragment;
import com.example.PointsGraph.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Подобие фабрики, порождающей и хранящей фрагменты графика и параметров.
 */

public class FragmentFactory {
    List<Fragment> fragmentList = new ArrayList<Fragment>();

    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public int getCount() {
        return fragmentList.size();
    }

    public FragmentFactory setParamsFragment(int count) {
        if (fragmentList == null || fragmentList.size() == 0) {
            fragmentList = new ArrayList<Fragment>();

        } else {
            ((ParamsFragment) fragmentList.get(0)).setPointsCount(count);
        }
        return this;
    }

    public FragmentFactory setGraphFragment(List<Point> points) throws InstantiationException {
        if (fragmentList == null || fragmentList.size() == 0) {
            throw new InstantiationException("Не был инициализирован setParamsFragment()");
        }

        if (fragmentList.size() == 1) {
            fragmentList.add(new GraphFragment());
        }

        ((GraphFragment) fragmentList.get(1)).setPoints(points);

        return this;
    }
}
