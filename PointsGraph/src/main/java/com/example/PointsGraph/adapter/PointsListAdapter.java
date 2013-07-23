package com.example.PointsGraph.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.example.PointsGraph.R;
import com.example.PointsGraph.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 24.07.13
 * Time: 0:17
 * Адаптер для списка координат
 */
public class PointsListAdapter implements ListAdapter {
    protected Map<Integer, PointsTableListItemController> controllerMap =
            new HashMap<Integer, PointsTableListItemController>();
    private List<Point> points = new ArrayList<Point>();
    private LayoutInflater inflater = null;

    public PointsListAdapter(List<Point> list, Context context) {
        this.points = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return points.size();
    }

    @Override
    public Object getItem(int position) {
        return points.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.table_list_item, parent, false);
        }
        Object o = convertView.getTag();
        if (o == null) {
            o = new PointsTableListItemController(convertView);
            convertView.setTag(o);
        }
        controllerMap.put(position, (PointsTableListItemController) o);
        convertView = ((PointsTableListItemController) o).setPoint((Point) getItem(position)).getView();

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public boolean isEmpty() {
        return points.isEmpty();
    }
}
