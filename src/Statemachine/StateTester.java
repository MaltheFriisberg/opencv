package Statemachine;

import Misc.DroneVideoListener;
import Util.ImageViewer;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;

/**
 * Created by Pyke-Laptop on 19-04-2017.
 */
public class StateTester {

    enum DroneStates {
        SearchRing, SearchQR, Approach, Evaluation, Landing
    }
    @SuppressWarnings("Duplicates")
    public static void main(String args[]) {

        CommandManager cmd = null;
        DroneVideoListener listener = null;

        IARDrone drone = null;

        System.out.println("Made it to IARDrone drone = null");
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
            System.out.println("Made it out of try");
        } catch (Exception exc) {
            System.out.println("test");
            exc.printStackTrace();
            System.out.println("made it out of catch");
        } finally {
            if (drone != null) {

                cmd = drone.getCommandManager();

                System.out.println("starting to initialize the listener");
                listener = new DroneVideoListener(drone);
                drone.getVideoManager().addImageListener(listener);
                System.out.println("Implemented");
            }

            ImageViewer viewer = new ImageViewer();
            DroneStates droneStates;
            droneStates = DroneStates.SearchRing;
            DroneAutoController droneController = new DroneAutoController();

            int speed = 5;

            int i = 1300;

            while (true) {
                System.out.println(droneStates.toString());
                BufferedImage image = null;

                image = listener.getImg();
                switch (droneStates) {
                    case SearchRing:
                        if (droneController.searchRing(listener.getImg(), drone)) {
                            droneStates = DroneStates.SearchQR;
                        }

                        break;
                    case SearchQR:
                        if (droneController.searchQR(listener.getImg(), drone)) {          // Hvis QR kode findes og er korrekt i forhold til rækkefølgen
                            droneStates = DroneStates.Approach;
                        }
                        break;

                    case Approach:
                        if (droneController.approach(listener.getImg(), drone)) {        // Hvis dronen succesfuldt har fløjet igennem ringen
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
    }
}
