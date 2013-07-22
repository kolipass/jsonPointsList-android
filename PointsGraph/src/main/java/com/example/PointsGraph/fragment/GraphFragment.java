package com.example.PointsGraph.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import com.actionbarsherlock.app.SherlockListFragment;
import com.example.PointsGraph.R;
import com.example.PointsGraph.model.Point;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 22.07.13
 * Time: 21:32
 * Фрагметн контролирует экран с графиком и таблицей координат
 */
@EFragment(R.layout.graph_fragment)
public class GraphFragment extends SherlockListFragment {
    @ViewById(R.id.graph)
    FrameLayout graphLayout;
    List<Point> points;
    Context context;
    Comparator<Point> comparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(p1.getX(), p2.getX());
        }
    };

    @AfterViews
    void afterViews() {
        context = getActivity();
        if (points != null && points.size() > 0) {
            setAdapter(points);
            setGraph(points);
        }
    }

    void setAdapter(List<Point> points) {
        ArrayAdapter<Point> adapter = new ArrayAdapter<Point>(context, android.R.layout.simple_list_item_1, points);
        setListAdapter(adapter);
    }

    void setGraph(List<Point> points) {
        graphLayout.removeAllViews();
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            data[i] = new GraphView.GraphViewData(point.getX(), point.getY());
        }

        String lineTitle = context.getString(R.string.line_title);
        GraphView graphView = new LineGraphView(context, lineTitle);

        graphView.addSeries(new GraphViewSeries(data));
        // set view port, start=2, size=40
        graphView.setViewPort(2, 40);
        graphView.setScrollable(true);
        graphView.setScalable(true);

        graphLayout.addView(graphView);
    }

    public void setComparator(Comparator<Point> comparator) {
        this.comparator = comparator;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Fragment setPoints(List<Point> points) {
        Collections.sort(points, comparator);
        this.points = points;
        return this;
    }
}
