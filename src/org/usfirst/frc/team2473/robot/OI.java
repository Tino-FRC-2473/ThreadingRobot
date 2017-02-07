package org.usfirst.frc.team2473.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class OI {
	ThreadingRobot robot;
	private Map<String, Boolean> buttonMapshot;
	private Map<String, BooleanSupplier> buttonCallMap;
	private Map<String, Double> joyMapshot;
	private Map<String, DoubleSupplier> joyCallMap;
	private ArrayList<ThreadingJoystick> joyList;
	private ArrayList<ThreadingButton> buttonList;

	public OI(ThreadingRobot bot) {
		robot = bot;
		buttonMapshot = new HashMap<>();
		buttonCallMap = new HashMap<>();
		joyMapshot = new HashMap<>();
		joyCallMap = new HashMap<>();
	}

	public void execute() {
		for (ThreadingButton button : buttonList) {
			button.activate(robot.database.getButton(button.getRef()));
		}
	}

	public ThreadingJoystick getJoystick(String ref) {
		ThreadingJoystick returner = null;
		for (ThreadingJoystick joy : joyList) {
			if (joy.getRef().equals(ref)) {
				returner = joy;
			}
		}
		return returner;
	}

	public void setButtons() {
		buttonList = robot.getButtons();
	}

	public void setJoysticks() {
		joyList = robot.getJoysticks();
	}

	public void fillButtonCallMap() {
		for (ThreadingButton button : buttonList) {
			buttonCallMap.put(button.getRef(), button.getValue());
		}
	}

	public void fillJoyCallMap() {
		for (ThreadingJoystick joy : joyList) {
			joyCallMap.put(joy.getRef(), joy.getValue());
		}
	}

	public void updateButtons() {
		for (String ref : buttonCallMap.keySet()) {
			buttonMapshot.put(ref, buttonCallMap.get(ref).getAsBoolean());
		}

		for (String ref : buttonMapshot.keySet()) {
			robot.database.setButtonValue(ref, buttonMapshot.get(ref));
		}
	}

	public void updateJoysticks() {
		for (String ref : joyCallMap.keySet()) {
			joyMapshot.put(ref, joyCallMap.get(ref).getAsDouble());
		}

		for (String ref : joyMapshot.keySet()) {
			robot.database.setJoyValue(ref, joyMapshot.get(ref));
		}
	}
}
