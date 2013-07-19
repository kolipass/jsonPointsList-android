
package com.example.PointsGraph;

import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class PointsGraph
        extends SherlockActivity
        implements TabListener {

    @ViewById(R.id.point_count)
    EditText pointCountEditText;
    @InstanceState
    int pointsCount;

    @AfterViews
    void afterViews() {
        if (pointsCount > 0) {
            pointCountEditText.setText(String.valueOf(pointsCount));
        }

    }

    @Click(R.id.start)
    void start() {
        try {
            String inputCount = pointCountEditText.getText().toString();
            if (inputCount.length() == 0) {
                makeToast(R.string.count_points_hint);
            } else {
                int count = Integer.valueOf(inputCount);
                if (count > 0) {
                    startGettingData(count);
                } else {
                    makeToast(R.string.count_points_hint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            makeToast(R.string.incorrect_input);
        }
    }

    @Background
    void startGettingData(int count) {
        pointsCount = count;

        String request = "https://demo.bankplus.ru/mobws/json/pointsList";
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("count", "10"));
        params.add(new BasicNameValuePair("version", "1.1"));

        try {
            String urlParameters = getQuery(params);
            SendRequest sr = new SendRequest(request, true);
            InputStream in = sr.sendRequestGetInputStream(urlParameters);
            if (in != null) {
                makeToast("Order" + " response " + convertStreamToString(in));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String convertStreamToString(InputStream is)
            throws IOException {
        //
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more flatPreviewList to
        // read. We use the StringWriter class to produce the string.
        //
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    @UiThread
    void makeToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
        pointCountEditText.setText(R.string.points_count);
    }

    @UiThread
    void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater();
        return true;
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}
