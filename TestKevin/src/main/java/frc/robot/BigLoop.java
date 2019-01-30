package frc.robot;

import java.util.ArrayList;

import frc.interfaces.LoopModule;
import frc.util.ClockRegulator;;

public class BigLoop {
	private final ArrayList<LoopModule> objects;
	private final ClockRegulator clockRegulator;

	public BigLoop(ClockRegulator clockRegulator) {
		objects = new ArrayList<LoopModule>();
		this.clockRegulator = clockRegulator;
	}

	public void update() {
		for (LoopModule o : objects) {
			o.update(clockRegulator.getMsPerUpdate());
		}
		clockRegulator.sync();
	}

	public void init() {
		for (LoopModule o : objects) {
			o.init();
		}
	}

	public void cleanUp() {
		for (LoopModule o : objects){
			o.init();
		}
	}

	public void add(LoopModule o) {
		objects.add(o);
	}

	public void remove(LoopModule o) {
		objects.remove(o);
	}
}
