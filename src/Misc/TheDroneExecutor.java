package Misc; /**
 * Created by malthe on 3/15/17.
 */


import Misc.DroneVideoListener;
import Statemachine.DroneAutoController;
import de.yadrone.apps.paperchase.PaperChase;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class TheDroneExecutor {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        PaperChase pc = new PaperChase();
        /*IARDrone drone = new ARDrone();

        drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
        drone.getVideoManager().addImageListener(new DroneVideoRecorder());



        drone.start();

        //drone.takeOff();

        //drone.hover();



        //drone.getCommandManager().hover().doFor(1000000);

        //drone.landing();
        */

    }

}
