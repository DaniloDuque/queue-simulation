package org.sim.engine;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.sim.event.Event;

@AllArgsConstructor
public class EventFifoManager implements EventScheduler, EventProvider {
	private final EventQueue eventQueue;

	@Override
	public void schedule(@NonNull final Event event) {
		eventQueue.schedule(event);
	}

	@Override
	public Event nextEvent() {
		return eventQueue.nextEvent();
	}
}
