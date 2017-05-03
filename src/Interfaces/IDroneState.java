package Interfaces;

import de.yadrone.base.IARDrone;

import java.awt.image.BufferedImage;

/**
 * Created by malthe on 4/4/17.
 */
public interface IDroneState {
    void searchQR(BufferedImage image);
    void searchRing(BufferedImage image);
    void approach(BufferedImage image);
    void evaluate();
    void landing();
}
