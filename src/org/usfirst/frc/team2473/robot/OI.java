package org.usfirst.frc.team2473.robot;

import java.util.ArrayList;

public class OI {
	ThreadingRobot robot;
	private ArrayList<ThreadingJoystick> joyList;
	private ArrayList<ThreadingButton> buttonList;

	public OI(ThreadingRobot bot) {
		robot = bot;
		buttonList = new ArrayList<>();
		joyList = new ArrayList<>();
	}

	public void execute() {
		for (ThreadingButton button : buttonList) {
			button.activate(robot.getDatabase().getButton(button.getRef()));
		}
	}

	public ThreadingJoystick getJoystick(String ref) {
		ThreadingJoystick returner = null;
		for (ThreadingJoystick joy : joyList) {
			if (joy.getRef().equals(ref)) {
				returner = joy;
				break;
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

	public void updateButtons() {
		for (ThreadingButton button : buttonList) {
			robot.getDatabase().setButtonValue(button.getRef(), button.getValue().getAsBoolean());
		}
	}

	public void updateJoysticks() {
		for (ThreadingJoystick joy : joyList) {
			robot.getDatabase().setJoyValue(joy.getRef(), joy.getValue().getAsDouble());
		}
	}
}