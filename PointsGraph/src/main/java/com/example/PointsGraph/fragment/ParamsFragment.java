package com.example.PointsGraph.fragment;

import android.widget.EditText;
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
@EActivity(R.layout.request_params)
public class ParamsFragment extends SherlockFragment {
    @ViewById(R.id.point_count)
    EditText pointCountEditText;
    @InstanceState
    int pointsCount;
    ParamsFragmentContainer container;

    @AfterViews
    void afterViews() {
        if (pointsCount > 0) {
            pointCountEditText.setText(String.valueOf(pointsCount));
        }
    }

    public void setPointsCount(int pointsCount) {
        this.pointsCount = pointsCount;
        if (isAdded()) {
            afterViews();
        }
    }

    public SherlockFragment setContainer(ParamsFragmentContainer container) {
        this.container = container;
        return this;
    }

    @Click(R.id.start)
    void start() {
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

