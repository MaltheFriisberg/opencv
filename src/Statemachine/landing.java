package Statemachine;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;

/**
 * Created by Pyke-Laptop on 13-06-2017.
 */
public class landing {

    public static void main(String[] args) {
        IARDrone drone = new ARDrone();
        drone.start();
        drone.getCommandManager().takeOff();
        drone.getCommandManager().landing();
    }
}
