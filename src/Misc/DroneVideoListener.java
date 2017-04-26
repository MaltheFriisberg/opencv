package Misc;

import Util.ImageViewer;
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
    private BufferedImage img;

    public DroneVideoListener(IARDrone drone) {
        this.drone = drone;
        this.imageViewer = new ImageViewer();
        drone.getVideoManager().addImageListener(this);
        this.openCV = new testOpenCV();

    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        counter++;
        img = bufferedImage;
        if (counter % 30 == 0) {
            detectAndShowCircles(bufferedImage, this.imageViewer);
            counter = 0;
        }
    }

    public BufferedImage getImg() {
        return img;
    }
}
