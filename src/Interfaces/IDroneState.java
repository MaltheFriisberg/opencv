package Interfaces;

import de.yadrone.base.IARDrone;

import java.awt.image.BufferedImage;

/**
 * Created by malthe on 4/4/17.
 */
public interface IDroneState {
    boolean searchQR(BufferedImage image, IARDrone drone);
    boolean searchRing(BufferedImage image, IARDrone drone);
    boolean approach(BufferedImage image, IARDrone drone);
    boolean evaluate();
    boolean landing(IARDrone drone);
}
