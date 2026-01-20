package org.sim.station;

import java.util.Collection;
import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.ArrivalEvent;
import org.sim.event.LeaveEvent;
import org.sim.model.Order;
import org.sim.model.OrderSizeGenerator;
import org.sim.stat.SimulationStatistics;

@Slf4j
@AllArgsConstructor
public class ServiceStation {
	private final int workers;
	private final ServiceTimeDistribution dist;
	private final Queue<Order> queue;
	private final SimulationStatistics simulationStatistics;

	private int busyWorkers;

	public ServiceStation(@NonNull final StationSpecification stationSpecification, final int workers) {
		this.workers = workers;
		this.dist = stationSpecification.dist();
		this.queue = stationSpecification.queue();
		this.simulationStatistics = stationSpecification.simulationStatistics();
	}

	public void arrive(@NonNull final Order order, @NonNull final SimulationEngine engine) {
		simulationStatistics.openClientOrder(order);
		if (busyWorkers < workers) {
			startService(order, engine);
		} else {
			queue.add(order);
		}
	}

	public void leave(@NonNull final Order order, @NonNull final SimulationEngine engine) {
		simulationStatistics.closeClientOrder(order);

		final Collection<StationWorkflow> nextStations = order.getChildStationWorkflows();
		if (!nextStations.isEmpty()) {
			for (final StationWorkflow stationWorkflow : nextStations) {
				final Order newOrder = new Order(order.getId(), order.getStartTime(), stationWorkflow);
				final int orderSize = OrderSizeGenerator.generate();
				for (int i = 0; i < orderSize; i++) {
					engine.schedule(new ArrivalEvent(engine.now(), newOrder, engine));
				}
			}
		} else {
			simulationStatistics.addServedOrder(order);
		}

		if (queue.isEmpty()) {
			busyWorkers--;
		} else {
			startService(queue.poll(), engine);
		}
	}

	private void startService(@NonNull final Order order, @NonNull final SimulationEngine engine) {
		busyWorkers++;
		final double serviceTime = dist.sample();
		final double currentTime = engine.now();
		final double leaveTime = serviceTime + currentTime;
		engine.schedule(new LeaveEvent(leaveTime, order, engine));
	}

}
