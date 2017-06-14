package Misc;

import Statemachine.DroneAutoController;
import Util.ImageViewer;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

import static Statemachine.DroneAutoController.doneUpdatingStatemachine;


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
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        if(doneUpdatingStatemachine) {
            imageViewer.show(bufferedImage);
            droneAutoController.updateStateMachine(bufferedImage);
        }
    }
}
