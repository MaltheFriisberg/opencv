package Misc; /**
 * Created by malthe on 3/15/17.
 */


import Misc.DroneVideoListener;
import Statemachine.DroneAutoController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;


public class TheDroneExecutor {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        IARDrone drone = new ARDrone();

        drone.start();

        drone.takeOff();

        //drone.hover();



        drone.getCommandManager().hover().doFor(1000000);

        drone.landing();

    }

}
