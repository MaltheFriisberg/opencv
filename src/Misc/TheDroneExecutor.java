package Misc; /**
 * Created by malthe on 3/15/17.
 */


import Misc.DroneVideoListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;


public class TheDroneExecutor {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        //PaperChase pc = new PaperChase();
        IARDrone drone = null;
        try
        {
            drone = new ARDrone();
            drone.start();

            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            //Remember to Toggle the camera on
            drone.toggleCamera();
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
            //drone.getVideoManager().connect(1337);
            //System.out.println("drone is connected : "+drone.getVideoManager().connect(1337));
        }
        catch (Exception exc)
        {
            System.out.println("exception");
            exc.printStackTrace();
        }
        finally
        {
            if (drone != null)
            {

//
                CommandManager cmd = drone.getCommandManager();

                //DroneVideoListener listener = new DroneVideoListener(drone);
                //drone.getVideoManager().addImageListener(listener);
                drone.getVideoManager().addImageListener(new ImageListener() {
                    @Override
                    public void imageUpdated(BufferedImage bufferedImage) {
                        int j = 0;
                        int x = 0;

                    }
                });
                //cmd.ca
                int speed = 5; // percentage of max speed

                //cmd.hover().doFor(50000);

                //cmd.takeOff().doFor(5000);
                //cmd.hover().doFor(10000);
                //cmd.hover();
                //cmd.goLeft(speed).doFor(1000);
                //cmd.hover().doFor(10000);

                //cmd.goRight(speed).doFor(1000);
                //cmd.hover().doFor(10000);

                //cmd.forward(speed).doFor(2000);
                //cmd.hover().doFor(10000);

                //cmd.backward(speed).doFor(2000);
                //cmd.hover().doFor(10000);



                //cmd.landing();
            }
            while(true) {

            }
            //drone.stop();
            //System.exit(0);
        }
    }

}
