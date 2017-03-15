import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

/**
 * Created by malthe on 3/15/17.
 */
public class DroneVideoListener implements ImageListener {

    private OpenCV openCV;
    //private ImageViewer imageViewer;
    private IARDrone drone;

    public DroneVideoListener(IARDrone drone) {
        this.drone = drone;
        drone.getVideoManager().addImageListener(this);
        this.openCV = new OpenCV();

    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        openCV.detectAndShowCircles(bufferedImage);
    }
}
