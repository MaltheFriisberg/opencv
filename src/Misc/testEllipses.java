package Misc;

import Util.ImageViewer;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Elipsedetection.ElipseDetector.detectAndShowElipses;

/**
 * Created by malthe on 4/3/17.
 */
public class testEllipses {
    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static void main(String[] args) {
        ImageViewer viewer = new ImageViewer();
        String filepath = "Resources/ellipses/ellipses.gif";

        BufferedImage image;

        try {
            image = ImageIO.read(new File(filepath));
            viewer.show(detectAndShowElipses(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
