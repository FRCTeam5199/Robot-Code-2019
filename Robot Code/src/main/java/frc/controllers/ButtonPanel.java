package frc.controllers;
import edu.wpi.first.wpilibj.Joystick;

public class ButtonPanel{
	private final Joystick button;

	public ButtonPanel(int n) {
		button = new Joystick(n);
	}
	public boolean getButton(int n) {
		return button.getRawButton(n);
	}

	public boolean getButtonDown(int n) {
		return button.getRawButtonPressed(n);
	}

	public boolean getButtonUp(int n){
		return button.getRawButtonReleased(n);
	}
}