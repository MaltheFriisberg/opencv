package Elipsedetection;

import Util.ImageConverter;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static Util.ImageConverter.bufferedImageToMat;

/**
 * Created by malthe on 4/2/17.
 */
public class ElipseDetector {

    public void detectAndShowElipses(BufferedImage sourceImage) {
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


    }
}
