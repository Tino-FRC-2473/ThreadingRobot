package org.usfirst.frc.team2473.robot;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

public class Server {
	private UpdaterThread updater;
	private FlusherThread flusher;
	private int port;

	@SuppressWarnings("resource")
	public Server(int port) {
		this.port = port;
		try {
			updater = new UpdaterThread();
			flusher = new FlusherThread(new PrintStream(new ServerSocket(port).accept().getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Server(int port, UpdaterThread updater, FlusherThread flusher) {
		setUpdater(updater);
		setFlusher(flusher);
	}
	
	public void run() {
		updater.start();
		flusher.start();
	}
	
	public void setUpdater(UpdaterThread thread) {
		updater = thread;
	}
	
	public void setFlusher(FlusherThread thread) {
		flusher = thread;
	}
	
	public void end() {
		updater.end();
		flusher.end();
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public UpdaterThread getUpdater() {
		return updater;
	}
	
	public FlusherThread getFlusher() {
		return flusher;
	}
}