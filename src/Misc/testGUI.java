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
        String imagepath = "Resources/pictures720p/image178.jpg";
        try {
            image = ImageIO.read(new File(imagepath));
            imageIcon = new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        GridLayout layout = new GridLayout(1,2);
        JPanel jPanel = new JPanel(layout);
        //JPanel jPanel = new JPanel(layout);

        jPanel.add( new JButton("asda") );

        frame.getContentPane().add(jPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
