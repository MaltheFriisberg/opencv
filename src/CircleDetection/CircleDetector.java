package CircleDetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

import Util.ImageViewer;
import static Util.ImageConverter.bufferedImageToMat;
import static org.opencv.imgproc.Imgproc.circle;

/**
 * Created by malthe on 4/2/17.
 */
public class CircleDetector {

    //public static ImageViewer viewer = new ImageViewer();

    public static ReturnCircle detectAndShowCircles(BufferedImage img, ImageViewer viewer) {
        Mat image = bufferedImageToMat(img);
        Mat gray = new Mat();
        //Mat blurred = new Mat();
        Mat circles = new Mat();

        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(gray, blurred, new Size(11,11),0);
        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
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
                circleArray[i] = new ReturnCircle(x,y,r);
            }
            Point imageCenter = new Point(320,180);
            Point center = new Point(x, y);
            System.out.println("ImageCenter calculated to "+center);
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

        ReturnCircle biggestCircle = new ReturnCircle(0,0,-1);

        for(int i = 0; i < circles.rows(); i++) {
            if(circleArray[i].getRadius() > biggestCircle.getRadius()) {
                biggestCircle = new ReturnCircle(circleArray[i]);
            }
        }

        return biggestCircle;
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

}
