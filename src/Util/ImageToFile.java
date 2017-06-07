package Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by malthe on 4/5/17.
 */
public class ImageToFile {

    public static void saveImageToFile(BufferedImage image) {

        File outputfile = new File("/Resources/QRimages/autoControllerImage.jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
