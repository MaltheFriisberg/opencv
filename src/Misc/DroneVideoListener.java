package Misc;

import Statemachine.DroneAutoController;
import Util.ImageViewer;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectAndShowCircles;

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

    public DroneVideoListener(DroneAutoController droneAutoController, ARDrone drone) {
        this.droneAutoController = droneAutoController;
        this.drone = drone;
        this.imageViewer = new ImageViewer();
        drone.getVideoManager().addImageListener(this);
        this.openCV = new testOpenCV();
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {

        counter++;

        if(counter % 5 == 0) {
            droneAutoController.updateStateMachine(bufferedImage);
            System.out.println("-------");
            System.out.println("Bredde: " + bufferedImage.getWidth());
        }

        if (counter % 15 == 0) {
            imageViewer.show(bufferedImage);
            //detectAndShowCircles(bufferedImage, this.imageViewer);
            counter = 0;
            //img = bufferedImage;
        }
    }

    //public BufferedImage getImg() {
      //  return img;
    //}
}
