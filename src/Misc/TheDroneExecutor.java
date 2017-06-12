package Misc; /**
 * Created by malthe on 3/15/17.
 */

<<<<<<< HEAD
import Statemachine.DroneAutoController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
=======

import Misc.DroneVideoListener;
import Statemachine.DroneAutoController;
import de.yadrone.apps.paperchase.PaperChase;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.video.ImageListener;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

>>>>>>> malthe

public class TheDroneExecutor {
    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        //PaperChase pc = new PaperChase();
<<<<<<< HEAD
        //ARDrone drone = new ARDrone();
        DroneAutoController droneAutoController = new DroneAutoController();
=======
        IARDrone drone = new ARDrone();



        drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
        drone.getVideoManager().addImageListener(new DroneVideoRecorder());

        drone.start();

>>>>>>> malthe
    }

}
