package Threads;

import Statemachine.DroneAutoController;

/**
 * Created by malthe on 6/14/17.
 */
public class WorkerThread implements Runnable {

    private DroneAutoController controller;
    private boolean isRunning;

    public WorkerThread(DroneAutoController controller) {
        this.controller = controller;
        isRunning = true;
    }
    @Override
    public void run() {

        while(isRunning) {

            controller.updateDroneState();
            

        }

    }
    public void stop() {
        this.isRunning = false;
    }
}
