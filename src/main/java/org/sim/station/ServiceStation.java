package org.sim.station;

import java.util.Collection;
import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.LeaveEvent;
import org.sim.model.Order;
import org.sim.stat.StatisticsCollector;

@Slf4j
@AllArgsConstructor
public class ServiceStation {
	private final int workers;
	private final StationName stationName;
	private final ServiceTimeDistribution dist;
	private final Queue<Order> queue;
	private final StatisticsCollector statisticsCollector;

	private int busyWorkers;

	public ServiceStation(@NonNull final StationSpecification stationSpecification, final int workers) {
		this.workers = workers;
		this.stationName = stationSpecification.name();
		this.dist = stationSpecification.dist();
		this.queue = stationSpecification.queue();
		this.statisticsCollector = stationSpecification.statisticsCollector();
	}

	public void arrive(@NonNull final Order order, @NonNull final SimulationEngine engine) {
		order.setQueueStartTime(engine.now());
		if (busyWorkers < workers) {
			startService(order, engine);
		} else {
			queue.add(order);
		}
	}

	public void leave(@NonNull final Order order, @NonNull final SimulationEngine engine) {

		final double serviceTime = engine.now() - order.getServiceStartTime();
		order.addWaitingTimeInService(serviceTime);

		// send event to the next station in the sequence
		final Collection<StationWorkflow> nextStations = order.getChildStationWorkflows();
		if (!nextStations.isEmpty()) {
			for (final StationWorkflow stationWorkflow : nextStations) {
				final Order newOrder = new Order(order.getId(), stationWorkflow);
				final ServiceStation nextStation = stationWorkflow.getCurrentStation();
				nextStation.arrive(newOrder, engine);
			}
		} else {
			statisticsCollector.addServedOrder(order);
		}

		if (queue.isEmpty()) {
			busyWorkers--;
		} else {
			startService(queue.poll(), engine);
		}
	}

	private double serviceTimeForOrders(final int numberOfOrders) {
		double serviceTime = 0.0;
		for (int i = 0; i < numberOfOrders; i++) {
			serviceTime += dist.sample();
		}
		return serviceTime;
	}

	private void startService(@NonNull final Order order, @NonNull final SimulationEngine engine) {
		final double currentTime = engine.now();

		// Calculate queue waiting time
		final double queueTime = currentTime - order.getQueueStartTime();
		order.addWaitingTimeInQueue(queueTime);

		order.setServiceStartTime(currentTime); // Track service start
		busyWorkers++;
		final int numberOfOrders = order.getOrderSizeForCurrentStation();
		final double serviceTime = serviceTimeForOrders(numberOfOrders);
		final double leaveTime = serviceTime + currentTime;
		engine.schedule(new LeaveEvent(leaveTime, order, engine));
	}

}
