package com.example.PointsGraph.task.decoratedTask;


import android.util.Base64;
import com.example.PointsGraph.DataStore;
import com.example.PointsGraph.ServerRequestSender;
import com.example.PointsGraph.manager.AbstractStringResourceManager;
import com.example.PointsGraph.model.Point;
import com.example.PointsGraph.model.ServerResponse;
import com.example.PointsGraph.task.AbstractTask;
import com.example.PointsGraph.task.TaskStatus;

import java.io.*;
import java.util.List;

import static com.example.PointsGraph.task.TaskStatus.*;


public class RequestPointsTask extends DecoratedTaskAbstract {
    String request;
    String urlParameters;

    public RequestPointsTask(AbstractStringResourceManager resourceManager, String tag, AbstractTask preExecutableTask,
                             String request, String urlParameters) {
        super(resourceManager, tag, preExecutableTask);
        this.request = request;
        this.urlParameters = urlParameters;
    }

    @Override
    protected TaskStatus currentHeavyTask() {
        taskStatus.setStatus(STATUS_START);
        taskStatus.setMessage(request);
        publishProgress(taskStatus);

        ServerRequestSender sr = null;
        try {
            sr = new ServerRequestSender(request, true);
        } catch (Exception e) {
            e.printStackTrace();
            taskStatus.setStatus(STATUS_ERROR);
            taskStatus.setMessage(resourceManager.getKeyManagementError());
        }

        if (sr != null) {

            InputStream in = null;
            try {
                in = sr.sendRequest(urlParameters);
            } catch (IOException e) {
                e.printStackTrace();
                taskStatus.setStatus(STATUS_ERROR);
                taskStatus.setMessage(resourceManager.getSendRequestError());
            }
            if (in != null) {
                ServerResponse serverResponse = null;
                try {
                    serverResponse = ServerResponse.getContactsObject(convertStreamToString(in));
                } catch (IOException e) {
                    e.printStackTrace();
                    taskStatus.setStatus(STATUS_ERROR);
                    taskStatus.setMessage(resourceManager.getParseRequestError());
                }
                if (serverResponse != null) {
                    Integer result = serverResponse.getResult();

                    switch (result) {
                        case 0: {
                            taskStatus = successResult(serverResponse);
                            break;
                        }
                        case -1: {
                            taskStatus = serverIsBusy(serverResponse);
                            break;
                        }
                        case -100:
                        default: {
                            taskStatus = incorrectRequestParam(serverResponse);
                            break;
                        }
                    }
                }

            }

        }
        return taskStatus;
    }

    private TaskStatus successResult(ServerResponse serverResponse) {
        List<Point> points = serverResponse.getResponse().getPoints();
        if (points != null && points.size() > 0) {
            DataStore.getInstance().addServerResponse(serverResponse);
            taskStatus.setStatus(STATUS_FINISH);
        } else {
            taskStatus.setStatus(STATUS_ERROR);
            taskStatus.setMessage(resourceManager.getParseRequestError());
            //ToDo Требует дополнительных условий
        }
        return taskStatus;
    }

    private TaskStatus incorrectRequestParam(ServerResponse serverResponse) {
        String message = serverResponse.getResponse().getMessage();

        taskStatus.setStatus(STATUS_ERROR);
        taskStatus.setMessage(resourceManager.getServerReturnError() + decodeMessage(message));

        return taskStatus;
    }

    private TaskStatus serverIsBusy(ServerResponse serverResponse) {
        String message = serverResponse.getResponse().getMessage();

        taskStatus.setStatus(STATUS_ERROR);
        taskStatus.setMessage(resourceManager.getServerReturnError() + decodeMessage(message));

        return taskStatus;
    }

    private String decodeMessage(String message) {


        byte[] decoded = null;
        if (message.lastIndexOf("=") == (message.length() - 1))
        try {
            decoded = Base64.decode(message, Base64.DEFAULT);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return decoded == null ? message : new String(decoded);

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
}