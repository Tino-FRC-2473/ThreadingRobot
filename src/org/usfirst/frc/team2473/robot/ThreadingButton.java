package org.usfirst.frc.team2473.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.command.Command;

public class ThreadingButton extends InternalButton {
	private String ref;
	private BooleanSupplier value;
	private Command command;
	private String condition;

	public ThreadingButton(String ref, BooleanSupplier value, String condition, Command command) {
		this.ref = ref;
		this.value = value;
		this.command = command;
	}

	public void activate(Button button) {
		if (condition.equals("held")) {
			button.whileHeld(command);
		} else if (condition.equals("pressed")) {
			button.whenPressed(command);
		} else if (condition.equals("released")) {
			button.whenReleased(command);
		} else if (condition.equals("toggle")) {
			button.toggleWhenPressed(command);
		} else if (condition.equals("cancel")) {
			button.cancelWhenPressed(command);
		}
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
	public BooleanSupplier getValue() {
		return value;
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
}
