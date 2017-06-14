package Misc;

import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.BatteryListener;

import javax.swing.*;

/**
 * Created by Pyke-Laptop on 13-06-2017.
 */
public class DroneBatteryListener implements BatteryListener{

    IARDrone drone;

    public DroneBatteryListener(IARDrone drone) {
        this.drone = drone;
    }

    @Override
    public void batteryLevelChanged(int i) {
        System.out.println("Battery level: " + i + "%");
        drone.getNavDataManager().removeBatteryListener(this);
    }

    @Override
    public void voltageChanged(int i) {

    }
}
