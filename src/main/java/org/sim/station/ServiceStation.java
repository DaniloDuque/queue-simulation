package org.sim.station;

import java.util.Collection;
import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.ArrivalEvent;
import org.sim.event.LeaveEvent;
import org.sim.model.Order;
import org.sim.generator.OrderSizeGenerator;
import org.sim.stat.SimulationStatistics;

@Slf4j
@Getter
@AllArgsConstructor
public class ServiceStation {
	private final int workers;
	private final ServiceTimeDistribution dist;
	private final Queue<Order> queue;

	private int busyWorkers = 0;

	public ServiceStation(@NonNull final StationSpecification stationSpecification, final int workers) {
		this.workers = workers;
		this.dist = stationSpecification.dist();
		this.queue = stationSpecification.queue();
	}

	public ServiceStation(@NonNull final ServiceTimeDistribution dist, final int workers,
			@NonNull final Queue<Order> queue) {
		this.workers = workers;
		this.dist = dist;
		this.queue = queue;
		this.busyWorkers = 0;
	}

	public void arrive(@NonNull final Order order, @NonNull final SimulationEngine engine,
			@NonNull final SimulationStatistics simulationStatistics) {
		simulationStatistics.openClientOrder(order);
		if (busyWorkers < workers) {
			startService(order, engine, simulationStatistics);
		} else {
			queue.add(order);
		}
	}

	public void leave(@NonNull final Order order, @NonNull final SimulationEngine engine,
			@NonNull final SimulationStatistics simulationStatistics) {
		simulationStatistics.closeClientOrder(order);
		order.setEndTime(engine.now());

		final Collection<StationWorkflow> nextStations = order.getChildStationWorkflows();

		for (final StationWorkflow stationWorkflow : nextStations) {
			final int orderSize = OrderSizeGenerator.generate();
			for (int i = 0; i < orderSize; i++) {
				final Order newOrder = new Order(order.getId(), order.getStartTime(), stationWorkflow);
				engine.schedule(new ArrivalEvent(engine.now(), newOrder, engine, simulationStatistics));
			}
		}

		if (nextStations.isEmpty()) {
			simulationStatistics.addServedOrder(order);
		}

		if (queue.isEmpty()) {
			busyWorkers--;
		} else {
			startService(queue.poll(), engine, simulationStatistics);
		}
	}

	private void startService(@NonNull final Order order, @NonNull final SimulationEngine engine,
			@NonNull final SimulationStatistics simulationStatistics) {
		busyWorkers++;
		final double serviceTime = dist.sample();
		final double currentTime = engine.now();
		final double leaveTime = serviceTime + currentTime;
		engine.schedule(new LeaveEvent(leaveTime, order, engine, simulationStatistics));
	}

}
