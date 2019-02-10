package frc.util;

import frc.robot.Robot;

public class ClockRegulator {
	private long msPerUpdate;
	private long lastTime;

	public ClockRegulator(double frequency) {
		msPerUpdate = (long) (1000 / frequency);
		lastTime = System.currentTimeMillis();
	}

	public void reset() {
		lastTime = System.currentTimeMillis();
	}

	public void sync() {
		long elapsed = System.currentTimeMillis() - lastTime;
		long waitTime = msPerUpdate - elapsed;
		if (waitTime < 0) {
			 Robot.nBroadcaster.println("Can't Keep Up! " + -waitTime + " ms behind!");
		} else {
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lastTime = System.currentTimeMillis();
	}
	
	public long getMsPerUpdate() {
		return msPerUpdate;
	}
}
