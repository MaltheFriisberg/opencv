package Misc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by malthe on 6/9/17.
 */
public class testGUI {
    public static void main(String[] args) {

        BufferedImage image;
        ImageIcon imageIcon;
        String imagepath = "Resources/pictures720p/image200.jpg";



        JFrame frame;
        JPanel topPanel;
        JPanel middlepanel;
        JPanel bottomPanel;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.add(new JLabel("Top Panel"));

        middlepanel = new JPanel();
        //middlepanel.add(new JLabel("Middle Panel"));

        bottomPanel = new JPanel();

        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(new JLabel("Orange"));
        bottomPanel.add(new JLabel("Green"));
        //middlepanel.add(new ImageIcon(image));
        try {
            image = ImageIO.read(new File(imagepath));
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            middlepanel.add(imageLabel);

        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(middlepanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(1280,900);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
