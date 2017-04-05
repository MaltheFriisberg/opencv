package QRcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.awt.image.BufferedImage;

/**
 * Created by simon on 4/5/17.
 */
public class QRCodeScanner {
    private static long imageCount = 0;

    @SuppressWarnings("Duplicates")
    public static Result scanForQRCode(BufferedImage image) {
        Result scanResult;
        if ((++imageCount % 10) == 0) {
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
                System.out.println(scanResult.getText());
                return scanResult;
            }
            //gui.onTag(scanResult, (long) theta);
        }
        return null;
    }
}
