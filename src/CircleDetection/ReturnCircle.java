package Util;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

/**
 * Created by Pyke-Laptop on 19-04-2017.
 */
public class ReturnCircle {
    Mat image;

    public Mat getImage() {
        return image;
    }

    public ReturnCircle(double x, double y, int radius, Mat image) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.image = image;

    }

    public ReturnCircle(ReturnCircle returnCircle, Mat circle) {
        this.x = returnCircle.x;
        this.y = returnCircle.y;
        this.radius = returnCircle.radius;
        this.image = circle;
    }

    private double x;
    private double y;
    private int radius;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }


}
