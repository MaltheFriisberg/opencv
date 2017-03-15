/**
 * Created by malthe on 3/15/17.
 */
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.video.ImageListener;

import java.awt.image.BufferedImage;


public class TheDroneExecutor {

    public static void main(String[] args) throws InterruptedException
    {
        IARDrone drone = null;
        try
        {
            drone = new ARDrone();
            drone.start();
            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            //Remember to Toggle the camera on
            drone.toggleCamera();
            
            //drone.getVideoManager().connect()



        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        finally
        {
            if (drone != null)
            {


                CommandManager cmd = drone.getCommandManager();

                DroneVideoListener listener = new DroneVideoListener(drone);
                /*drone.getVideoManager().addImageListener(new ImageListener() {
                    @Override
                    public void imageUpdated(BufferedImage bufferedImage) {
                        int j = 0;
                        int x = 0;

                    }
                });*/
                //cmd.ca
                int speed = 5; // percentage of max speed

                cmd.hover().doFor(50000);

                cmd.takeOff().doFor(5000);
                //cmd.hover().doFor(10000);

                //cmd.goLeft(speed).doFor(1000);
                //cmd.hover().doFor(10000);

                //cmd.goRight(speed).doFor(1000);
                //cmd.hover().doFor(10000);

                //cmd.forward(speed).doFor(2000);
                //cmd.hover().doFor(10000);

                //cmd.backward(speed).doFor(2000);
                //cmd.hover().doFor(10000);



                cmd.landing();
            }
            drone.stop();
            System.exit(0);
        }
    }

}
