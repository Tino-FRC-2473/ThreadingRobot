package org.usfirst.frc.team2473.robot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class SensorThread extends Thread {
	private ThreadingRobot robot;
	private final int TIME;
	private volatile boolean alive = true;
	private Map<String, Double> doubleTemp;
	private Map<String, DoubleSupplier> doubleCallMap;
	private Map<String, Boolean> booleanTemp;
	private Map<String, BooleanSupplier> booleanCallMap;

	public SensorThread(ThreadingRobot bot, int seconds) {
		robot = bot;
		TIME = seconds;
		instances();
		resetDevices();
	}

	public SensorThread(ThreadingRobot bot) {
		robot = bot;
		TIME = 10;
		instances();
		resetDevices();
	}
	
	public void instances() {
		doubleTemp = new HashMap<>();
		doubleCallMap = Collections.unmodifiableMap(robot.doubleCalls());
		booleanTemp = new HashMap<>();
		booleanCallMap = Collections.unmodifiableMap(robot.booleanCalls());
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
		for (String key : doubleCallMap.keySet()) doubleTemp.put(key, doubleCallMap.get(key).getAsDouble());
		for (String key : doubleTemp.keySet()) robot.getDatabase().setDeviceValue(key, doubleTemp.get(key));
		for (String key : booleanCallMap.keySet()) booleanTemp.put(key, booleanCallMap.get(key).getAsBoolean());
		for (String key : booleanTemp.keySet()) robot.getDatabase().setDeviceValue(key, booleanTemp.get(key));
	}
}