package frc.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class XBoxController {
	private Joystick stick;
	private double deadzone = 0.07;

	public XBoxController(int n) {
		stick = new Joystick(n);
	}

	public double getStickLX() {
		if (Math.abs(stick.getRawAxis(0)) > deadzone){
			return stick.getRawAxis(0);
		}
		else{
			return 0;
		}
	}

	public double getStickLY() {
		if (Math.abs(stick.getRawAxis(1)) > deadzone){
			return -stick.getRawAxis(1);
		}
		else{
			return 0;
		}
	}

	public double getStickRX() {
		if (Math.abs(stick.getRawAxis(4)) > deadzone){
			return stick.getRawAxis(4);
		}
		else{
			return 0;
		}
	}

	public double getStickRY() {
		if (Math.abs(stick.getRawAxis(5)) > deadzone){
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

	public boolean getButtonDown(int n){
		return stick.getRawButtonPressed(n);
	}

	public boolean getButtonUp(int n){
		return stick.getRawButtonReleased(n);
	}

	public void setLRumble(double n) {
		stick.setRumble(RumbleType.kLeftRumble, n);
	}

	public void setRRumble(double n) {
		stick.setRumble(RumbleType.kRightRumble, n);
	}
}
