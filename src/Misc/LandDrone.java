package Misc;

import de.yadrone.base.ARDrone;

/**
 * Created by simon on 6/7/17.
 */
public class LandDrone {


    public static void main(String[] args) {
        ARDrone drone = new ARDrone();

        drone.start();
        drone.landing();
    }
}
