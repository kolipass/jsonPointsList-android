
package com.example.PointsGraph;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.example.PointsGraph.adapter.ViewPagerAdapter;
import com.example.PointsGraph.factory.FragmentFactory;
import com.example.PointsGraph.manager.TaskManager;
import com.example.PointsGraph.model.Point;
import com.example.PointsGraph.task.AbstractTask;
import com.example.PointsGraph.task.TaskBuilder;
import com.example.PointsGraph.task.TaskStatus;
import com.googlecode.androidannotations.annotations.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@EActivity(R.layout.activity_main)
public class MainActivity extends SherlockFragmentActivity implements ViewPager.OnPageChangeListener,
        TabListener, ParamsFragmentContainer, Observer {
    private final static String TASK_TAG = "server_request";
    @InstanceState
    int pointsCount;
    @ViewById
    ViewPager pager;
    @InstanceState
    ArrayList<Point> points = new ArrayList<Point>();
    ViewPagerAdapter viewPagerAdapter;
    private ProgressDialog mProgressDialog;

    @AfterViews
    void afterViews() {
        configureViewPager();

        TaskManager taskManager = TaskManager.getInstance();
        AbstractTask task = taskManager.getTask(TASK_TAG);
        if (task != null && task.isCurrentlyStarted()) {
            taskManager.addObserver(this);
            showGettingData();
        }

        if (!points.isEmpty()) {
            setGraph(points);
        }
    }

    private void configureViewPager() {
        FragmentFactory fragmentFactory = new FragmentFactory();
        fragmentFactory.setParamsFragment(pointsCount, this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentFactory);
        pager.setAdapter(viewPagerAdapter);
        pager.setOnPageChangeListener(this);
    }

    private void configureViewPager(List<Point> points) throws InstantiationException {
        if (viewPagerAdapter == null) {
            configureViewPager();
        }
        FragmentFactory factory = viewPagerAdapter.getFragmentFactory().setGraphFragment(points);
        viewPagerAdapter.setFragmentFactory(factory);
        pager.setCurrentItem(1);
    }

    private void configureActionBar() {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab paramTab = getSupportActionBar().newTab().setText(R.string.paramTab).setTabListener(this);
        Tab graphTab = getSupportActionBar().newTab().setText(R.string.paramResult).setTabListener(this);

        getSupportActionBar().removeAllTabs();

        getSupportActionBar().addTab(paramTab);
        getSupportActionBar().addTab(graphTab);
    }

    private void startGettingData(int count) {
        if (isNetworkAvailable()) {
            createTask(count);
            showGettingData();
        } else {
            makeToast(R.string.alert_dialog_internet_problem);
        }
    }

    private void showGettingData() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.getting_data);
        mProgressDialog.setMessage(getString(R.string.getting_data_from_server));
        mProgressDialog.show();
    }

    private void createTask(int count) {
        try {
            TaskManager taskManager = TaskManager.getInstance();
            taskManager.addObserver(this);
            taskManager.addTask(TASK_TAG, TaskBuilder.getRequestPointsTask(this, count, TASK_TAG));
            taskManager.start(TASK_TAG);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @UiThread
    void setGraph(List<Point> points) {
        try {
            configureActionBar();
            configureViewPager(points);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
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
        this.pointsCount = count;
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

    /**
     * Кастыль для корректной работы фрагментов в адаптере
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
//        super.onSaveInstanceState(outState);
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

    @Override
    public void update(Observable o, Object taskStatus) {
        if (o != null && o instanceof TaskManager) {
            if (taskStatus instanceof TaskStatus) {
                TaskStatus status = (TaskStatus) taskStatus;
                if (status.getKey().equals(TASK_TAG)) {
                    switch (status.getStatus()) {
                        case TaskStatus.STATUS_FINISH: {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }

                            this.points = new ArrayList<Point>(DataStore.getInstance().peekServerResponse().
                                    getResponse().getPoints());
                            setGraph(this.points);
                            break;
                        }
                        case TaskStatus.STATUS_ERROR: {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }

                            makeToast("Ошибка: " + status.getMessage());
                            break;
                        }
                    }
                }
            }
        }
    }
}
