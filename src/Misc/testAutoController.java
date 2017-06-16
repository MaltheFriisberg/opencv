package Misc;

import Statemachine.DroneAutoController;
import Util.ImageViewer;
import Util.ReturnCircle;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import testStuff.testDrone;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.detectCirclesGrayFilter;

/**
 * Created by malthe on 6/6/17.
 */
public class testAutoController {
    public static void main(String[] args) {
        IARDrone drone = new testDrone();
        ImageViewer viewer = new ImageViewer();
        //DroneAutoController controller = new DroneAutoController(drone);
        BufferedImage image;
        //controller.start();

        for(int i = 0; i < 3818; i++) {

            String imagepath = "Resources/newpictures/image"+i+".jpg";
            try {
                image = ImageIO.read(new File(imagepath));

                ReturnCircle circle = detectCirclesGrayFilter(image);
                //controller.updateImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
