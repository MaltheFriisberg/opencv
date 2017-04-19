package Util;

import QRcode.QRCodeScanner;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Pyke-Laptop on 05-04-2017.
 */
public class QRScanner {

    //private static Util.ImageViewer viewer = new Util.ImageViewer();
    private static QRCodeScanner gui = new QRCodeScanner();

    public String getQRCode(BufferedImage image) {
        long imageCount = 0;
        Result scanResult;

        // try to detect QR code
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode (if only QR codes are used, the QRCodeReader might be a better choice)
        //MultiFormatReader reader = new MultiFormatReader();
        QRCodeReader reader = new QRCodeReader();
        double theta = Double.NaN;
        try {
            scanResult = reader.decode(bitmap);

            ResultPoint[] points = scanResult.getResultPoints();
            ResultPoint a = points[1]; // top-left
            ResultPoint b = points[2]; // top-right

            // Find the degree of the rotation (needed e.g. for auto control)

            double z = Math.abs(a.getX() - b.getX());
            double x = Math.abs(a.getY() - b.getY());
            theta = Math.atan(x / z); // degree in rad (+- PI/2)

            theta = theta * (180 / Math.PI); // convert to degree

            if ((b.getX() < a.getX()) && (b.getY() > a.getY())) { // code turned more than 90� clockwise
                theta = 180 - theta;
            } else if ((b.getX() < a.getX()) && (b.getY() < a.getY())) { // code turned more than 180� clockwise
                theta = 180 + theta;
            } else if ((b.getX() > a.getX()) && (b.getY() < a.getY())) { // code turned more than 270 clockwise
                theta = 360 - theta;
            }
        } catch (ReaderException e) {
            // no code found.
            scanResult = null;
        }

        //viewer.show();
        if (scanResult != null) {
            return scanResult.getText();
        } else {
            return "";
        }
    }
}
