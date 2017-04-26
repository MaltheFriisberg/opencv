package Statemachine;

import CircleDetection.CircleDetector;
import Interfaces.IDroneState;
import Util.ImageViewer;
import Util.QRScanner;
import Util.ReturnCircle;
import de.yadrone.base.IARDrone;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectAndShowCircles;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {

    QRScanner qrScanner = new QRScanner();
    public int nextPort = 1;

    private final int deviation = 20;
    private final int pictureWidth = 640;
    private final int pictureHeight = 360;


    public boolean searchRing(BufferedImage image, IARDrone drone) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if(circle.getRadius() != -1) {
            if(circle.getX() < pictureWidth / 2 + deviation) {
                // Ryk drone til højre

            } else if(circle.getX() > pictureWidth / 2 - deviation) {
                // Ryk drone til venstre

            } else if(circle.getY() < pictureHeight / 2 + deviation) {
                // Ryk drone opad

            } else if(circle.getY() > pictureHeight / 2 - deviation) {
                // Ryk drone nedad

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
}
