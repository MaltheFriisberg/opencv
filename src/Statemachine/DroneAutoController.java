package Statemachine;

import CircleDetection.ReturnCircle;
import Interfaces.IDroneState;
import Misc.DroneVideoListener;
import Util.*;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectCirclesGrayFilter;
import static CircleDetection.CircleDetector.detectCirclesRedFilter;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    ARDrone drone = null;
    CommandManager cmd;
    DroneVideoListener videoListener;
    QRScanner qrScanner;
    boolean firstEnter = true;
    private ImageViewer imageViewer;
    static public String outputText;
    static public String droneStateText;

    // Drone flight constants
    private final int flyThroughTime = 2000;
    private int flightSpeed = 10;

    boolean usingCommandManager = true;

    // Ring manager
    public int nextPort = 1;
    private final int mapPortTotal = 6;

    // Line up konstanter
    //static public BufferedImage autoControllerImage;
    private final int pictureDeviation = 100;
    private final int pictureWidth = 1280;
    private final int pictureHeight = 720;
    private final long FLYFORWARDCONST = 100000;
    private final int MAXALTITUDE = 3000; //3 meters
    private final int optimalCircleRadius = 230;
    private final int optimalCircleRadiusDeviation = 30;
    private final int timeBetweenCommands = 10;

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
            // drone.toggleCamera();
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
        imageViewer = new ImageViewer();

        currentState = DroneStates.Approach;
        approachStates = ApproachStates.CircleLineUp;
    }


    public void updateStateMachine(BufferedImage image) {

        System.out.println(currentState.toString());
        droneStateText = currentState.toString();

        if (firstEnter) {
            System.out.println("TAKE OFF!");
            cmd.takeOff();
            //cmd.landing();
            firstEnter = false;
        }

        switch (currentState) {
            case SearchRing:
                searchRing(image);
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

    public void searchRing(BufferedImage image) {

        ReturnCircle circle = detectCirclesGrayFilter(image);

        if (circle.getRadius() != -1) {
            currentState = DroneStates.Approach;
        } else {
            if (usingCommandManager) {
                //cmd.up(flightSpeed);
                //cmd.hover();
                //cmd.spinRight(flightSpeed);
                //cmd.hover();
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

        while (QRCodeFound == false) {
            QRCodeCounter++;
            if (usingCommandManager) {
//                cmd.down(flightSpeed);
//                cmd.hover();
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
//                cmd.up(flightSpeed);
//                cmd.hover();
            } else {
                drone.up();
                drone.hover();
            }
        }
    }

    @Override
    public void approach(BufferedImage image) {

        //System.out.println("W: " + image.getWidth());
        //System.out.println("H: " + image.getHeight());

        ReturnCircle circle = detectCirclesGrayFilter(image);

        System.out.println("Radius: " + circle.getRadius());
        /*
        if (circle.getRadius() != -1) {
            approachStates = ApproachStates.CircleLineUp;
        } else {
            currentState = DroneStates.SearchRing;
        }
        */

        if (circle.getRadius() != -1) {
            System.out.println(approachStates.toString());
            switch (approachStates) {
                case CircleLineUp:
                    centerDroneToRing(circle, image);
                    break;

                case FlyThrough:
                    // Flyv ligeud og fortsæt i x sekunder..

                    if (usingCommandManager) {
                        cmd.forward(flightSpeed).doFor(flyThroughTime);
                        cmd.hover();
                    } else {
                        drone.forward();
                        cmd.hover();
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
            // cmd.landing();
            System.exit(0);
        } else {
            //    drone.landing();
        }
        isRunning = false;
    }

    public void centerDroneToRing(ReturnCircle circle, BufferedImage image) {
        if (circle.getRadius() != -1) {
            int zLineUp = circle.getRadius() - optimalCircleRadius;
            int xLineUp = (int) (circle.getX() - pictureWidth / 2);
            int yLineUp = (int) (circle.getY() - pictureHeight / 2);
            System.out.println("Z-axis: " + zLineUp);
            System.out.println("X-axis: " + xLineUp);
            System.out.println("y-axis: " + yLineUp);
            String doCommand = "";
            if (Math.abs(xLineUp) > Math.abs(yLineUp) && Math.abs(xLineUp) > Math.abs(zLineUp)) {
                // X er størst
                if (Math.abs(yLineUp) > Math.abs(zLineUp)) {
                    // Y er større end Z
                    doCommand = "xyz";
                } else {
                    // Z er større end Y
                    doCommand = "xzy";
                }
            } else if (Math.abs(yLineUp) > Math.abs(xLineUp) && Math.abs(yLineUp) > Math.abs(zLineUp)) {
                // Y er størst
                if (Math.abs(xLineUp) > Math.abs(zLineUp)) {
                    // X er større end Z
                    doCommand = "yxz";
                } else {
                    // Z er større end X
                    doCommand = "yzx";
                }
            } else if (Math.abs(zLineUp) > Math.abs(xLineUp) && Math.abs(zLineUp) > Math.abs(yLineUp)) {
                // Z er størst
                if (Math.abs(xLineUp) > Math.abs(yLineUp)) {
                    // X er større end Y
                    doCommand = "zxy";
                } else {
                    // Y er større end X
                    doCommand = "zyx";
                }
            } else {
                // Burde ikke være muligt?
            }
            System.out.println(doCommand);
            for (int i = 0; i < doCommand.length(); i++) {
                switch (doCommand.charAt(i)) {
                    case 'x':
                        if (!xAxisAdjust(circle)) { // Dronen er ikke linet up på x-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            i = 5;
                        }
                        break;
                    case 'y':
                        if (!yAxisAdjust(circle)) { // Dronen er ikke linet up på y-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            i = 5;
                        }
                        break;
                    case 'z':
                        if (!zAxisAdjust(circle)) { // Dronen er ikke linet up på z-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            i = 5;
                        }
                        break;
                }
                if (i == doCommand.length() - 1) { // Hvis dronen kom igennem alle commands med true (Korrekt lineup)
                    outputText = "Perfekt centret! Jubiiii!";
                    if (QRValid) {
                        approachStates = ApproachStates.FlyThrough;
                    } else {
                        searchQR(image);
                    }
                }
            }
            System.out.println("\n\n\n\n\n");
        }
    }

    boolean xAxisAdjust(ReturnCircle circle) {
        if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
            // Ryk drone til venstre
            System.out.println("Ryk " + Math.abs(circle.getX() - pictureWidth / 2 + pictureDeviation) + " længere til Venstre");
            outputText = "Venstre";
            if (usingCommandManager) {
                cmd.goLeft(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {
                drone.goLeft();
                drone.hover();
            }
            System.out.println("Færdig med venstre");
        } else if (circle.getX() > pictureWidth / 2 + pictureDeviation) {
            // Ryk drone til højre
            System.out.println("Ryk " + Math.abs(circle.getX() - pictureWidth / 2 + pictureDeviation) + " længere til Højre");
            outputText = "Højre";
            if (usingCommandManager) {
                cmd.goRight(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {
                drone.goRight();
                drone.hover();
            }
            System.out.println("Færdig med højre");
        } else { // Perfekt center på x-aksen
            return true;
        }
        return false;
    }

    boolean yAxisAdjust(ReturnCircle circle) {
        if (circle.getY() - 20 < pictureHeight / 2 - pictureDeviation) {
            // Ryk drone opad
            System.out.println("Ryk " + Math.abs(circle.getY() - pictureHeight / 2 + pictureDeviation) + " længere Op");
            if (usingCommandManager) {
                cmd.up(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {//
                drone.up();
                drone.hover();
            }
            outputText = "OP";
        } else if (circle.getY() > pictureHeight / 2 + pictureDeviation) {
            // Ryk drone nedad
            System.out.println("Ryk " + Math.abs(circle.getY() - pictureHeight / 2 + pictureDeviation) + " længere Ned");
            outputText = "Nedad";
            if (usingCommandManager) {
                cmd.down(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {
                drone.down();
                drone.hover();
            }
            System.out.println("Færdig med nedad");
        } else { // Perfekt center på y aksen
            return true;
        }
        return false;
    }

    boolean zAxisAdjust(ReturnCircle circle) {
        if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
            // Flyv længere væk
            System.out.println("Ryk " + Math.abs(circle.getRadius() - optimalCircleRadius - pictureDeviation) + " længere til tilbage");
            outputText = "Længere væk";
            if (usingCommandManager) {
                cmd.backward(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {
                drone.backward();
                drone.hover();
            }
            System.out.println("Færdig med længere væk");
        } else if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
            //  Flyv tættere på
            System.out.println("Ryk " + Math.abs(circle.getRadius() - optimalCircleRadius - pictureDeviation) + " længere til frem");
            outputText = "Tættere på";
            if (usingCommandManager) {
                cmd.forward(flightSpeed);
                cmd.waitFor(timeBetweenCommands);
                cmd.hover();
            } else {
                drone.forward();
                drone.hover();
            }
            System.out.println("Færdig med tættere på");
        } else { // Perfekt afstand
            return true;
        }
        return false;
    }

    //public void updateImage(BufferedImage image) {
    //    this.autoControllerImage = image;
    //}

}
