package org.usfirst.frc.team2473.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.buttons.Button;

public class Database {
	private ThreadingRobot robot;
	private Map<String, ThreadSafeDouble> doubleMap;
	private Map<String, ThreadSafeBoolean> booleanMap;
	private Map<String, ThreadSafeInternalButton> buttonMap;
	private Map<String, ThreadSafeDouble> joyMap;
	private final ArrayList<String> device_ids, button_refs, joy_refs;

	public Database(ThreadingRobot bot) {
		robot = bot;
		device_ids = getDeviceIDs();
		button_refs = getButtonRefs();
		joy_refs = getJoyRefs();

		Map<String, ThreadSafeDouble> tempDoubleMap = new HashMap<>();
		doubleMap = Collections.synchronizedMap(tempDoubleMap);
		Map<String, ThreadSafeBoolean> tempBooleanMap = new HashMap<>();
		booleanMap = Collections.synchronizedMap(tempBooleanMap);
		fillDeviceMaps();
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

	private void fillDeviceMaps() {
		for (String key : device_ids) {
			if(robot.callType(key).equals("double")) doubleMap.put(key, new ThreadSafeDouble());
			else if(robot.callType(key).equals("boolean")) booleanMap.put(key, new ThreadSafeBoolean());
		}
	}

	private void fillButtonMap() {
		for (String key : button_refs) {
			buttonMap.put(key, new ThreadSafeInternalButton());
		}
	}

	private void fillJoyMap() {
		for (String key : joy_refs) {
			joyMap.put(key, new ThreadSafeDouble());
		}
	}

	public double getJoyValue(String ref) {
		return joyMap.get(ref).getValue();
	}

	public void setJoyValue(String ref, double val) {
		joyMap.get(ref).setValue(val);
	}

	public double getDeviceValue(String key) {
		return doubleMap.get(key).getValue();
	}

	public boolean getDeviceMode(String key) {
		return booleanMap.get(key).getValue();
	}

	public void setDeviceValue(String key, double val) {
		doubleMap.get(key).setValue(val);
	}
	
	public void setDeviceValue(String key, boolean val) {
		booleanMap.get(key).setValue(val);
	}

	public Button getButton(String ref) {
		return buttonMap.get(ref);
	}

	public boolean getButtonValue(String ref) {
		return buttonMap.get(ref).get();
	}

	public void setButtonValue(String ref, boolean newValue) {
		buttonMap.get(ref).setPressed(newValue);
	}
}