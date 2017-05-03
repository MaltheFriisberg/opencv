package Statemachine;

import Interfaces.IDroneState;
import Misc.DroneVideoListener;
import Util.*;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectAndShowCircles;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    ARDrone drone;
    CommandManager commandManager;
    DroneVideoListener videoListener;
    QRScanner qrScanner;

    public int nextPort = 1;
    private final int mapPortTotal = 6;

    BufferedImage image;
    private final int pictureDeviation = 20;
    private final int pictureWidth = 640;
    private final int pictureHeight = 360;

    // Approach constants
    private final int optimalCircleRadius = 30;
    private final int optimalCircleRadiusDeviation = 10;
    private ApproachStates approachStates;

    private boolean isRunning;
    private DroneStates currentState;

    public DroneAutoController(ARDrone drone) {
        this.drone = drone;
        this.drone.start();
        System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
        this.drone.toggleCamera();
        this.drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
        this.commandManager = drone.getCommandManager();
        this.videoListener = new DroneVideoListener(this);
        this.drone.getVideoManager().addImageListener(this.videoListener);
        this.qrScanner = new QRScanner();
    }

    public void start() {

        this.isRunning = true;
        this.currentState = DroneStates.SearchRing;
        while (isRunning) {
            //System.out.println(droneStates.toString());
            BufferedImage image = null;
            DroneStates state = DroneStates.Evaluation;

            switch (currentState) {
                case SearchRing:
                    searchRing(image);
                    break;

                case SearchQR:
                    searchQR(image);
                    break;

                case Approach:
                    approach(image);
                    break;

                case Evaluation:
                    evaluate();
                    break;

                case Landing:
                    landing();
                    break;
            }
        }
    }

    public void searchRing(BufferedImage image) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if (circle.getRadius() != -1) {
            if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
                // Ryk drone til højre
                System.out.println("Gå mod højre");

            } else if (circle.getX() > pictureWidth / 2 + pictureDeviation) {
                // Ryk drone til venstre
                System.out.println("Gå mod venstre");


            } else if (circle.getY() < pictureHeight / 2 - pictureDeviation) {
                // Ryk drone nedad
                System.out.println("Gå nedad");

            } else if (circle.getY() > pictureHeight / 2 + pictureDeviation) {
                // Ryk drone opad
                System.out.println("Gå opad");
            } else {
                // Dronen er perfekt centreret
                // Skift state til Approach
                currentState = DroneStates.Approach;
            }
        }
    }

    @Override
    public void searchQR(BufferedImage image) {

        //this.drone.getCommandManager().hover();
        //turn 360 and look for circles.cr codes
        String temp = qrScanner.getQRCode(image);
        System.out.println("QR Code: " + temp);
        if (temp.equals("P.0" + Integer.toString(nextPort))) {
            currentState = DroneStates.SearchRing;
        }
    }

    @Override
    public void approach(BufferedImage image) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if (circle.getRadius() != -1) {
            approachStates = ApproachStates.CircleFound;
        } else {
            approachStates = ApproachStates.CircleNotFound;
        }

        switch (approachStates) {
            case CircleFound:
                if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
                    // Flyv tættere på

                } else if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
                    // Flyv længere væk
                } else { // Dronen har den perfekte afstand til cirklen
                    approachStates = ApproachStates.FlyThrough;
                }
                break;
            case CircleNotFound:
                // Flyv op og ned og drej for at finde cirkel??? (Den burde være der, da den ellers ikke ville skifte til approach state)
                break;
            case FlyThrough:
                // Flyv ligeud og fortsæt i x sekunder..
                currentState = DroneStates.Evaluation;
                nextPort++;
                break;
        }

    }

    @Override
    public void evaluate() {

        if (nextPort == mapPortTotal) {
            currentState = DroneStates.SearchRing;
        } else {
            currentState = DroneStates.Landing;
        }
    }

    @Override
    public void landing() {
        // this.drone.getCommandManager().landing();
    }

    public void updateImage(BufferedImage image) {
        this.image = image;
    }
    /*private void stateSwitch() {

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
    }*/
}
