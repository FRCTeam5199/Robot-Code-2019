package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;
import frc.controllers.JoystickController;

public class GrabberControl implements LoopModule {
    private final Grabber grabber;
    private final JoystickController Joy;

    private boolean grabberOut, pokersOut;

    public GrabberControl(Grabber grabber, JoystickController Joy) {
        this.grabber = grabber;
        this.Joy = Joy;
        grabberOut = false;
        pokersOut = false;
    }

    @Override
    public void init() {
        grabberOut = false;
    }

    @Override
    public void update(long delta) {
        if(Joy.getButtonDown(5) || Joy.getButtonDown(6)){
            grabber.setIntake(1);
        }
        else if(Joy.getButtonUp(5) || Joy.getButtonUp(6)){
            grabber.setIntake(0);
        }
        if (Joy.getButtonDown(3) || Joy.getButtonDown(4)) {
            grabber.setIntake(-1);
        }
        else if (Joy.getButtonUp(3) || Joy.getButtonUp(4)){
            grabber.setIntake(0);
        }
        if (Joy.getButtonDown(1)){

            if (!grabberOut && !pokersOut) {
                grabber.setGrabber(true);
                grabberOut = true;
            } else if (grabberOut && !pokersOut) {
                grabber.setPokers(true);
                pokersOut = true;

                grabber.setGrabber(false);
                grabberOut = false;
            } else {
                grabber.setPokers(false);
                pokersOut = false;
                
                grabber.setGrabber(false);
                grabberOut = false;
            }

            
        }
        //else if (Joy.getButtonUp(1)){
        //  grabber.setGrabber(false);
        //  grabberOut = true;
        //  grabber.setPokers(true);
        //}
        //grabber.setPokers(Joy.getButton(2));
}
}