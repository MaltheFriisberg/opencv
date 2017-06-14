package Misc;

import Statemachine.DroneAutoController;
import Util.DroneDebugWindow;
import Util.ImageViewer;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.testRedFilter;


/**
 * Created by malthe on 3/15/17.
 */

public class DroneVideoListener implements ImageListener {

    private testOpenCV openCV;
    private ImageViewer imageViewer;
    private IARDrone drone;
    private int counter;
    //private BufferedImage img;
    DroneAutoController droneAutoController;
    private DroneDebugWindow debugWindow;


    public DroneVideoListener(DroneAutoController droneAutoController, ARDrone drone, DroneDebugWindow debugWindow) {
        this.droneAutoController = droneAutoController;
        this.drone = drone;
        this.imageViewer = new ImageViewer();
        drone.getVideoManager().addImageListener(this);
        this.openCV = new testOpenCV();
        this.debugWindow = debugWindow;

    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {

        droneAutoController.updateStateMachine(bufferedImage);
        //Mat test = testRedFilter(bufferedImage);
        debugWindow.imageUpdated(bufferedImage);

        counter++;
        if (counter % 30 == 0) {
            droneAutoController.updateStateMachine(bufferedImage);
            //Mat test = testRedFilter(bufferedImage);
            //debugWindow.imageUpdated(test);
            //detectAndShowCircles(bufferedImage, this.imageViewer);
            //this.debugWindow.imageUpdated(bufferedImage);

        }


    }

}
