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
      throw new IllegalArgumentException("Clock cannot go backwards");
    }
    this.currentTime = newTime;
  }
}
