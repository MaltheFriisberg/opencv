package Util;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by malthe on 4/2/17.
 */
public class ImageConverter {

    public static Mat bufferedImageToMat(BufferedImage bi) {
        if(bi != null) {
            Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
            byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, data);
            return mat;
        }

        return new Mat();
    }

    public static BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg)
    {
        if ( matrix != null ) {
            int cols = matrix.cols();
            int rows = matrix.rows();
            int elemSize = (int)matrix.elemSize();
            byte[] data = new byte[cols * rows * elemSize];
            int type;
            matrix.get(0, 0, data);
            switch (matrix.channels()) {
                case 1:
                    type = BufferedImage.TYPE_BYTE_GRAY;
                    break;
                case 3:
                    type = BufferedImage.TYPE_3BYTE_BGR;
                    // bgr to rgb
                    byte b;
                    for(int i=0; i<data.length; i=i+3) {
                        b = data[i];
                        data[i] = data[i+2];
                        data[i+2] = b;
                    }
                    break;
                default:
                    return null;
            }

            // Reuse existing BufferedImage if possible
            if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
                bimg = new BufferedImage(cols, rows, type);
            }
            bimg.getRaster().setDataElements(0, 0, cols, rows, data);
        } else { // mat was null
            bimg = null;
        }
        return bimg;
    }
}
