package Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public DroneDebugWindow() {

        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.add(new JLabel("Top Panel"));

        middlepanel = new JPanel();
        middlepanel.setSize(1280,720);
        //middlepanel.add(new JLabel("Middle Panel"));

        bottomPanel = new JPanel();

        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(new JLabel("Orange"));
        bottomPanel.add(new JLabel("Green"));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlepanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(1280,900);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void imageUpdated(BufferedImage image, String droneState, String direction) {
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        middlepanel.add(imageLabel);
        frame.pack();
    }
}
