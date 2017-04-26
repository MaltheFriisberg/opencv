package Statemachine;

import Misc.DroneVideoListener;
import Util.ImageViewer;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Pyke-Laptop on 19-04-2017.
 */
public class StateTester {

    //public ApproachAnalyzer approachAnalyzer;

    /*public static enum DroneStates {
        SearchRing, SearchQR, Approach, Evaluation, Landing
    }*/
    
    /*public static void main(String args[]) {

        CommandManager cmd = null;
        DroneVideoListener listener = null;
        DroneAutoController droneController = null;
        IARDrone drone = null;
        try {
            drone = new ARDrone();
            drone.start();
            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            //Remember to Toggle the camera on
            drone.toggleCamera();
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
            drone.getCommandManager().setVideoCodecFps(15);
            //drone.getVideoManager().connect(1337);
            //System.out.println("drone is connected : "+drone.getVideoManager().connect(1337));
        } catch (Exception exc) {
            System.out.println("test");
            exc.printStackTrace();
        } finally {
            if (drone != null) {

                cmd = drone.getCommandManager();
                droneController = new DroneAutoController();
                listener = new DroneVideoListener(drone);
                drone.getVideoManager().addImageListener(listener);
                drone.getVideoManager().addImageListener(new ImageListener() {
                    @Override
                    public void imageUpdated(BufferedImage bufferedImage) {
                        int j = 0;
                        int x = 0;
                    }
                });
            }

            ImageViewer viewer = new ImageViewer();
            DroneStates droneStates;
            droneStates = DroneStates.SearchRing;


            int speed = 5;

            int i = 1300;

            while (true) {
                System.out.println(droneStates.toString());
                BufferedImage image = null;

                switch (droneStates) {
                    case SearchRing:
                        if (droneController.searchRing(image, drone)) {
                            droneStates = DroneStates.SearchQR;
                        }

                        break;
                    case SearchQR:
                        if (droneController.searchQR(image, drone)) {          // Hvis QR kode findes og er korrekt i forhold til rækkefølgen
                            droneStates = DroneStates.Approach;
                        }
                        break;

                    case Approach:
                        if (droneController.approach(image, drone)) {        // Hvis dronen succesfuldt har fløjet igennem ringen
                            droneStates = DroneStates.Evaluation;
                        }
                        break;

                    case Evaluation:
                        if (droneController.evaluate()) {        // Hvis der stadig er flere ubesøgte ringe tilbage
                            droneStates = DroneStates.SearchQR;
                        } else {                                // Hvis alle ringe er besøgt
                            droneStates = DroneStates.Landing;
                        }
                        break;
                    case Landing:

                        break;
                }
            }

        }
    }*/

}
