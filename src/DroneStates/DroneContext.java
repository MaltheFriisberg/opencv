package DroneStates;

import Interfaces.IDroneState;
import de.yadrone.base.IARDrone;

/**
 * Created by malthe on 4/4/17.
 */
public class DroneContext {
    IDroneState state;
    IARDrone drone;

    public DroneContext(IARDrone drone, IDroneState state) {
        this.drone = drone;
        this.state = state;
    }
}
