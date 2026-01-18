package org.sim.engine;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public final class SimulationClock {

	private double currentTime = 0.0;

	public double now() {
		return currentTime;
	}

	public void advanceTo(final double newTime) throws IllegalArgumentException {
		if (newTime < currentTime) {
			final String errorMessage = String.format("Clock cannot go backwards. Current time: %f, new time: %f",
					currentTime, newTime);
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		this.currentTime = newTime;
	}
}
