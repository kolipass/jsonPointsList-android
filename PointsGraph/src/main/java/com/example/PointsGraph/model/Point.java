package com.example.PointsGraph.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
    double x;
    double y;
    protected Point(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
    }

    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public Point() {

    }

    @Override
    public String toString() {
        return
//                "Point{" +
                "(" + x +
                " : " + y +
                ')';
    }
}
