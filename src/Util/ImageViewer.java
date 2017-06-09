package Util;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static Statemachine.DroneAutoController.droneStateText;
import static Statemachine.DroneAutoController.outputText;

/**
 * Created by malthe on 3/10/17.
 */
public class ImageViewer {
    private JLabel imageView;
    private JFrame frame;
    private JTextArea textOutput = new JTextArea();
    private JTextArea stateText = new JTextArea();
    public ImageViewer() {
        createJFrame("");
    }


    public void show(Mat image) {
        show(image,"");
    }
    public void show(Mat image,String windowName){
        setSystemLookAndFeel();
        //JFrame frame = createJFrame(windowName);
        Image loadedImage = toBufferedImage(image);
        imageView.setIcon(new ImageIcon(loadedImage));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void show(BufferedImage image) {
        imageView.setIcon(new ImageIcon(image));
        frame.setLocationRelativeTo(null);
        textOutput.setText(outputText);
        stateText.setSelectedTextColor(Color.yellow);
        //stateText.setText(droneStateText);
        frame.pack();
        frame.setVisible(true);
    }

    private JFrame createJFrame(String windowName) {
        frame = new JFrame(windowName);
        imageView = new JLabel();
        final JScrollPane imageScrollPane = new JScrollPane(imageView);
        imageScrollPane.setPreferredSize(new Dimension(1500, 720));
        frame.add(imageScrollPane, BorderLayout.CENTER);

   //     textOutput.setBounds(1300, 10, 200, 100);
   //     textOutput.setEnabled(true);
     //   textOutput.setVisible(true);

    //    stateText.setBounds(1100, 10, 200, 100);
   //    stateText.setEnabled(true);
    //    stateText.setVisible(true);

  //      frame.add(textOutput);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel
                    (UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public Image toBufferedImage(Mat matrix){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( matrix.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels()*matrix.cols()*matrix.rows();
        byte [] buffer = new byte[bufferSize];
        matrix.get(0,0,buffer); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(),matrix.
                rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().
                getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
