package org.usfirst.frc.team2473.robot;

import java.util.Map;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;

public class UpdaterThread extends Thread {
	Map<String, Supplier<Command>> commandMap;
	ThreadingRobot robot;
	boolean alive = true;

	public UpdaterThread(ThreadingRobot bot, Map<String, Supplier<Command>> systems) {
		robot = bot;
		commandMap = systems;
		super.setDaemon(true);
	}

	@Override
	public void run() {
		while (alive) {
			try {
				String str = "";
				for (String key : robot.getDatabase().getDeviceIDs()) {
					str += (robot.getDatabase().getDeviceValue(key) + " ");
				}

				for (String s : commandMap.keySet()) {
					boolean b = commandMap.get(s).get() != null;
					str += (b + " ");
				}
				robot.getTempData().add(str);
				Thread.sleep(robot.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void end() {
		alive = false;
	}
}
