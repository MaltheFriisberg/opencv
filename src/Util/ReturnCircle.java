package Util;

/**
 * Created by Pyke-Laptop on 19-04-2017.
 */
public class ReturnCircle {
    public ReturnCircle(double x, double y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public ReturnCircle(ReturnCircle returnCircle) {
        this.x = returnCircle.x;
        this.y = returnCircle.y;
        this.radius = returnCircle.radius;
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
