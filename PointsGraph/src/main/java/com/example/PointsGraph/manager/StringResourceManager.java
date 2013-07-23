package com.example.PointsGraph.manager;

import android.content.Context;
import com.example.PointsGraph.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kolipass
 * Date: 23.07.13
 * Time: 1:48
 * Information about this garbage is coming soon
 */
public class StringResourceManager extends AbstractStringResourceManager {
    Context context;

    public StringResourceManager(Context context) {
        this.context = context;
    }

    @Override
    public String getKeyManagementError() {
        return context.getString(R.string.key_manager_error);
    }

    @Override
    public String getSendRequestError() {
        return context.getString(R.string.send_request_error);
    }

    @Override
    public String getParseRequestError() {
        return context.getString(R.string.parse_request_error);
    }

    @Override
    public String getServerReturnError() {
        return context.getString(R.string.server_return_error);
    }
}
