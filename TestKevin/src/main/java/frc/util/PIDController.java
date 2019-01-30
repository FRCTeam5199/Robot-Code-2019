package frc.util;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDController {

	private final String name;
	private double p;
	private double i;
	private double d;
	private double target;
	private double lastPos;
	private double integral;
	private double lastTime;
	private double errorRate;

	public PIDController(double p, double i, double d) {
		this("unnamed pid", p, i, d);
	}

	public PIDController(String name, double p, double i, double d) {
		this.name = name;

		this.p = p;
		this.i = i;
		this.d = d;

		target = 0;
		integral = 0;
	}

	public void reset(double position) {
		lastPos = position;
		lastTime = getTimeS();
		integral = 0;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double update(double position) {
		double tDelta = getTimeS() - lastTime;
		double error = target - position;
		double errorRate = (lastPos - position) / tDelta;

		// Robot.dashboard.putNumber(name + " p*error", p * error);

		integral += error * i * tDelta;

		integral = clamp(integral, 1);

		// Robot.nBroadcaster.println(target + "\t" + error + "\t" + integral +
		// "\t" +
		// errorRate);

		// p = Robot.dashboard.getNumber(name + " p value", p);
		// i = Robot.dashboard.getNumber(name + " i value", i);
		// d = Robot.dashboard.getNumber(name + " d value", d);
		// Robot.dashboard.putNumber(name + " integral", integral);

		Robot.nBroadcaster.println(p * error + "\t" + integral + "\t" + d * errorRate);

		double motorSpeed = p * error + integral + d * errorRate;

		lastPos = position;
		lastTime = getTimeS();
		// Robot.nBroadcaster.println(integral + " " + errorRate + " " +
		// getTimeS());

//		Robot.dashboard.putNumber(name + " motorSpeed", motorSpeed);
		return motorSpeed;
	}

	public void setP(double p) {
		this.p = p;
	}

	public void setI(double i) {
		this.i = i;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getErrorRate() {
		return errorRate;
	}

	// public void putOnDashboard() {
	// Robot.dashboard.putNumber(name + " P", p);
	// Robot.dashboard.putNumber(name + " I", i);
	// Robot.dashboard.putNumber(name + " D", d);
	// }
	//
	// public void getFromDashboard() {
	// p = Robot.dashboard.getNumber(name + " P");
	// i = Robot.dashboard.getNumber(name + " I");
	// d = Robot.dashboard.getNumber(name + " D");
	// }

	private static double getTimeS() {
		return System.currentTimeMillis() / 1000d;
	}

	private static double clamp(double n, double clamp) {
		if (n > clamp) {
			return clamp;
		}
		if (n < -clamp) {
			return -clamp;
		}
		return n;
	}
}
