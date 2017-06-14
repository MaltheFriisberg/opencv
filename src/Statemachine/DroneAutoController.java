package Statemachine;


import Interfaces.IDroneState;
import Misc.DroneVideoListener;
import Threads.WorkerThread;
import Util.*;
import Util.ReturnCircle;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;

import java.awt.image.BufferedImage;
import java.util.Stack;

import static CircleDetection.CircleDetector.detectCirclesGrayFilter;
import static CircleDetection.CircleDetector.detectCirclesRedFilter;
import static Util.ApproachStates.FlyThrough;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    ARDrone drone = null;
    CommandManager cmd;
    DroneVideoListener videoListener;
    QRScanner qrScanner;
    boolean firstEnter = true;
    private Stack<BufferedImage> imageStack;
    static public String outputText;
    static public String droneStateText;
    private DroneDebugWindow debugWindow;

    // Drone flight constants
    private final int flyThroughTime = 3000;
    private int flightSpeed = 10;
    private Thread wThread;
    boolean usingCommandManager = true;

    // Ring manager
    public int nextPort = 1;
    private final int mapPortTotal = 6;

    // Line up konstanter
    //static public BufferedImage autoControllerImage;
    private final int pictureDeviation = 80;
    private final int pictureWidth = 1280;
    private final int pictureHeight = 720;
    private final long FLYFORWARDCONST = 100000;
    private final int MAXALTITUDE = 3000; //3 meters
    private final int optimalCircleRadius = 260;
    private final int optimalCircleRadiusDeviation = 30;
    private final int timeBetweenCommands = 5;

    // Statemachines
    private DroneStates currentState;
    private ApproachStates approachStates = ApproachStates.CircleLineUp;

    // Andet
    private boolean isRunning;
    private boolean QRValid = false;

    public DroneAutoController() {

        this.debugWindow = new DroneDebugWindow();

        try {

            drone = new ARDrone();

            drone.start();
            cmd = drone.getCommandManager();
            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());

            cmd.setVideoChannel(VideoChannel.HORI);

            //drone.getCommandManager().setWifiMode(WifiM)

            cmd.setVideoCodec(VideoCodec.H264_720P);

            //drone.getVideoManager().connect(1337);



            videoListener = new DroneVideoListener(this, drone, this.debugWindow);

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
        this.wThread = new Thread(new WorkerThread(this));
        qrScanner = new QRScanner();


        currentState = DroneStates.Approach;
        approachStates = ApproachStates.CircleLineUp;
        firstEnter = true;
    }
    // only the worker thread should call this method!!
    public synchronized void updateDroneState() {


        BufferedImage image;

        if(!imageStack.empty()) {
            image = imageStack.pop();
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


    }

    //The video manager thread enters here, no heavy work should be done.
    public void imageUpdated(BufferedImage image) {

        imageStack.push(image);



        System.out.println(currentState.toString());
        droneStateText = currentState.toString();

        this.debugWindow.updateState(droneStateText);

        if (firstEnter) {
            System.out.println("TAKE OFF!");
            debugWindow.updateDirection("TAKE OFF");
            //start the worker thread
            this.wThread.start();
            //cmd.takeOff();
            //cmd.up(50).doFor(2000);
            //cmd.waitFor(2000);
            //cmd.up(50).doFor(2000);
            //cmd.landing();
            firstEnter = false;
        }
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

                    if(usingCommandManager) {
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

            System.out.println("\n\n\n\n\n");

            if (circle.getRadius() > optimalCircleRadius + pictureDeviation) { // Dronen er for langt væk fra circlen
                // Flyv længere væk
                //System.out.println("Ryk " + Math.abs(circle.getRadius() - optimalCircleRadius - pictureDeviation) + " længere til tilbage");
                outputText = "Længere væk";
                this.debugWindow.updateDirection("Længere væk");
                if (usingCommandManager) {
                    cmd.backward(flightSpeed);
                    cmd.waitFor(timeBetweenCommands);
                    cmd.hover();
                } else {
                    drone.backward();
                    drone.hover();
                }


            } else if (circle.getRadius() < optimalCircleRadius - pictureDeviation) { // Dronen er for tæt på cirklen
                //  Flyv tættere på
                //System.out.println("Ryk " + Math.abs(circle.getRadius() - optimalCircleRadius - pictureDeviation) + " længere til frem");
                //outputText = "Tættere på";
                this.debugWindow.updateDirection("Tættere på");
                if (usingCommandManager) {
                    cmd.forward(flightSpeed);
                    cmd.waitFor(timeBetweenCommands);
                    cmd.hover();
                } else {
                    drone.forward();
                    drone.hover();
                }
                System.out.println("Færdig med tættere på");

            } else if (circle.getX() < pictureWidth / 2 - pictureDeviation) {
                // Ryk drone til venstre

                this.debugWindow.updateDirection("Venstre");
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
                this.debugWindow.updateDirection("Højre");
                if (usingCommandManager) {
                    cmd.goRight(flightSpeed);
                    cmd.waitFor(timeBetweenCommands);
                    cmd.hover();
                } else {
                    drone.goRight();
                    drone.hover();
                }
                System.out.println("Færdig med højre");

            } else if (circle.getY() - 5 < pictureHeight / 2 - pictureDeviation) {
                // Ryk drone opad
                this.debugWindow.updateDirection("Op");
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
                this.debugWindow.updateDirection("Nedad");
                outputText = "Nedad";
                if (usingCommandManager) {
                    cmd.down(flightSpeed);
                    cmd.waitFor(timeBetweenCommands);
                    cmd.hover();
                } else {
                    drone.down();
                    drone.hover();
                }


            } else {
                outputText = "Perfekt centret! Jubiiii!";
                this.debugWindow.updateDirection("- !Centreret! -");
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