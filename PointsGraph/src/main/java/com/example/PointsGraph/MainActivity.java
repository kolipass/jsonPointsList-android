
package com.example.PointsGraph;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.example.PointsGraph.model.Point;
import com.googlecode.androidannotations.annotations.*;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends SherlockFragmentActivity implements ViewPager.OnPageChangeListener,
        TabListener, ParamsFragmentContainer {
    @InstanceState
    int pointsCount;
    @ViewById
    ViewPager pager;

    @AfterViews
    void afterViews() {

    }


    @Background
    void startGettingData(int count) {

    }

    private void setGraph(List<Point> points) {

    }


    @UiThread
    void makeToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeMessage(int resId) {
        makeToast(resId);
    }

    @Override
    public void setUserParam(int count) {
        startGettingData(count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater();
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        Tab tab = getSupportActionBar().getTabAt(position);
        getSupportActionBar().selectTab(tab);
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        int position = tab.getPosition();
        pager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
