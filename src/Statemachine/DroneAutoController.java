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

    private BufferedImage image;
    private final int pictureDeviation = 20;
    private final int pictureWidth = 640;
    private final int pictureHeight = 360;
    private final long FLYFORWARDCONST = 100000;
    private final int MAXALTITUDE = 3000; //3 meters

    // Approach constants
    private final int optimalCircleRadius = 90;
    private final int optimalCircleRadiusDeviation = 10;
    private ApproachStates approachStates;

    private boolean isRunning;
    private DroneStates currentState;

    public DroneAutoController(ARDrone drone) {
        this.drone = drone;
        this.drone.start();
        image = null;
        System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
        this.drone.toggleCamera();
        this.drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
        this.commandManager = drone.getCommandManager();
        this.commandManager.setMaxAltitude(MAXALTITUDE);
        this.videoListener = new DroneVideoListener(this, this.drone);
        this.drone.getVideoManager().addImageListener(this.videoListener);
        this.qrScanner = new QRScanner();
    }

    public void start() {

        this.isRunning = true;
        this.currentState = DroneStates.SearchRing;
        while (isRunning) {
            //System.out.println(droneStates.toString());
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

        if (circle.getRadius() != -1){
        currentState = DroneStates.Approach;
        }else {
            drone.spinRight();
            drone.hover();
        }

    }

    @Override
    public void searchQR(BufferedImage image) {
        long QRCodeCounter = 0;
        boolean QRCodeFound = false;

        while(!QRCodeFound){
            QRCodeCounter++;
            drone.down();
            drone.hover();
            String temp = qrScanner.getQRCode(image);
            System.out.println("QR Code: " + temp);
            if (temp.equals("P.0" + Integer.toString(nextPort))) {
                QRCodeFound = true;
            }
        }
        for (int i = 0; i < QRCodeCounter; i++){
            drone.up();
            drone.hover();
        }
        currentState = DroneStates.Approach;
    }

    @Override
    public void approach(BufferedImage image) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if (circle.getRadius() != -1) {
            approachStates = ApproachStates.CircleLineUp;
        } else {
            currentState = DroneStates.SearchRing;
        }

        switch (approachStates) {

            case CircleLineUp:
                if (circle.getRadius() != -1) {
                    if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
                        // Ryk drone til højre
                        drone.goRight();
                        //commandManager.goRight(15);
                        drone.hover();
                    } else if (circle.getX() > pictureWidth / 2 + pictureDeviation) {
                        // Ryk drone til venstre
                        drone.goLeft();
                        //commandManager.goLeft(15);
                        drone.hover();
                    } else if (circle.getY() < pictureHeight / 2 - pictureDeviation) {
                        // Ryk drone nedad
                        drone.down();
                        //commandManager.down(15);
                        drone.hover();
                    } else if (circle.getY() > pictureHeight / 2 + pictureDeviation) {
                        // Ryk drone opad
                        drone.up();
                        //commandManager.up(15);
                        drone.hover();
                    } else if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
                        // Flyv tættere på
                        drone.backward();
                        drone.hover();
                    } else if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
                        // Flyv længere væk
                        drone.forward();
                        drone.hover();
                    } else {
                        approachStates = ApproachStates.FlyThrough;
                    }
                }
                break;
            case FlyThrough:
                // Flyv ligeud og fortsæt i x sekunder..

                for (int i = 0; i < FLYFORWARDCONST; i++) {
                    drone.forward();
                    drone.hover();
                }
                currentState = DroneStates.Evaluation;
                nextPort++;
                break;
        }

    }

    @Override
    public void evaluate() {

        if (nextPort == mapPortTotal) {
            currentState = DroneStates.Landing;
        } else {
            currentState = DroneStates.SearchRing;
        }
    }

    @Override
    public void landing() {
        // this.drone.getCommandManager().landing();
        drone.landing();
    }

    public void updateImage(BufferedImage image) {
        System.out.println(currentState);
        this.image = image;
    }

}
