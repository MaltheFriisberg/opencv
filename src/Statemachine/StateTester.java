package Statemachine;

import Util.ImageViewer;

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


        ImageViewer viewer = new ImageViewer();
        DroneStates droneStates;
        droneStates = DroneStates.SeachQR;
        DroneAutoController droneController = new DroneAutoController();

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
                    if(droneController.searchRing(image)) {
                        droneStates = DroneStates.SearchRing;
                    }

                    break;
                case SeachQR:
                    if (droneController.searchQR(image)) {          // Hvis QR kode findes og er korrekt i forhold til rækkefølgen
                        droneStates = DroneStates.Approach;
                    }
                    break;

                case Approach:
                    if (droneController.approach(image)) {        // Hvis dronen succesfuldt har fløjet igennem ringen
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
