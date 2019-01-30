package frc.util;

import frc.robot.Robot;

public class Interpolater {
	private final double lockMargin;
	private final double maxVel;

	private double acceleration;
	private double velocity;
	private double location;

	private double target;

	public Interpolater(double acceleration, double maxVel, double lockMargin) {
		this.acceleration = acceleration / 1000;
		this.maxVel = maxVel / 1000;
		velocity = 0;
		location = 0;
		target = 0;

		this.lockMargin = lockMargin;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public void setTarget(double d) {
		target = d;
	}

	public void setLocation(double d) {
		location = d;
	}

	public double getValue(long delta) {		
		if (Math.abs(target - location) < lockMargin) {
			return target;
		}

		if (target > location) {
			double startingSpeed = startingSpeed(0, target - location, -acceleration);
			if (velocity < startingSpeed) {
				velocity += acceleration * delta;
			} else {
				velocity = startingSpeed;
			}
		} else if (target < location) {
			double startingSpeed = startingSpeed(0, location - target, -acceleration);
			if (velocity > startingSpeed) {
				velocity += -acceleration * delta;
			} else {
				velocity = -startingSpeed;
			}
		}

		if (velocity > maxVel) {
			velocity = maxVel;
		} else if (velocity < -maxVel) {
			velocity = -maxVel;
		}

		location += velocity * delta;

		return location;
	}

	public double startingSpeed(double vf, double d, double maxAccel) {
		if ((vf * vf - (2 * maxAccel * d)) < 0) {
			Robot.nBroadcaster.println(vf + "\t" + d + "\t" + maxAccel);
			System.exit(-1);
		}
		return Math.sqrt(vf * vf - (2 * maxAccel * d));
	}
}
