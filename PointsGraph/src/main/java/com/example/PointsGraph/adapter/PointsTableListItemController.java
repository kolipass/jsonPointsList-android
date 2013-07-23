package com.example.PointsGraph.adapter;

import android.view.View;
import android.widget.TextView;
import com.example.PointsGraph.model.Point;

public class PointsTableListItemController {
    protected View container;
    String x;
    String y;

    TextView name;
    TextView count;

    public PointsTableListItemController(View convertView) {
        this.container = convertView;

        name = (TextView) convertView.findViewById(android.R.id.text1);
        count = (TextView) convertView.findViewById(android.R.id.text2);
    }

    public PointsTableListItemController setPoint(Point point) {
        return setPoint(String.valueOf(point.getX()),String.valueOf(point.getY()));
    }

    public PointsTableListItemController setPoint(String x, String y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public View getView() {
        name.setText(x);
        count.setText(y);

        return container;
    }


}
