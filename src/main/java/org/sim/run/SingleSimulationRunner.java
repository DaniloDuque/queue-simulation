package org.sim.run;

import java.util.Collection;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sim.assignment.StationAssignment;
import org.sim.engine.SimulationEngine;
import org.sim.event.Event;
import org.sim.generator.EventGenerator;
import org.sim.stat.single.SimulationStatistics;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class SingleSimulationRunner implements Runnable {
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final StationAssignment stationAssignment;
	private final SimulationEngine engine;
	private final SimulationStatistics simulationStatistics;

	public void run() {
		Collection<Event> events = eventGenerator.generateEventsUntil(simulationTime, stationAssignment, engine,
				simulationStatistics);
		events.forEach(engine::schedule);
		engine.run(simulationTime);
	}
}
