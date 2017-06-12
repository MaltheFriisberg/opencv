package Misc;

import CircleDetection.CircleDetector;
import Util.ImageViewer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.convertToRedHsv;

import static CircleDetection.CircleDetector.detectCirclesRedFilter;
import static CircleDetection.CircleDetector.testRedFilter;
import static Util.ImageConverter.bufferedImageToMat;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.rectangle;

/**
 * Created by malthe on 3/8/17.
 * Hello
 */
public class testOpenCV {



    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    //public static ImageViewer viewer = new ImageViewer();
    public static void main(String[] args) {

        Mat mat = new Mat();
        BufferedImage image;
        ImageViewer viewer = new ImageViewer();
        DroneDebugWindow debugWindow = new DroneDebugWindow();

        for (int i = 178; i <565; i++) {
            String imagepath = "Resources/pictures720p/image"+i+".jpg";
            //String imagepath = "Resources/qrcodes/qrcode.png";

            try {
                image = ImageIO.read(new File(imagepath));
                //long startTime = System.nanoTime();
                Mat result = testRedFilter(image);
                //long stopTime = System.nanoTime();
                //viewer.show(image);
                //debugWindow.imageUpdated(image, ""+i, ""+i);
                //System.out.println((stopTime - startTime)/1000000000.0);
                //Mat red = convertToRedHsv(image);
                //viewer.show(red);
                debugWindow.imageUpdated(result, ""+i, ""+i);

                //1 fps pcmasterrace
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //BufferedImage image;
        /*String imagepath = "Resources/newpictures/IMG_20170607_104612.jpg";

        try {
            image = ImageIO.read(new File(imagepath));
            detectAndShowCircles(image, viewer);
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        }

}





