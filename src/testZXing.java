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
 * Created by malthe on 4/1/17.
 */
public class testZXing {
    //private static Util.ImageViewer viewer = new Util.ImageViewer();
    private static QRCodeScanner gui = new QRCodeScanner();
    public static void main(String[] args) {


        BufferedImage image;
        for (int i = 0; i < 3700; i++) {

            String imagepath = "Resources/newpictures/billlede" + i + ".png";
            //String imagepath = "Resources/qrcodes/qrcode(1).png";
            try {
                image = ImageIO.read(new File(imagepath));

                updateImage(image);
                gui.imageUpdated(image);
                //viewer.show(image);
                //1 fps pcmasterrace
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static void updateImage(BufferedImage image) {
        long imageCount = 0;
        Result scanResult;
        if ((++imageCount % 2) == 0)
            return;

        // try to detect QR code
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode (if only QR codes are used, the QRCodeReader might be a better choice)
        //MultiFormatReader reader = new MultiFormatReader();
        QRCodeReader reader = new QRCodeReader();
        double theta = Double.NaN;
        try
        {
            scanResult = reader.decode(bitmap);

            ResultPoint[] points = scanResult.getResultPoints();
            ResultPoint a = points[1]; // top-left
            ResultPoint b = points[2]; // top-right

            // Find the degree of the rotation (needed e.g. for auto control)

            double z = Math.abs(a.getX() - b.getX());
            double x = Math.abs(a.getY() - b.getY());
            theta = Math.atan(x / z); // degree in rad (+- PI/2)

            theta = theta * (180 / Math.PI); // convert to degree

            if ((b.getX() < a.getX()) && (b.getY() > a.getY()))
            { // code turned more than 90� clockwise
                theta = 180 - theta;
            }
            else if ((b.getX() < a.getX()) && (b.getY() < a.getY()))
            { // code turned more than 180� clockwise
                theta = 180 + theta;
            }
            else if ((b.getX() > a.getX()) && (b.getY() < a.getY()))
            { // code turned more than 270 clockwise
                theta = 360 - theta;
            }
        }
        catch (ReaderException e)
        {
            // no code found.
            scanResult = null;
        }

        //viewer.show();
        if(scanResult != null)
        System.out.println(scanResult.getText());
        gui.onTag(scanResult, (long)theta);
    }
}