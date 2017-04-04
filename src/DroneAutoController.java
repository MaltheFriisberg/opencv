import Interfaces.IDroneState;
import de.yadrone.base.IARDrone;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneAutoController implements IDroneState {
    IARDrone drone;

    public DroneAutoController(IARDrone drone) {
        this.drone = drone;
    }

    @Override
    public void search() {
        this.drone.getCommandManager().hover();
        //turn 360 and look for circles.cr codes
    }

    @Override
    public void approach() {
        //fly towards the object
        this.drone.getCommandManager().doFor(1000).forward(2);
    }

    @Override
    public void evaluate() {
        //boolean isCirclesInSight = detectAndShowCircles();
        //boolean isQRcodeInSight =
    }

    @Override
    public void landing() {
        this.drone.getCommandManager().landing();
    }
}
