import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by malthe on 4/1/17.
 */
public class testZXing {
    private static ImageViewer viewer = new ImageViewer();
    public static void main(String[] args) {


        BufferedImage image;
        for (int i = 1700; i < 3800; i++) {
            //String imagepath = "Resources/newpictures/billede" + i + ".png";
            String imagepath = "Resources/newpictures/billlede" + i + ".png";
            System.out.println(imagepath);
            try {
                image = ImageIO.read(new File(imagepath));
                viewer.show(image);
                //detectAndShowCircles(image);
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
        
    }
}