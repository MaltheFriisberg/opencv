package Statemachine;

import CircleDetection.ReturnCircle;
import Interfaces.IDroneState;
import Misc.DroneBatteryListener;
import Misc.DroneExceptionListener;
import Misc.DroneVideoListener;
import Util.*;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.command.WifiMode;

import java.awt.image.BufferedImage;

import static CircleDetection.CircleDetector.detectCirclesGrayFilter;
import static CircleDetection.CircleDetector.detectCirclesRedFilter;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    ARDrone drone = null;

    static public boolean doneUpdatingStatemachine = true;

    // Listeners
    CommandManager cmd;
    DroneVideoListener videoListener;
    DroneBatteryListener batteryListener;
    DroneExceptionListener exceptionListener;
    QRScanner qrScanner;

    boolean firstEnter = true;
    private ImageViewer imageViewer;
    static public String outputText;
    static public String droneStateText;

    // Drone flight constants
    private final int flyThroughTime = 2000;
    private int flightSpeed = 5;

    boolean usingCommandManager = true;

    // Ring manager
    public int nextPort = 1;
    private final int mapPortTotal = 6;

    // Line up konstanter
    //static public BufferedImage autoControllerImage;
    private final int pictureDeviation = 100;
    private int pictureWidth = 1280;
    private int pictureHeight = 720;
    private final long FLYFORWARDCONST = 100000;
    private final int MAXALTITUDE = 3000; //3 meters
    private final int optimalCircleRadius = 110;
    private final int optimalCircleRadiusDeviation = 30;
    private final int timeBetweenCommands = 10;

    // Statemachines
    private DroneStates currentState;
    private ApproachStates approachStates = ApproachStates.CircleLineUp;

    // Andet
    private boolean isRunning;
    private boolean QRValid = false;
    private int lineUpCounter = 0;

    public DroneAutoController() {

        try {
            drone = new ARDrone();
            drone.start();
            cmd = drone.getCommandManager();

            cmd.setVideoChannel(VideoChannel.HORI);
            cmd.setVideoCodec(VideoCodec.H264_360P);
            //drone.getVideoManager().connect(1337);

            videoListener = new DroneVideoListener(this, drone);

            batteryListener = new DroneBatteryListener(drone);
            exceptionListener = new DroneExceptionListener();

           // drone.getVideoManager().addImageListener(videoListener);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        System.out.println("------------------------------");
        System.out.println("Navigation manager is connected = " + drone.getNavDataManager().isConnected());
        System.out.println("Video manager is connected = " + drone.getVideoManager().isConnected());
        System.out.println("Command manager is connected = " + drone.getCommandManager().isConnected());
        System.out.println("Configuration manager is connected = " + drone.getConfigurationManager().isConnected());
        System.out.println("-------------------------------");

        qrScanner = new QRScanner();
        imageViewer = new ImageViewer();

        currentState = DroneStates.Approach;
        approachStates = ApproachStates.CircleLineUp;
    }


    public void updateStateMachine(BufferedImage image) {

        doneUpdatingStatemachine = false;

        System.out.println(currentState.toString());

        if (firstEnter) {
            System.out.println("TAKE OFF!");
            pictureWidth = image.getWidth();
            pictureHeight = image.getHeight();
           // cmd.cancelRecordingNavData();
            cmd.takeOff().doFor(2000);
            cmd.hover();
            cmd.up(50).doFor(1000);
            cmd.hover();
            cmd.up(50).doFor(1000);
            cmd.hover();
            cmd.up(50).doFor(1000);
           // cmd.waitFor(4000);
           // cmd.up(50).doFor(5000);
            cmd.hover();
          //  cmd.landing();
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

        doneUpdatingStatemachine = true;
    }

    public void searchRing(BufferedImage image) {

        ReturnCircle circle = detectCirclesRedFilter(image);

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

        ReturnCircle circle = detectCirclesRedFilter(image);

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

                    if(usingCommandManager) {
                        cmd.stopRecordingNavData();
                        cmd.forward(20).doFor(flyThroughTime);
                        cmd.hover();
                    } else {
                        drone.forward();
                        cmd.hover();
                    }

                    approachStates = ApproachStates.CircleLineUp;
                   // currentState = DroneStates.Evaluation;
                  //  nextPort++;
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

            String commandOrder = selectOrderOfCommands(circle);

            System.out.println(commandOrder);
            boolean foundCommand = false;

            for(int i = 0; i < commandOrder.length(); i++) {
                switch (commandOrder.charAt(i)) {
                    case 'x':
                        if(!xAxisAdjust(circle)) { // Dronen er ikke linet up på x-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            foundCommand = true;
                            lineUpCounter = 0;
                            i = 5;
                        }
                        break;
                    case 'y':
                        if(!yAxisAdjust(circle)) { // Dronen er ikke linet up på y-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            foundCommand = true;
                            lineUpCounter = 0;
                            i = 5;
                        }
                        break;
                    case 'z':
                        if(!zAxisAdjust(circle)) { // Dronen er ikke linet up på z-aksen, så for at undgå flere commands for hver billede, break if løkken.
                            foundCommand = true;
                            lineUpCounter = 0;
                            i = 5;
                        }
                        break;
                }
                if(!foundCommand && i == commandOrder.length() - 1) { // Hvis dronen kom igennem alle commands med true (Korrekt lineup)
                    lineUpCounter++;
                    System.out.println("Perfekt center!");
                    if(lineUpCounter == 3) {
                        if (QRValid) {
                            approachStates = ApproachStates.FlyThrough;
                        } else {
                            searchQR(image);
                        }
                    }
                }
            }
            System.out.println("\n\n\n\n\n");
        }

    String selectOrderOfCommands(ReturnCircle circle) {
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
                // Hvis værdierne er ens?
                doCommand = "xyz";
            }

            return doCommand;
        } else {
            return "";
        }
    }

    boolean xAxisAdjust(ReturnCircle circle) {
        if (circle.getX() < pictureWidth / 2 - pictureDeviation) { // Dronen er for langt til højre
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
        } else if (circle.getX() > pictureWidth / 2 + pictureDeviation) { // Dronen er for langt til venstre
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
        if (circle.getY() - 20 < pictureHeight / 2 - pictureDeviation) { // Dronen er for langt under ringen
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
        } else if (circle.getY() > pictureHeight / 2 + pictureDeviation) { // Dronen er for langt over ringen
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

}
