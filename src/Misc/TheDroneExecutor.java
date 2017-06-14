package Misc; /**
 * Created by malthe on 3/15/17.
 */

import Statemachine.DroneAutoController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class TheDroneExecutor {
    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        //PaperChase pc = new PaperChase();
        //ARDrone drone = new ARDrone();
        DroneAutoController droneAutoController = new DroneAutoController();
    }

}
