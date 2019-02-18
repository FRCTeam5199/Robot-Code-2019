package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;
import frc.controllers.JoystickController;

public class GrabberControl implements LoopModule {
    private final Grabber grabber;
    private final JoystickController Joy;

    private boolean grabberOpen;

    public GrabberControl(Grabber grabber, JoystickController Joy) {
        this.grabber = grabber;
        this.Joy = Joy;
        grabberOpen = false;

    }

    @Override
    public void init() {
        grabberOpen = false;
    }

    @Override
    public void update(long delta) {
        if(Joy.getButtonDown(3) || Joy.getButtonDown(4)){
            grabber.setIntake(.7);
        }
        else if(Joy.getButtonUp(3) || Joy.getButtonUp(4)){
            grabber.setIntake(0);
        }
        if (Joy.getButtonDown(5) || Joy.getButtonDown(6)) {
            grabber.setIntake(-.7);
        }
        else if (Joy.getButtonUp(5) || Joy.getButtonUp(6)){
            grabber.setIntake(0);
        }
        if (Joy.getButtonDown(1) && grabberOpen){
            grabber.setGrabber(true);
            grabberOpen = true;
        }
        else if (Joy.getButtonUp(1)){
            grabber.setGrabber(false);
            grabberOpen = true;
        }
        grabber.setPokers(Joy.getButton(2));
}
}