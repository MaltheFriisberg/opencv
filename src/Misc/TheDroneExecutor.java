package Misc; /**
 * Created by malthe on 3/15/17.
 */


import Statemachine.DroneAutoController;
import de.yadrone.base.ARDrone;


public class TheDroneExecutor {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws InterruptedException
    {
        //PaperChase pc = new PaperChase();
        ARDrone drone = new ARDrone();
        DroneAutoController droneAutoController = new DroneAutoController(drone);
        droneAutoController.startStateMachine();

        /*
        try
        {
            drone.startStateMachine();

            System.out.println("the drone is connected = " + drone.getNavDataManager().isConnected());
            //Remember to Toggle the camera on
            drone.toggleCamera();
            drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
            drone.getVideoManager().connect(1337);
            System.out.println("drone is connected : "+drone.getVideoManager().connect(1337));
        }
        catch (Exception exc)
        {
            System.out.println("exception");
            exc.printStackTrace();
        }
        finally
        {
            if (drone != null  )
            {

            //

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
            //drone.stop();
            //System.exit(0);
        }
        */
    }

}
