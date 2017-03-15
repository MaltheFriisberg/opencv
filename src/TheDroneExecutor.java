/**
 * Created by malthe on 3/15/17.
 */
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.navdata.BatteryListener;


public class TheDroneExecutor {

    public static void main(String[] args) throws InterruptedException
    {
        IARDrone drone = null;
        try
        {
            drone = new ARDrone();
            drone.start();

            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());




        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
        finally
        {
            if (drone != null)
            {
                //VideoListener listener = new VideoListener(drone);

                CommandManager cmd = drone.getCommandManager();
                int speed = 5; // percentage of max speed

                cmd.takeOff().doFor(5000);
                cmd.hover().doFor(10000);

                cmd.goLeft(speed).doFor(1000);
                cmd.hover().doFor(10000);

                cmd.goRight(speed).doFor(1000);
                cmd.hover().doFor(10000);

                cmd.forward(speed).doFor(2000);
                cmd.hover().doFor(10000);

                cmd.backward(speed).doFor(2000);
                cmd.hover().doFor(10000);



                cmd.landing();
            }
            drone.stop();
            System.exit(0);
        }
    }

}
