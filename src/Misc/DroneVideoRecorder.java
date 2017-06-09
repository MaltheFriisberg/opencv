package Misc;

import Util.ImageViewer;
import de.yadrone.base.navdata.VideoListener;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.detectCirclesRedFilter;

/**
 * Created by malthe on 6/7/17.
 */
public class DroneVideoRecorder implements ImageListener{

    private boolean saveToDisk;
    private int count;
    ImageViewer viewer;

    public DroneVideoRecorder() {
        this.viewer = new ImageViewer();
    }

    @Override
    public void imageUpdated(BufferedImage bufferedImage) {
        //System.out.println("saving picture to disk");
        detectCirclesRedFilter(bufferedImage, this.viewer);
        //viewer.show(bufferedImage);
        saveImgToDisk(bufferedImage);
    }

    private void saveImgToDisk(BufferedImage image) {


        File outputfile = new File("image"+count+".jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;


    }
}
