package org.usfirst.frc.team2473.robot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class ThreadingRobot extends IterativeRobot {
	private PrintStream out;
	private int server_port;
	private CommandThread updater;
	private CommandFlusher flush;
	private SensorThread sense;
	private Database database;
	private Map<String, Supplier<Command>> commandsMap;
	private Map<String, Subsystem> subsystemsMap;
	private ArrayList<Server> servers;
	private ArrayList<ThreadingJoystick> joyList;
	private ArrayList<ThreadingButton> buttonList;
	private Map<String, DoubleSupplier> doubleCalls; //stores double values of sensors
	private Map<String, BooleanSupplier> booleanCalls; //stores boolean values of sensors
	private ArrayList<AnalogGyro> gyros;
	private ArrayList<CANTalon> encoders;
	private ArrayBlockingQueue<String> tempData;
	private int delay = 10;
	private OI oi;
	private Timer robotControlLoop;
	private boolean isTimerRunning;
	private boolean usingNetworking;

	@Override
	public void robotInit() {
		gyros = new ArrayList<AnalogGyro>();
		encoders = new ArrayList<CANTalon>();
		joyList = new ArrayList<>();
		buttonList = new ArrayList<>();
		updateJoyList();
		oi = new OI(this);
		oi.setJoysticks();
		updateButtonList();
		oi.setButtons();
		subsystemsMap = new HashMap<>();
		commandsMap = new HashMap<>();
		doubleCalls = new HashMap<>();
		booleanCalls = new HashMap<>();
		updateDeviceCalls();
		database = new Database(this);
		oi.execute();
		sense = new SensorThread(this, delay);
		robotControlLoop = new Timer(false);
		isTimerRunning = false;
		server_port = 8080;

		// code for creating all networking threads

		ServerSocket server;
		if (usingNetworking) {
			try {
				server = new ServerSocket(server_port);
				Socket socket = server.accept();
				OutputStream out_stream = socket.getOutputStream();
				out = new PrintStream(out_stream);
			} catch (IOException e) {
				e.printStackTrace();
			}

			updater = new CommandThread(this, commandsMap);
			flush = new CommandFlusher(this, out);
		}
		addCommandListeners();
		startThreads();
	}

	public double getDeviceValue(String deviceID) {
		return database.getDeviceValue(deviceID);
	}

	protected int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void addDeviceCall(String deviceID, DoubleSupplier valueSupplier) {
		doubleCalls.put(deviceID, valueSupplier);
	}
	
	public void addDeviceCall(String deviceID, BooleanSupplier valueSupplier) {
		booleanCalls.put(deviceID, valueSupplier);
	}

	public void addJoy(int port, String ref, String value) {
		joyList.add(new ThreadingJoystick(port, ref, value));
	}

	public double getJoyValue(String ref) {
		return database.getJoyValue(ref);
	}

	public void addJoy(int port, int numAxisTypes, int numButtonTypes, String ref, String value) {
		joyList.add(new ThreadingJoystick(port, numAxisTypes, numButtonTypes, ref, value));
	}

	protected ArrayList<ThreadingJoystick> getJoysticks() {
		return joyList;
	}

	public ThreadingJoystick getJoystick(String ref) {
		return oi.getJoystick(ref);
	}

	protected ArrayList<String> setJoyRefs() {
		ArrayList<String> returner = new ArrayList<>();
		for (ThreadingJoystick joy : joyList) {
			returner.add(joy.getRef());
		}
		return returner;
	}

	public void addButton(String ref, BooleanSupplier value, String condition, Command command) {
		buttonList.add(new ThreadingButton(ref, value, condition, command));
	}

	public boolean getButtonValue(String ref) {
		return database.getButtonValue(ref);
	}

	protected ArrayList<ThreadingButton> getButtons() {
		return buttonList;
	}

	public Map<String, Subsystem> getSubsystems() {
		return subsystemsMap;
	}

	protected ArrayList<String> setButtonRefs() {
		ArrayList<String> returner = new ArrayList<>();
		for (ThreadingButton button : buttonList) {
			returner.add(button.getRef());
		}
		return returner;
	}

	protected ArrayList<String> setDeviceIDs() {
		ArrayList<String> returner = new ArrayList<>();
		for (String key : doubleCalls.keySet()) returner.add(key);
		for (String key : booleanCalls.keySet()) returner.add(key);
		return returner;
	}

	protected Map<String, DoubleSupplier> doubleCalls() {
		return doubleCalls;
	}

	protected Map<String, BooleanSupplier> booleanCalls() {
		return booleanCalls;
	}
	
	protected String callType(String device_id) {
		if(doubleCalls.keySet().contains(device_id)) return "double";
		else if(booleanCalls.keySet().contains(device_id)) return "boolean";
		else return "none";
	}

	// Method MUST be overridden
	public void setNetworking(boolean b) {
		usingNetworking = b;
	}

	// Method MUST be overridden
	public void updateDeviceCalls() {

	}

	// Method MUST be overridden
	public void updateJoyList() {

	}

	// Method MUST be overriden
	public void updateButtonList() {

	}

	// Method MUST be overriden
	public void addSystems() {
		// subsystemsMap.put("ExampleSubsystem", new ExampleSubsystem());
	}


	protected void resetEncoders() {
		for(CANTalon talon : encoders) resetEnc(talon);			
	}
	
	private void resetEnc(CANTalon talon) {
		talon.changeControlMode(CANTalon.TalonControlMode.Position);
		talon.setPosition(0);
		talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	// Method to override
	protected void resetGyro() {
		for(AnalogGyro gyro : gyros) gyro.reset();
	}

	public void addEncoderDevice(CANTalon talon) {
		encoders.add(talon);
	}

	public void addGyroDevice(AnalogGyro gyro) {
		gyros.add(gyro);
	}

	private void startThreads() {
		sense.start();
		if (usingNetworking) {
			updater.start();
			flush.start();
			for(Server s : servers) s.run();
		}
	}

	private void addCommandListeners() {
		for (String key : subsystemsMap.keySet()) commandsMap.put(key, () -> subsystemsMap.get(key).getCurrentCommand());
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {

		runTeleop();
	}
	
	@Override
	public void testInit() {
		
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	public void setPort(int server) {
		server_port = server;
	}

	public int getPort() {
		return server_port;
	}

	public void addServer(int port) {
		servers.add(new Server(port));
	}
	
	public Server getServer(int port) {
		for(Server s : servers) if(s.getPort()==port) return s;
		return null;
	}

	public Database getDatabase() {
		return database;
	}

	protected ArrayBlockingQueue<String> getTempData() {
		return tempData;
	}

	protected void runTeleop() {
		if (!isTimerRunning) {
			robotControlLoop.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Scheduler.getInstance().run();
					oi.updateButtons();
					oi.updateJoysticks();
				}
			}, 0, 20);
			isTimerRunning = true;
		}
	}
}