package org.sim.engine;

import org.sim.event.Event;

public interface EventScheduler {
	void schedule(Event event);
}
