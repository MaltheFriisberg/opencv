package CircleDetection;

import Util.ReturnCircle;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

import Util.ImageViewer;
import static Util.ImageConverter.bufferedImageToMat;
import static Util.ImageConverter.matToBufferedImage;
import static org.opencv.imgproc.Imgproc.circle;

/**
 * Created by malthe on 4/2/17.
 */
public class CircleDetector {

    //public static ImageViewer viewer = new ImageViewer();

    public static ReturnCircle detectCirclesRedFilter(BufferedImage img) {
        if(img != null) {
            Mat circles = new Mat();

            Mat red = convertToRedHsv(img);
            //viewer.show(red);
            Imgproc.HoughCircles(red, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 8, 100, 20, 0, 0 );
            //System.out.
            //println("#rows " + circles.rows() + " #cols " + circles.cols());
            double x = 0.0;
            double y = 0.0;
            int r = 0;
            ReturnCircle[] circleArray = new ReturnCircle[10];

            for( int i = 0; i < circles.rows(); i++ ) {
                double[] data = circles.get(i, 0);
                for (int j = 0; j < data.length; j++) {
                    x = data[0];
                    y = data[1];
                    r = (int) data[2];
                    circleArray[i] = new ReturnCircle(x,y,r, null);
                }
                Point imageCenter = new Point(640,320);
                Point center = new Point(x, y);
                System.out.println("ImageCenter calculated to "+center);
                //pointFeaturesToCircleCenter(red, center);

                //Image center

                circle(red, imageCenter, 6, new Scalar(255,255,255));

                // circle center
                circle(red, center, 3, new Scalar(255, 255, 255), -1);
                // circle outline
                circle(red, center, r, new Scalar(255, 255, 255), 10);
                //viewer.show(red);

                //Core.
            }
            //viewer.show(red);
            ReturnCircle biggestCircle = new ReturnCircle(0,0,-1, red);

            for(int i = 0; i < circles.rows(); i++) {
                if(circleArray[i].getRadius() > biggestCircle.getRadius()) {
                    biggestCircle = new ReturnCircle(circleArray[i]);
                }
            }

            return biggestCircle;
        }
        return new ReturnCircle(0.0, 0.0, 0, null);
    }
    public static MatOfPoint pointFeaturesToCircleCenter(Mat image, Point center) {
        MatOfPoint corners = new MatOfPoint();
        //corners.
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        //this function only works on gray scale imgs. it will throw exception if colored.
        //will identify points in the image
        Imgproc.goodFeaturesToTrack(gray, corners, 100, 0.1, 0.0);
        Point[] cornerpoints = corners.toArray();

        int x = 1;
        for (Point points : cornerpoints) {
            //draw vectors from identified image features to the circle center
            Imgproc.line(image, points, center, new Scalar( 0, 255, 0, 128 ) );
        }

        //draw corners

        //rectangle(image, new Point(cornerpoints[0].x, cornerpoints[0].y), new Point(cornerpoints[1].x, cornerpoints[1].y), new Scalar(255, 255, 0));
        //Imgproc.goodFeaturesToTrack();
        return corners;
    }

    public static Mat convertToRedHsv(BufferedImage img) {

        Mat image = bufferedImageToMat(img);

        Mat hsv_image = new Mat();
        Imgproc.cvtColor(image, hsv_image, Imgproc.COLOR_BGR2HSV);

        Mat lower_red_hue_range = new Mat();
        Mat upper_red_hue_range = new Mat();

        Core.inRange(hsv_image, new Scalar(0, 70, 50), new Scalar(10, 255, 255), lower_red_hue_range);
        Core.inRange(hsv_image, new Scalar(170, 70, 50), new Scalar(180, 255, 255), upper_red_hue_range);


        //Core.inRange(hsv_image, new Scalar(5,50,50), new Scalar(15, 255, 255), lower_red_hue_range);
        //Core.inRange(hsv_image, new Scalar(5, 100, 100), new Scalar(5, 255, 255), lower_red_hue_range);
        //Core.inRange(hsv_image, new Scalar(160, 200, 200), new Scalar(179, 255, 255), upper_red_hue_range);


        Mat red_hue_image = new Mat();
        Core.addWeighted(lower_red_hue_range, 1.0, upper_red_hue_range, 1.0, 0.0, red_hue_image);

        Imgproc.GaussianBlur(red_hue_image, red_hue_image, new Size(9,9), 2,2);

        return red_hue_image;


    }

    public static ReturnCircle detectCirclesGrayFilter(BufferedImage img, ImageViewer viewer) {
        Mat image = bufferedImageToMat(img);
        Mat gray = new Mat();
        //Mat blurred = new Mat();
        Mat circles = new Mat();

        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(gray, blurred, new Size(11,11),0);
        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 50, 0 );
        //System.out.println("#rows " + circles.rows() + " #cols " + circles.cols());
        double x = 0.0;
        double y = 0.0;
        int r = 0;
        ReturnCircle[] circleArray = new ReturnCircle[10];

        for( int i = 0; i < circles.rows(); i++ ) {
            double[] data = circles.get(i, 0);
            for (int j = 0; j < data.length; j++) {
                x = data[0];
                y = data[1];
                r = (int) data[2];
                circleArray[i] = new ReturnCircle(x,y,r, null);
            }
            Point imageCenter = new Point(320,180);
            Point center = new Point(x, y);
            // System.out.println("ImageCenter calculated to "+center);
            pointFeaturesToCircleCenter(image, center);

            //Image center

            circle(image, imageCenter, 6, new Scalar(255,255,255));

            // circle center
            circle(image, center, 3, new Scalar(0, 255, 0), -1);
            // circle outline
            circle(image, center, r, new Scalar(0, 0, 255), 1);
            viewer.show(image);

            //Core.
        }

        ReturnCircle biggestCircle = new ReturnCircle(0,0,-1, image);

        for(int i = 0; i < circles.rows(); i++) {
            if(circleArray[i].getRadius() > biggestCircle.getRadius()) {
                biggestCircle = new ReturnCircle(circleArray[i]);
            }
        }

        return biggestCircle;
    }
    public static Mat testRedFilter(BufferedImage img) {
        if(img != null) {
            Mat circles = new Mat();

            Mat red = convertToRedHsv(img);
            //viewer.show(red);
            Imgproc.HoughCircles(red, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 8, 100, 20, 0, 0);
            //System.out.
            //println("#rows " + circles.rows() + " #cols " + circles.cols());
            double x = 0.0;
            double y = 0.0;
            int r = 0;
            ReturnCircle[] circleArray = new ReturnCircle[10];

            for (int i = 0; i < circles.rows(); i++) {
                double[] data = circles.get(i, 0);
                for (int j = 0; j < data.length; j++) {
                    x = data[0];
                    y = data[1];
                    r = (int) data[2];
                    circleArray[i] = new ReturnCircle(x, y, r, null);
                }
                Point imageCenter = new Point(640, 320);
                Point center = new Point(x, y);
                System.out.println("ImageCenter calculated to " + center);
                //pointFeaturesToCircleCenter(red, center);

                //Image center

                circle(red, imageCenter, 6, new Scalar(255, 255, 255));

                // circle center
                circle(red, center, 3, new Scalar(255, 255, 255), -1);
                // circle outline
                circle(red, center, r, new Scalar(255, 255, 255), 10);
                //viewer.show(red);

                //Core.
            }
            //viewer.show(red);
            return red;
        }
        return new Mat();
    }

}
