package frc.grabber;

import frc.grabber.Grabber;
import frc.interfaces.LoopModule;
import frc.controllers.JoystickController;

public class GrabberControl implements LoopModule{
    private final Grabber grabber;
	private final JoystickController Joy;

	private boolean grabberOpen;

	public GrabberControl(Grabber grabber, JoystickController Joy) {
		this.grabber = grabber;
		this.Joy = Joy;
		grabberOpen = false;
    }
    
    public void init(){
        
    }

    public void update(long delta){

    }
}