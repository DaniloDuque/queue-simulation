package org.sim.run;

import java.util.Collection;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.station.assignment.StationAssignment;
import org.sim.engine.EventScheduler;
import org.sim.engine.SimulationEngine;
import org.sim.event.Event;
import org.sim.generator.EventGenerator;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class SingleSimulationRunner implements Runnable {
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationAssignment stationAssignment;
	private final EventScheduler eventScheduler;
	private final SimulationEngine engine;

	public void run() {
		Collection<Event> events = eventGenerator.generateEventsUntil(simulationTime, stationAssignment,
				eventScheduler);
		events.forEach(eventScheduler::schedule);
		engine.run(simulationTime);
	}
}
