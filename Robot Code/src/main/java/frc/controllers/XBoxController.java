package frc.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class XBoxController {
	private Joystick stick;

	public XBoxController(int n) {
		stick = new Joystick(n);
	}

	public double getStickLX() {
		if (Math.abs(stick.getRawAxis(0)) > .1){
			return -stick.getRawAxis(0);
		}
		else{
			return 0;
		}
	}

	public double getStickLY() {
		if (Math.abs(stick.getRawAxis(1)) > .1){
			return -stick.getRawAxis(1);
		}
		else{
			return 0;
		}
	}

	public double getStickRX() {
		if (Math.abs(stick.getRawAxis(4)) > .1){
			return -stick.getRawAxis(4);
		}
		else{
			return 0;
		}
	}

	public double getStickRY() {
		if (Math.abs(stick.getRawAxis(5)) > .1){
			return -stick.getRawAxis(5);
		}
		else{
			return 0;
		}
	}

	public double getLTrigger() {
		return stick.getRawAxis(2);
	}

	public double getRTrigger() {
		return stick.getRawAxis(3);
	}

	public boolean getButton(int n) {
		return stick.getRawButton(n);
	}

	public void setLRumble(double n) {
		stick.setRumble(RumbleType.kLeftRumble, n);
	}

	public void setRRumble(double n) {
		stick.setRumble(RumbleType.kRightRumble, n);
	}
}
