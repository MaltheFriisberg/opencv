package Statemachine;

import CircleDetection.ReturnCircle;
import Interfaces.IDroneState;
import Misc.DroneVideoListener;
import Util.*;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectAndShowCircles;
import static CircleDetection.CircleDetector.detectCircles;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    ARDrone drone = null;
    CommandManager cmd;
    DroneVideoListener videoListener;
    QRScanner qrScanner;
    private int speed = 30;
    boolean firstEnter = true;

    boolean usingCommandManager = true;

    // Ring manager
    public int nextPort = 1;
    private final int mapPortTotal = 6;

    // Line up konstanter
    //static public BufferedImage autoControllerImage;
    private final int pictureDeviation = 20;
    private final int pictureWidth = 640;
    private final int pictureHeight = 360;
    private final long FLYFORWARDCONST = 100000;
    private final int MAXALTITUDE = 3000; //3 meters
    private final int optimalCircleRadius = 90;
    private final int optimalCircleRadiusDeviation = 10;

    // Statemachines
    private DroneStates currentState;
    private ApproachStates approachStates = ApproachStates.CircleLineUp;

    // Andet
    private boolean isRunning;
    private boolean QRValid = false;

    public DroneAutoController() {

        try {
            System.out.println("Line 1");
            drone = new ARDrone();
            System.out.println("Line 2");
            drone.start();
            System.out.println("Line 3");
            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            System.out.println("Line 4");
            drone.toggleCamera();
            System.out.println("Line 5");
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
            System.out.println("Line 6");
            //drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
            System.out.println("Line 7");
            //drone.getVideoManager().connect(1337);
            System.out.println("Line 8");
            cmd = drone.getCommandManager();
            System.out.println("Line 9");
            videoListener = new DroneVideoListener(this, drone);
            System.out.println("Line 10");
            drone.getVideoManager().addImageListener(videoListener);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        /*
        this.drone = drone;
        autoControllerImage = null;
        System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
        this.drone.toggleCamera();
        this.drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
        this.commandManager = drone.getCommandManager();
        this.commandManager.setMaxAltitude(MAXALTITUDE);
        this.videoListener = new DroneVideoListener(this, drone);
        this.drone.getVideoManager().addImageListener(this.videoListener);
        */

        qrScanner = new QRScanner();

        currentState = DroneStates.Approach;
        approachStates = ApproachStates.CircleLineUp;
    }


    public void updateStateMachine(BufferedImage image) {

        System.out.println(currentState.toString());

        if (firstEnter) {
            System.out.println("TAKE OFF!");
            cmd.takeOff();
            cmd.landing();
            firstEnter = false;
        }

        switch (currentState) {
            case SearchRing:
                searchRing(image);
                break;

            case Approach:
                System.out.println("Går ind i approach!");
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

    public void searchRing(BufferedImage image) {

        ReturnCircle circle = detectAndShowCircles(image, new ImageViewer());

        if (circle.getRadius() != -1) {
            currentState = DroneStates.Approach;
        } else {
            if (usingCommandManager) {
                cmd.up(speed);
                cmd.hover();
                cmd.spinRight(speed);
                cmd.hover();
            } else {
                drone.up();
                drone.hover();
                drone.spinRight();
                drone.hover();
            }
        }
    }

    @Override
    public void searchQR(BufferedImage image) {
        long QRCodeCounter = 0;

        boolean QRCodeFound = false;

        // TODO: Test (slettes når QR-kode skal bruges)
        QRValid = true;
        QRCodeFound = true;

        while (!QRCodeFound) {
            QRCodeCounter++;
            if (usingCommandManager) {
                cmd.down(speed);
                cmd.hover();
            } else {
                drone.down();
                drone.hover();
            }
            String temp = qrScanner.getQRCode(image);
            System.out.println("QR Code: " + temp);
            if (temp.equals("P.0" + Integer.toString(nextPort))) {
                QRCodeFound = true;
                QRValid = true;
            }
        }
        for (int i = 0; i < QRCodeCounter; i++) {
            if (usingCommandManager) {
                cmd.up(speed);
                cmd.hover();
            } else {
                drone.up();
                drone.hover();
            }
        }
    }

    @Override
    public void approach(BufferedImage image) {

        System.out.println("Er inde i approach!");
        ReturnCircle circle = detectCircles(image);

        System.out.println(circle.getRadius());
        /*
        if (circle.getRadius() != -1) {
            approachStates = ApproachStates.CircleLineUp;
        } else {
            currentState = DroneStates.SearchRing;
        }
        */

        if (circle.getRadius() != -1) {

            System.out.println("Starter centrering");

            switch (approachStates) {

                case CircleLineUp:
                    centerDroneToRing(circle, image);
                    break;

                case FlyThrough:
                    // Flyv ligeud og fortsæt i x sekunder..
                    for (int i = 0; i < FLYFORWARDCONST; i++) {
                        if (usingCommandManager) {
                            cmd.forward(speed);
                            cmd.hover();
                        } else {
                            drone.forward();
                            drone.hover();
                        }
                    }
                    currentState = DroneStates.Evaluation;
                    nextPort++;
                    QRValid = false;
                    break;
            }
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
        if (usingCommandManager) {
            cmd.landing();
        } else {
            drone.landing();
        }
        isRunning = false;
    }

    public void centerDroneToRing(ReturnCircle circle, BufferedImage image) {
        if (circle.getRadius() != -1) {

            if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
                // Flyv længere væk
                System.out.println("Længere væk");
                if (usingCommandManager) {
                    cmd.backward(speed);
                    cmd.hover();
                } else {
                    drone.backward();
                    drone.hover();
                }


            } else if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
                // Flyv tættere på
                System.out.println("Tættere på");
                if (usingCommandManager) {
                    cmd.forward(speed);
                    cmd.hover();
                } else {
                    drone.forward();
                    drone.hover();
                }

            } else if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
                // Ryk drone til venstre
                System.out.println("Venstre");
                if (usingCommandManager) {
                    cmd.goLeft(speed);
                    cmd.hover();
                } else {
                    drone.goLeft();
                    drone.hover();
                }

            } else if (circle.getX() > pictureWidth / 2 + pictureDeviation) {
                // Ryk drone til højre
                System.out.println("Højre");
                if (usingCommandManager) {
                    cmd.goRight(speed);
                    cmd.hover();
                } else {
                    drone.goRight();
                    drone.hover();
                }

            } else if (circle.getY() < pictureHeight / 2 - pictureDeviation) {
                // Ryk drone opad
                System.out.println("Opad");
                if (usingCommandManager) {
                    cmd.up(speed);
                    cmd.hover();
                } else {
                    drone.up();
                    drone.hover();
                }

            } else if (circle.getY() > pictureHeight / 2 + pictureDeviation) {
                // Ryk drone nedad
                System.out.println("Nedad");
                if (usingCommandManager) {
                    cmd.down(speed);
                    cmd.hover();
                } else {
                    drone.down();
                    drone.hover();
                }

            } else {
                System.out.println("Perfekt!");
                if (QRValid == true) {
                    approachStates = ApproachStates.FlyThrough;
                } else {
                    searchQR(image);
                }
            }
        }
    }

    //public void updateImage(BufferedImage image) {
    //    this.autoControllerImage = image;
    //}

}
