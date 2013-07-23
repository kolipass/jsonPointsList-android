package com.example.PointsGraph.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.PointsGraph.manager.GalleryFileManager;
import com.example.PointsGraph.R;
import com.example.PointsGraph.adapter.PointsListAdapter;
import com.example.PointsGraph.adapter.PointsTableListItemController;
import com.example.PointsGraph.model.Point;
import com.googlecode.androidannotations.annotations.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;
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
public class GraphFragment extends SherlockFragment {
    @ViewById(R.id.graph)
    FrameLayout graphLayout;
    @ViewById(android.R.id.list)
    ListView listView;
    @InstanceState
    ArrayList<Point> points;
    Context context;
    Comparator<Point> comparator = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(p1.getX(), p2.getX());
        }
    };
    GraphView graphView;

    @AfterViews
    void afterViews() {
        context = getActivity();
        if (points != null && points.size() > 0) {
            setGraph(points);
            setAdapter(points);
        }
    }

    void setAdapter(List<Point> points) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PointsTableListItemController controller =
                new PointsTableListItemController(inflater.inflate(R.layout.table_list_item, null)).setPoint("X", "Y");
        listView.addHeaderView(controller.getView(), null, false);
        listView.setAdapter(new PointsListAdapter(points, context));
    }

    void setGraph(List<Point> points) {
        graphLayout.removeAllViews();
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            data[i] = new GraphView.GraphViewData(point.getX(), point.getY());
        }

        String lineTitle = context.getString(R.string.line_title);
        graphView = new LineGraphView(context, lineTitle);

        graphView.addSeries(new GraphViewSeries(data));
        // set view port, start=2, size=40
        graphView.setViewPort(2, 40);
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.setDrawingCacheEnabled(true);

        graphLayout.addView(graphView);
    }

    @Click(R.id.save_to_gallary)
    void onLongClick() {
        if (graphView != null) {
            String fileName = new GalleryFileManager(context).savePNG(graphView.getDrawingCache());
            if (fileName != null) {
                Toast.makeText(context, "успех: " + fileName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "неудача", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setComparator(Comparator<Point> comparator) {
        this.comparator = comparator;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Fragment setPoints(List<Point> points) {
        Collections.sort(points, comparator);
        this.points = points instanceof ArrayList ? (ArrayList<Point>) points : new ArrayList<Point>(points);
        return this;
    }
}