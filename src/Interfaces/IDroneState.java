package Interfaces;

import de.yadrone.base.IARDrone;

import java.awt.image.BufferedImage;

/**
 * Created by malthe on 4/4/17.
 */
public interface IDroneState {
    boolean searchQR(BufferedImage image);
    boolean searchRing(BufferedImage image);
    boolean approach(BufferedImage image);
    boolean evaluate();
    boolean landing();
}
