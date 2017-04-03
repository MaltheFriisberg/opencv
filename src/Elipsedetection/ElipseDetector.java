package Elipsedetection;

import Util.ImageConverter;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static Util.ImageConverter.bufferedImageToMat;

/**
 * Created by malthe on 4/2/17.
 */
public class ElipseDetector {

    public static Mat detectAndShowElipses(BufferedImage sourceImage) {
        Mat image = bufferedImageToMat(sourceImage);
        //convert to gray
        Mat gray = new Mat();
        //Mat blurred = new Mat();
        Mat circles = new Mat();

        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Mat tresshold_output = new Mat();
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.threshold(gray, tresshold_output, 100.0, 255.0, Imgproc.THRESH_BINARY);

        Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

        /// Find the rotated rectangles and ellipses for each contour
        Vector<RotatedRect> minRect = new Vector<>( contours.size() );
        Vector<RotatedRect> minEllipse = new Vector<>( contours.size() );

        Mat result = new Mat();
        result.zeros(tresshold_output.size(), CvType.CV_8UC3);

        /*for( int i = 0; i < contours.size(); i++ )
        {
            minRect[i] = Imgproc.minAreaRect( new Mat(contours[i]) );
            if( contours[i].size() > 5 )
            {
                minEllipse[i] = Imgproc.fitEllipse( Mat(contours[i]) ); }
        }*/


        // if any contour exist...
        if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
        {
            System.out.println("contour exists");
            // for each contour, display it in blue
            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
            {
                Imgproc.drawContours(image, contours, idx, new Scalar(250, 0, 0));
            }

        }
        return image;



    }
}
