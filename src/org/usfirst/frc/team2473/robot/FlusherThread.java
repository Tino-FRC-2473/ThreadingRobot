package org.usfirst.frc.team2473.robot;

import java.io.PrintStream;

public class FlusherThread extends Thread {
	
	boolean alive;
	PrintStream out;
	
	public FlusherThread(PrintStream out) {
		alive = true;
		this.out = out;
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
	
	public void execute() {
		
	}
	
	public void serverPrint(String s) {
		out.println(s);
	}
}