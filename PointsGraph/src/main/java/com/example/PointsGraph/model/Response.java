package com.example.PointsGraph.model;

import java.util.List;

public class Response {
    List<Point> points;
    String message;

    public Response() {
    }

    public Response(List<Point> points, String message) {
        this.points = points;
        this.message = message;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
