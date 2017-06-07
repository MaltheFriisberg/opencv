package Misc;

import QRcode.QRCodeScannerGUI;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static QRcode.QRCodeScanner.scanForQRCode;

/**
 * Created by malthe on 4/1/17.
 */
public class testZXing {
    //private static Util.ImageViewer viewer = new Util.ImageViewer();

    public static void main(String[] args) {
        BufferedImage image;
        for (int i = 1700; i < 3700; i++) {

            String imagepath = "Resources/newpictures/billlede" + i + ".png";
            //String imagepath = "Resources/qrcodes/qrcode(1).png";
            try {
                image = ImageIO.read(new File(imagepath));
                scanForQRCode(image);
                //viewer.show(autoControllerImage);
                //1 fps pcmasterrace
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}