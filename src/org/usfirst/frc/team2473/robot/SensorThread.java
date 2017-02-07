package org.usfirst.frc.team2473.robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class SensorThread extends Thread {
	private ThreadingRobot robot;
	private final int TIME;
	private volatile boolean alive = true;
	private Map<String, Double> temp;
	private Map<String, DoubleSupplier> callMap;

	public SensorThread(ThreadingRobot bot, int seconds) {
		robot = bot;
		TIME = seconds;
		temp = new HashMap<>();
		callMap = robot.deviceCalls();
		callMap = Collections.unmodifiableMap(callMap);
		resetDevices();
	}

	public SensorThread(ThreadingRobot bot) {
		robot = bot;
		TIME = 10;
		temp = new HashMap<>();
		callMap = robot.deviceCalls();
		callMap = Collections.unmodifiableMap(callMap);
		resetDevices();
	}

	@Override
	public void run() {
		while (alive) {
			updateDevices();
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void kill() {
		alive = false;
	}

	public boolean isDead() {
		return !alive;
	}

	public void resetDevices() {
		robot.resetEncoders();
		robot.resetGyro();
	}

	public void updateDevices() {
		for (String key : callMap.keySet()) {
			temp.put(key, callMap.get(key).getAsDouble());//
		}

		for (String key : temp.keySet()) {
			robot.database.setValue(key, temp.get(key));
		}
	}
}