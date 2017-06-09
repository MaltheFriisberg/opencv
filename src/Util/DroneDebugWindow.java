package Util;

import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Created by malthe on 6/9/17.
 */
public class DroneDebugWindow {

    private JFrame frame;
    private JPanel topPanel;
    private JPanel middlepanel;
    private JPanel bottomPanel;
    private ImageIcon imageIcon;
    private JLabel imageLabel;
    private JLabel droneStateLabel;
    private JLabel bottomLeftLabel;
    private JLabel bottomRightLabel;

    public DroneDebugWindow() {

        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        imageIcon = new ImageIcon();
        this.bottomLeftLabel = new JLabel();
        this.bottomLeftLabel.setText("Direction : ");
        this.bottomRightLabel = new JLabel();
        imageLabel = new JLabel(this.imageIcon);
        this.droneStateLabel = new JLabel();

        topPanel = new JPanel();
        topPanel.add(new JLabel("Drone State : "));
        topPanel.add(droneStateLabel);

        middlepanel = new JPanel();
        middlepanel.setSize(1280,720);
        //middlepanel.add(new JLabel("Middle Panel"));

        bottomPanel = new JPanel();

        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(this.bottomLeftLabel);
        bottomPanel.add(this.bottomRightLabel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlepanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(1280,900);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void imageUpdated(BufferedImage image, String droneState, String direction) {
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        this.droneStateLabel.setText(droneState);
        //this.bottomLeftLabel.setText(droneState);
        this.bottomRightLabel.setText(direction);

        //imageIcon.setImage(image);
        middlepanel.removeAll();
        middlepanel.add(imageLabel);
        frame.pack();
    }

    public void imageUpdated(Mat image, String droneState, String direction) {
        JLabel imageLabel = new JLabel(new ImageIcon(toBufferedImage(image)));
        this.droneStateLabel.setText(droneState);
        //this.bottomLeftLabel.setText(droneState);
        this.bottomRightLabel.setText(direction);

        //imageIcon.setImage(image);
        middlepanel.removeAll();
        middlepanel.add(imageLabel);
        frame.pack();
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

    public void updateState(String state) {
        this.droneStateLabel.setText(state);
    }

    public void updateDirection(String direction) {
        this.bottomRightLabel.setText(direction);
    }
}
