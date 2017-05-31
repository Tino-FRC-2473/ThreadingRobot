package org.usfirst.frc.team2473.robot;

import java.io.PrintStream;
import java.util.ArrayList;

public class CommandFlusher extends Thread {
	boolean alive;
	ThreadingRobot robot;
	PrintStream out;
	int delay;

	public CommandFlusher(ThreadingRobot bot, PrintStream out) {
		robot = bot;
		this.out = out;
		alive = true;
		super.setDaemon(false);

		if (1000 / robot.getDelay() >= 10) {
			delay = 1000;
		} else {
			delay = robot.getDelay() * 10;
		}
	}

	public void end() {
		alive = false;
	}

	@Override
	public void run() {
		while (alive) {
			ArrayList<String> arr = new ArrayList<>();
			robot.getTempData().drainTo(arr);

			for (int i = 0; i < arr.size(); i++) {
				out.println(arr.get(i));
			}

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}