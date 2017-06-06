package Statemachine;

import CircleDetection.ReturnCircle;
import Util.ApproachStates;
import Util.ImageViewer;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.detectAndShowCircles;

/**
 * Created by Pyke-Laptop on 06-06-2017.
 */
public class StateMachineTester {

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static ImageViewer viewer = new ImageViewer();

    // Line up konstanter
    private BufferedImage image;
    static final int pictureDeviation = 20;
    static final int pictureWidth = 640;
    static final int pictureHeight = 360;
    static final long FLYFORWARDCONST = 100000;
    static final int MAXALTITUDE = 3000; //3 meters
    static final int lineUpSpeed = 10;
    static final int optimalCircleRadius = 120;
    static final int optimalCircleRadiusDeviation = 10;

    public static void main(String[] args) {
        Mat mat = new Mat();
        BufferedImage image;
        for (int i = 141; i <1000; i++) {
            String imagepath = "Resources/billed/image"+i+".jpg";
            //String imagepath = "Resources/qrcodes/qrcode.png";

            try {
                image = ImageIO.read(new File(imagepath));
                centerDroneToRing(detectAndShowCircles(image, viewer));

                //1 fps pcmasterrace
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void centerDroneToRing(ReturnCircle circle) {
        if (circle.getRadius() != -1) {
            if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
                // Ryk drone til venstre
                System.out.println("Ryk til venstre");

            } if (circle.getX() > pictureWidth / 2 + pictureDeviation) {
                // Ryk drone til højre
                System.out.println("Ryk til højre");

            }  if (circle.getY() < pictureHeight / 2 - pictureDeviation) {
                // Ryk drone opad
                System.out.println("Ryk opad");

            }  if (circle.getY() > pictureHeight / 2 + pictureDeviation) {
                // Ryk drone opad
                System.out.println("Ryk nedad");

            }  if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
                // Flyv tættere på
                System.out.println("Ryk længere væk");

            } if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
                // Flyv længere væk
                System.out.println("Ryk tættere på");

            } else {
                System.out.println("Dronen er center!");
            }
        }
    }
}
