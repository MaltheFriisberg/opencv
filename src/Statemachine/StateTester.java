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

    enum DroneStates {
        SearchRing, SeachQR, Approach, Evaluation, Landing
    }

    public static void main(String args[]) {

        IARDrone drone = null;
        try {
            drone = new ARDrone();
            drone.start();
            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            //Remember to Toggle the camera on
            drone.toggleCamera();
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
            //drone.getVideoManager().connect(1337);
            //System.out.println("drone is connected : "+drone.getVideoManager().connect(1337));
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (drone != null) {

                CommandManager cmd = drone.getCommandManager();

                DroneVideoListener listener = new DroneVideoListener(drone);
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
            droneStates = DroneStates.SeachQR;
            DroneAutoController droneController = new DroneAutoController();

            int speed = 5;

            int i = 1300;
            while (true) {


                System.out.println(droneStates.toString());
                BufferedImage image = null;
                String imagepath = "Resources/newpictures/billlede" + i + ".png";
                i++;
                //String imagepath = "Resources/qrcodes/qrcode(1).png";
                try {
                    image = ImageIO.read(new File(imagepath));
                    viewer.show(image);
                    //1 fps pcmasterrace
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch (droneStates) {
                    case SearchRing:
                        if (droneController.searchRing(image, drone)) {
                            droneStates = DroneStates.SearchRing;
                        }

                        break;
                    case SeachQR:
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
                            droneStates = DroneStates.SeachQR;
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