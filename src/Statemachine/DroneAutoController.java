package Statemachine;

import CircleDetection.CircleDetector;
import Interfaces.IDroneState;
import Util.ImageViewer;
import Util.QRScanner;
import Util.ReturnCircle;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectAndShowCircles;
import static Statemachine.StateTester.DroneStates.SearchQR;
import static Statemachine.StateTester.DroneStates.SearchRing;
import static Util.DroneStates.Evaluation;
import static Util.DroneStates.Landing;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    //IARDrone drone;
    QRScanner qrScanner = new QRScanner();
    public int nextPort = 1;
    BufferedImage image;
    private final int deviation = 20;
    private final int pictureWidth = 640;
    private final int pictureHeight = 360;

//public Statemachine.DroneAutoController(IARDrone drone) {this.drone = drone;}

    public boolean searchRing(BufferedImage image) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if(circle.getRadius() != -1) {
            if(circle.getX() < pictureWidth / 2 + deviation) {
                // Ryk drone til højre
                System.out.println("Gå mod højre");

            } else if(circle.getX() > pictureWidth / 2 - deviation) {
                // Ryk drone til venstre
                System.out.println("Gå mod venstre");


            } else if(circle.getY() < pictureHeight / 2 + deviation) {
                // Ryk drone opad
                System.out.println("Gå opad");

            } else if (circle.getY() > pictureHeight / 2 - deviation) {
                // Ryk drone nedad
                System.out.println("Gå nedad");
            } else {
                // Dronen er perfekt centreret

                // Skift state til Approach
            }

        }


        return false;
    }

    @Override
    public boolean searchQR(BufferedImage image, IARDrone drone) {

        //this.drone.getCommandManager().hover();
        //turn 360 and look for circles.cr codes
        String temp = qrScanner.getQRCode(image);
        System.out.println("QR Code: " + temp);
        if (temp.equals("P.0" + Integer.toString(nextPort))) {
            nextPort++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean searchRing(BufferedImage image, IARDrone drone) {
        return false;
    }

    @Override
    public boolean approach(BufferedImage image, IARDrone drone) {
        //fly towards the object
        // this.drone.getCommandManager().doFor(1000).forward(2);
        return false;
    }

    @Override
    public boolean evaluate() {
        //boolean isCirclesInSight = detectAndShowCircles();
        //boolean isQRcodeInSight =
        return false;
    }

    @Override
    public boolean landing(IARDrone drone) {
        // this.drone.getCommandManager().landing();
        return false;
    }

    public void updateImage(BufferedImage image) {
        this.image = image;
    }
    private void stateSwitch() {

        while (true) {
            System.out.println(droneStates.toString());
            BufferedImage image = null;

            switch (droneStates) {
                case SearchRing:
                    if (searchRing(image, drone)) {
                        droneStates = SearchQR;
                    }

                    break;
                case SearchQR:
                    if (searchQR(image, drone)) {          // Hvis QR kode findes og er korrekt i forhold til rækkefølgen
                        droneStates = StateTester.DroneStates.Approach;
                    }
                    break;

                case Approach:
                    if (approach(image, drone)) {        // Hvis dronen succesfuldt har fløjet igennem ringen
                        droneStates = StateTester.DroneStates.Evaluation;
                    }
                    break;

                case Evaluation:
                    if (evaluate()) {        // Hvis der stadig er flere ubesøgte ringe tilbage
                        droneStates = SearchQR;
                    } else {                                // Hvis alle ringe er besøgt
                        droneStates = StateTester.DroneStates.Landing;
                    }
                    break;
                case Landing:

                    break;
            }

        }
    }
}
