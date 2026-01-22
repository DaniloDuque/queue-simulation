package org.sim.engine;

import org.sim.event.Event;

public interface EventProvider {
	Event nextEvent();
}
