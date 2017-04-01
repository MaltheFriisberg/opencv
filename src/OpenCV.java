import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.rectangle;

/**
 * Created by malthe on 3/8/17.
 */
public class OpenCV {



    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static ImageViewer viewer = new ImageViewer();
    public static void main(String[] args) {
        Mat mat = new Mat();
        BufferedImage image;
        for (int i = 141; i <643; i++) {
            String imagepath = "Resources/billed/image"+i+".jpg";

            try {
                image = ImageIO.read(new File(imagepath));
                detectAndShowCircles(image);
                //1 fps pcmasterrace
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    public static void detectAndShowCircles(BufferedImage img) {
        Mat image = bufferedImageToMat(img);
        Mat gray = new Mat();
        Mat circles = new Mat();

        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0 );
        System.out.println("#rows " + circles.rows() + " #cols " + circles.cols());
        double x = 0.0;
        double y = 0.0;
        int r = 0;

        for( int i = 0; i < circles.rows(); i++ ) {
            double[] data = circles.get(i, 0);
            for (int j = 0; j < data.length; j++) {
                x = data[0];
                y = data[1];
                r = (int) data[2];
            }
            Point center = new Point(x, y);
            pointFeaturesToCircleCenter(image, center);
            // circle center
            circle(image, center, 3, new Scalar(0, 255, 0), -1);
            // circle outline
            circle(image, center, r, new Scalar(0, 0, 255), 1);
            viewer.show(image);

            //Core.
        }
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
    private  static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}





