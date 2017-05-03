package Approach;

import de.yadrone.base.IARDrone;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.awt.*;

/**
 * Created by malthe on 4/26/17.
 */
public class ApproachAnalyzer {

    Point circleCenter;
    Point imageCenter;
    Mat secondLastImage;
    Mat currentImage;

    public ApproachAnalyzer(IARDrone drone) {

    }

    public void updateImage(Mat image) {
        this.currentImage = image;
    }

    public void updateCenterPoints(Point circleCenter, Point imageCenter) {
        this.circleCenter = circleCenter;
        this.imageCenter = imageCenter;
    }

    public void calculateVector(Point circleCenter, Point imageCenter) {

        //return new Scalar();

    }


}
