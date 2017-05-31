package org.usfirst.frc.team2473.robot;

public class UpdaterThread extends Thread {
	boolean alive;

	public UpdaterThread() {
		alive = true;
	}
	
	@Override
	public void run() {
		while(alive) {
			execute();
		}
	}
	
	public void end() {
		alive = false;
		
	}

	//METHOD TO BE OVERRIDDEN
	public void execute() {
		
	}
}