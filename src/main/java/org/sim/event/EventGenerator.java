package org.sim.event;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import org.sim.engine.SimulationEngine;
import org.sim.station.StationRouter;
import org.sim.order.Order;
import org.sim.station.StationWorkflow;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class EventGenerator {
	private final StationRouter stationRouter;
	private final ExponentialDistribution exponentialDistribution;
	private final SimulationEngine simulationEngine;

	public Collection<Event> generateEventsUntil(final double untilTime) {
		final List<Event> eventSequence = new ArrayList<>();
		double currentTime = 0;
		int clientId = 0;

		while (currentTime <= untilTime) {
			double interArrivalTime = exponentialDistribution.sample();
			currentTime += interArrivalTime;
			eventSequence.add(generateEvent(clientId++, currentTime));
		}

		log.info("Generated {} arrival events", eventSequence.size());
		log.info("Last arrival at time: {}", eventSequence.getLast().time());
		log.info("Target time: {}", untilTime);

		return eventSequence;
	}

	private Event generateEvent(final int clientId, final double currentTime) {
		final StationWorkflow stationWorkflow = stationRouter.getStationWorkflow();
		final Order order = new Order(clientId, stationWorkflow);

		return new ArrivalEvent(currentTime, order, simulationEngine);
	}

}
