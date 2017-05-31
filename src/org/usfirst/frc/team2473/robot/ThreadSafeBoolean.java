package org.usfirst.frc.team2473.robot;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeBoolean {

	private volatile boolean value;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	public boolean getValue() {
		try {
			lock.readLock().lock();
			return value;
		} finally {
			lock.readLock().unlock();
		}

	}

	public void setValue(boolean newValue) {
		try {
			lock.writeLock().lock();
			value = newValue;
		} finally {
			lock.writeLock().unlock();
		}
	}
}