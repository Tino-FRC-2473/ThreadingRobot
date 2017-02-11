package org.usfirst.frc.team2473.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.buttons.Button;

public class Database {
	private ThreadingRobot robot;
	private Map<String, ThreadSafeHolder> deviceMap;
	private Map<String, ThreadSafeInternalButton> buttonMap;
	private Map<String, ThreadSafeHolder> joyMap;
	private final ArrayList<String> device_ids, button_refs, joy_refs;

	public Database(ThreadingRobot bot) {
		robot = bot;
		device_ids = getDeviceIDs();
		button_refs = getButtonRefs();
		joy_refs = getJoyRefs();

		Map<String, ThreadSafeHolder> tempMap = new HashMap<>();
		deviceMap = Collections.synchronizedMap(tempMap);
		fillDeviceMap();
		buttonMap = Collections.synchronizedMap(new HashMap<>());
		fillButtonMap();
		joyMap = Collections.synchronizedMap(new HashMap<>());
		fillJoyMap();

	}

	public ArrayList<String> getDeviceIDs() {
		if (device_ids == null) {
			return robot.setDeviceIDs();
		} else {
			return device_ids;
		}
	}

	public ArrayList<String> getJoyRefs() {
		if (joy_refs == null) {
			return robot.setJoyRefs();
		} else {
			return joy_refs;
		}
	}

	public ArrayList<String> getButtonRefs() {
		if (button_refs == null) {
			return robot.setButtonRefs();
		} else {
			return button_refs;
		}
	}

	private void fillDeviceMap() {
		for (String key : device_ids) {
			deviceMap.put(key, new ThreadSafeHolder());
		}
	}

	private void fillButtonMap() {
		for (String key : button_refs) {
			buttonMap.put(key, new ThreadSafeInternalButton());
		}
	}

	private void fillJoyMap() {
		for (String key : joy_refs) {
			joyMap.put(key, new ThreadSafeHolder());
		}
	}

	public synchronized double getJoyValue(String ref) {
		return joyMap.get(ref).getValue();
	}

	public synchronized void setJoyValue(String ref, double val) {
		joyMap.get(ref).setValue(val);
	}

	public synchronized double getValue(String key) {
		return deviceMap.get(key).getValue();
	}

	public synchronized void setValue(String key, double val) {
		deviceMap.get(key).setValue(val);
	}

	public synchronized Button getButton(String ref) {
		return buttonMap.get(ref);
	}

	public synchronized boolean getButtonValue(String ref) {
		return buttonMap.get(ref).get();
	}

	public synchronized void setButtonValue(String ref, boolean newValue) {
		buttonMap.get(ref).setPressed(newValue);
	}
}
