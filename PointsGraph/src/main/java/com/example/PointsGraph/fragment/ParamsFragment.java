package com.example.PointsGraph.fragment;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.PointsGraph.ParamsFragmentContainer;
import com.example.PointsGraph.R;
import com.googlecode.androidannotations.annotations.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 22.07.13
 * Time: 14:50
 * Фрагмент контролирует view параметров запроса
 */
@EFragment(R.layout.request_params)
public class ParamsFragment extends SherlockFragment {
    @ViewById(R.id.point_count)
    EditText pointCountEditText;
    @InstanceState
    int pointsCount;
    ParamsFragmentContainer container;

    @AfterViews
    void afterViews() {
        if (container==null){
            container = (ParamsFragmentContainer) getActivity();
        }
        if (pointsCount > 0) {
            pointCountEditText.setText(String.valueOf(pointsCount));
        }
        pointCountEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo. IME_ACTION_DONE) {
                    start();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public ParamsFragment setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
        if (isAdded()) {
            afterViews();
        }     return this;
    }

    public ParamsFragment setContainer(ParamsFragmentContainer container) {
        this.container = container;
        return this;
    }

    @Click(R.id.start)
    void start() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        try {
            String inputCount = pointCountEditText.getText().toString();
            if (inputCount.length() == 0) {
                container.makeMessage(R.string.count_points_hint);
            } else {
                int count = Integer.valueOf(inputCount);
                if (count > 0) {
                    container.setUserParam(count);
                } else {
                    container.makeMessage(R.string.count_points_hint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            container.makeMessage(R.string.incorrect_input);
        }
    }
}

