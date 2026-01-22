package org.sim.engine;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.sim.event.Event;

@AllArgsConstructor
public class SimulationEngine {
	private final SimulationClock clock;
	private final EventProvider eventProvider;

	public void run(@NonNull final Double untilTime) {
		while (true) {
			final Event currentEvent = eventProvider.nextEvent();

			if (currentEvent == null || currentEvent.time() > untilTime)
				break;

			clock.advanceTo(currentEvent.time());
			currentEvent.process();
		}
	}
}
