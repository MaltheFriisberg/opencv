package Misc;

import Util.ImageViewer;
import org.opencv.core.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.detectAndShowWhiteCircles;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.rectangle;

/**
 * Created by malthe on 3/8/17.
 * Hello
 */
public class testOpenCV {

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static ImageViewer viewer = new ImageViewer();
    public static void main(String[] args) {
        Mat mat = new Mat();
        BufferedImage image;
        for (int i = 141; i <643; i++) {
            String imagepath = "Resources/billed/autoControllerImage"+i+".jpg";
            //String imagepath = "Resources/qrcodes/qrcode.png";

            try {
                image = ImageIO.read(new File(imagepath));
                detectAndShowWhiteCircles(image, viewer);

                //1 fps pcmasterrace
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }

}





