package testStuff;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.exception.IExceptionListener;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;

/**
 * Created by malthe on 6/6/17.
 */
public class testDrone implements IARDrone {
    @Override
    public CommandManager getCommandManager() {
        return null;
    }

    @Override
    public NavDataManager getNavDataManager() {
        return null;
    }

    @Override
    public VideoManager getVideoManager() {
        return null;
    }

    @Override
    public ConfigurationManager getConfigurationManager() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setHorizontalCamera() {

    }

    @Override
    public void setVerticalCamera() {

    }

    @Override
    public void setHorizontalCameraWithVertical() {

    }

    @Override
    public void setVerticalCameraWithHorizontal() {

    }

    @Override
    public void toggleCamera() {

    }

    @Override
    public void landing() {

    }

    @Override
    public void takeOff() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void forward() {

    }

    @Override
    public void backward() {

    }

    @Override
    public void spinRight() {

    }

    @Override
    public void spinLeft() {

    }

    @Override
    public void up() {

    }

    @Override
    public void down() {

    }

    @Override
    public void goRight() {

    }

    @Override
    public void goLeft() {

    }

    @Override
    public void freeze() {

    }

    @Override
    public void hover() {

    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public void setSpeed(int i) {

    }

    @Override
    public void addSpeedListener(ARDrone.ISpeedListener iSpeedListener) {

    }

    @Override
    public void removeSpeedListener(ARDrone.ISpeedListener iSpeedListener) {

    }


    public void addExceptionListener(IExceptionListener iExceptionListener) {

    }


    public void removeExceptionListener(IExceptionListener iExceptionListener) {

    }

    @Override
    public void setMaxAltitude(int i) {

    }

    @Override
    public void setMinAltitude(int i) {

    }

    @Override
    public void move3D(int i, int i1, int i2, int i3) {

    }
}
