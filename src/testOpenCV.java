import CircleDetection.CircleDetector;
import Util.ImageViewer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static CircleDetection.CircleDetector.detectAndShowCircles;
import static Util.ImageConverter.bufferedImageToMat;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.rectangle;

/**
 * Created by malthe on 3/8/17.
 */
public class testOpenCV {



    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
    public static ImageViewer viewer = new ImageViewer();
    public static void main(String[] args) {
        Mat mat = new Mat();
        BufferedImage image;
        for (int i = 141; i <643; i++) {
            String imagepath = "Resources/billed/image"+i+".jpg";
            //String imagepath = "Resources/qrcodes/qrcode.png";

            try {
                image = ImageIO.read(new File(imagepath));
                detectAndShowCircles(image, viewer);

                //1 fps pcmasterrace
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }

}





