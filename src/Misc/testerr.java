package Misc;

import de.yadrone.base.ARDrone;

/**
 * Created by Pyke-Laptop on 14-06-2017.
 */
public class testerr {

    public static void main(String[] args) {
        ARDrone drone = new ARDrone();
        DroneBatteryListener batteryListener = new DroneBatteryListener(drone);
    }
}
