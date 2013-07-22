package com.example.PointsGraph.task;

import android.content.Context;
import com.example.PointsGraph.manager.AbstractStringResourceManager;
import com.example.PointsGraph.manager.StringResourceManager;
import com.example.PointsGraph.task.decoratedTask.DecoratedTaskAbstract;
import com.example.PointsGraph.task.decoratedTask.RequestPointsTask;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: artem
 * Date: 15.03.13
 * Time: 11:06
 */
public class TaskBuilder {

    public static DecoratedTaskAbstract getRequestPointsTask( Context context,int count, String tag)
            throws UnsupportedEncodingException {
        String request = "https://demo.bankplus.ru/mobws/json/pointsList";
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("count", String.valueOf(count)));
        params.add(new BasicNameValuePair("version", "1.1"));

        return new RequestPointsTask(new StringResourceManager(context), tag, null, request, getQuery(params));
    }

    static private String getQuery(List<BasicNameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (BasicNameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
