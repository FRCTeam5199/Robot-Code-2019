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
        if (Joy.hatUp()) {
            grabber.setIntake(-1);
        } else if (Joy.hatDown()) {
            grabber.setIntake(1);
        } else {
            grabber.setIntake(0);
        }

        if (Joy.getButtonDown(1)) {
            grabberOpen = !grabberOpen;
        }

        grabber.setGrabber(grabberOpen);
    }
}