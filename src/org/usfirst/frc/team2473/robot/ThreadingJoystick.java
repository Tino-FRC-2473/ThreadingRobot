package org.usfirst.frc.team2473.robot;

import java.security.InvalidParameterException;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public class ThreadingJoystick extends Joystick {

	String ref;
	DoubleSupplier value;

	public ThreadingJoystick(int port, int numAxisTypes, int numButtonTypes, String ref, String value) {
		super(port, numAxisTypes, numButtonTypes);
		this.ref = ref;
		this.value = getAsDoubleSupplier(value);
	}

	public ThreadingJoystick(int port, String ref, String value) {
		super(port);
		this.ref = ref;
		this.value = getAsDoubleSupplier(value);
	}

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @return the value
	 */
	public DoubleSupplier getValue() {
		return value;
	}

	private DoubleSupplier getAsDoubleSupplier(String supplier) {
		DoubleSupplier returner;
		if (supplier.equals("X")) {
			returner = () -> getX();
		} else if (supplier.equals("Y")) {
			returner = () -> getY();
		} else if (supplier.equals("Z")) {
			returner = () -> getZ();
		} else if (supplier.equals("throttle")) {
			returner = () -> getThrottle();
		} else if (supplier.equals("twist")) {
			returner = () -> getTwist();
		} else {
			throw new InvalidParameterException("The supplier has to be x, y, or z.");
		}
		return returner;
	}

}
