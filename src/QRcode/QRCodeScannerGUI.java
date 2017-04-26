package QRcode;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import de.yadrone.apps.paperchase.PaperChase;
import de.yadrone.apps.paperchase.TagListener;
import de.yadrone.base.IARDrone;
import de.yadrone.base.navdata.ControlState;
import de.yadrone.base.navdata.DroneState;
import de.yadrone.base.navdata.StateListener;
import de.yadrone.base.video.ImageListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Created by malthe on 4/1/17.
 */
public class QRCodeScannerGUI extends JFrame implements ImageListener, TagListener
{


    private int counterForImage = 0;

    private BufferedImage image = null;
    private Result result;
    private String orientation;

    private String[] shredsToFind = new String[] {"Shred 1", "Shred 2"};
    private boolean[] shredsFound = new boolean[] {false, false};

    private JPanel videoPanel;

    private java.util.Timer timer = new java.util.Timer();
    private long gameStartTimestamp = System.currentTimeMillis();
    private String gameTime = "0:00";

    private boolean gameOver = false;

    public QRCodeScannerGUI()
    {
        super("");



        //createMenuBar();

        setSize(PaperChase.IMAGE_WIDTH, PaperChase.IMAGE_HEIGHT);
        setVisible(true);
        setResizable(false);

        setLayout(new GridBagLayout());

        add(createVideoPanel(), new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

        pack();
    }



    private JPanel createVideoPanel()
    {
        videoPanel = new JPanel() {

            private Font tagFont = new Font("SansSerif", Font.BOLD, 14);
            private Font timeFont = new Font("SansSerif", Font.BOLD, 18);
            private Font gameOverFont = new Font("SansSerif", Font.BOLD, 36);

            public void paint(Graphics g)
            {
                if (image != null)
                {
                    // now draw the camera image
                    g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

                    // draw "Shreds to find"
                    g.setColor(Color.RED);
                    g.setFont(tagFont);
                    g.drawString("Shreds to find", 10, 20);
                    for (int i=0; i < shredsToFind.length; i++)
                    {
                        if (shredsFound[i])
                            g.setColor(Color.GREEN.darker());
                        else
                            g.setColor(Color.RED);
                        g.drawString(shredsToFind[i], 30, 40 + (i*20));
                    }

                    // draw tolerance field (rectangle)
                    g.setColor(Color.RED);

                    int imgCenterX = PaperChase.IMAGE_WIDTH / 2;
                    int imgCenterY = PaperChase.IMAGE_HEIGHT / 2;
                    int tolerance = PaperChase.TOLERANCE;

                    g.drawPolygon(new int[] {imgCenterX-tolerance, imgCenterX+tolerance, imgCenterX+tolerance, imgCenterX-tolerance},
                            new int[] {imgCenterY-tolerance, imgCenterY-tolerance, imgCenterY+tolerance, imgCenterY+tolerance}, 4);

                    // draw triangle if tag is visible
                    if (result != null)
                    {
                        ResultPoint[] points = result.getResultPoints();
                        ResultPoint a = points[1]; // top-left
                        ResultPoint b = points[2]; // top-right
                        ResultPoint c = points[0]; // bottom-left
                        ResultPoint d = points.length == 4 ? points[3] : points[0]; // alignment point (bottom-right)

                        g.setColor(Color.GREEN);

                        g.drawPolygon(new int[] {(int)a.getX(),(int)b.getX(),(int)d.getX(),(int)c.getX()},
                                new int[] {(int)a.getY(),(int)b.getY(),(int)d.getY(),(int)c.getY()}, 4);

                        g.setColor(Color.RED);
                        g.setFont(tagFont);
                        g.drawString(result.getText(), (int)a.getX(), (int)a.getY());
                        g.drawString(orientation, (int)a.getX(), (int)a.getY() + 20);

                        if ((System.currentTimeMillis() - result.getTimestamp()) > 1000)
                        {
                            result = null;
                        }
                    }

                    // draw "Congrats" if all tags have been detected
                    if (gameOver)
                    {
                        String str = "Congratulation !";

                        g.setColor(Color.GREEN.darker());
                        g.setFont(gameOverFont);

                        FontMetrics metrics = g.getFontMetrics(gameOverFont);
                        int hgt = metrics.getHeight();
                        int adv = metrics.stringWidth(str);

                        g.drawString(str, (getWidth() / 2) - (adv / 2), (getHeight() / 2) - (hgt / 2) - 50); // draw text centered
                    }

                    // draw the time
                    g.setColor(Color.RED);
                    g.setFont(timeFont);
                    g.drawString(gameTime, getWidth() - 50, 20);
                }
                else
                {
                    // draw "Waiting for video"
                    g.setColor(Color.RED);
                    g.setFont(tagFont);
                    g.drawString("Waiting for Video ...", 10, 20);
                }
            }
        };
        videoPanel.setSize(PaperChase.IMAGE_WIDTH, PaperChase.IMAGE_HEIGHT);
        videoPanel.setMinimumSize(new Dimension(PaperChase.IMAGE_WIDTH, PaperChase.IMAGE_HEIGHT));
        videoPanel.setPreferredSize(new Dimension(PaperChase.IMAGE_WIDTH, PaperChase.IMAGE_HEIGHT));
        videoPanel.setMaximumSize(new Dimension(PaperChase.IMAGE_WIDTH, PaperChase.IMAGE_HEIGHT));

        return videoPanel;
    }

//	private JPanel createButtonPanel()
//	{
//		JPanel buttonPanel = new JPanel();
//
//		JButton toggleCamButton = new JButton("Toggle Camera");
//		toggleCamButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				drone.toggleCamera();
//			}
//		});
//
//		buttonPanel.add(toggleCamButton);
//
//		return buttonPanel;
//	}

    private long imageCount = 0;

    public void imageUpdated(BufferedImage newImage)
    {
        if ((++imageCount % 2) == 0)
            return;

        image = newImage;

        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                videoPanel.repaint();
            }
        });
    }

    public void onTag(Result result, float orientation)
    {
        if (result != null)
        {
            this.result = result;
            this.orientation = orientation + "�";

            // check if that's a tag (shred) which has not be seen before and mark it as 'found'
            for (int i=0; i < shredsToFind.length; i++)
            {
                if (shredsToFind[i].equals(result.getText()))
                {
                    shredsToFind[i] = shredsToFind[i] + " - " + gameTime;
                    shredsFound[i] = true;
                }
            }

            // now check if all shreds have been found and if so, set the gameOver flag
            boolean isGameOver = true;
            for (int i=0; i < shredsFound.length; i++)
            {
                if (shredsFound[i] == false)
                    isGameOver = false;
            }

            if (isGameOver) // all shreds found ?
            {
                gameOver = true;

            }
        }
    }


}

