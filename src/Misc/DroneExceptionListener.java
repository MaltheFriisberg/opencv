package Misc;

import java.beans.ExceptionListener;

/**
 * Created by Pyke-Laptop on 13-06-2017.
 */
public class DroneExceptionListener implements ExceptionListener {
    @Override
    public void exceptionThrown(Exception e) {
        e.printStackTrace();
    }
}
