package org.sim.generator;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import org.sim.assignment.StationAssignment;
import org.sim.engine.SimulationEngine;
import org.sim.event.ArrivalEvent;
import org.sim.event.Event;
import org.sim.model.order.Order;
import org.sim.stat.single.SimulationStatistics;
import org.sim.station.StationWorkflow;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class EventGenerator {
	private final ExponentialDistribution exponentialDistribution;

	public Collection<Event> generateEventsUntil(final double untilTime,
			@NonNull final StationAssignment stationAssignment,
			@NonNull final SimulationEngine engine,
			@NonNull final SimulationStatistics simulationStatistics) {
		final List<Event> eventSequence = new ArrayList<>();
		double currentTime = 0;
		int clientId = 0;

		while (currentTime <= untilTime) {
			final double interArrivalTime = exponentialDistribution.sample();
			final StationWorkflow stationWorkflow = StationWorkflowGenerator.generate(stationAssignment);
			currentTime += interArrivalTime;
			eventSequence.add(generateEvent(clientId++, currentTime, stationWorkflow, engine, simulationStatistics));
		}

		return eventSequence;
	}

	private Event generateEvent(final int clientId, final double currentTime,
			@NonNull final StationWorkflow stationWorkflow, @NonNull final SimulationEngine simulationEngine,
			@NonNull final SimulationStatistics simulationStatistics) {
		final Order order = new Order(clientId, currentTime, stationWorkflow);
		return new ArrivalEvent(currentTime, order, simulationEngine, simulationStatistics);
	}

}
