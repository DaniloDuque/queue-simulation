package org.sim.station;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.engine.EventScheduler;
import org.sim.station.assignment.StationSpecification;
import org.sim.station.distribution.ServiceTimeDistribution;
import org.sim.event.LeaveEvent;
import org.sim.model.order.Order;
import org.sim.stat.single.SimulationStatistics;
import org.sim.station.queue.StationQueue;
import org.sim.station.router.StationWorkflowRouter;

@Slf4j
@Getter
public class Station {
	private final ServiceTimeDistribution serviceTimeDistribution;
	private final StationQueue stationQueue;
	private final SimulationStatistics simulationStatistics;

	public Station(@NonNull final StationSpecification stationSpecification, @NonNull final StationQueue stationQueue,
			@NonNull final SimulationStatistics simulationStatistics) {
		this.serviceTimeDistribution = stationSpecification.dist();
		this.stationQueue = stationQueue;
		this.simulationStatistics = simulationStatistics;
	}

	public void arrive(@NonNull final Order order, @NonNull final EventScheduler eventScheduler,
			@NonNull final Double currentTime) {
		simulationStatistics.openClientOrder(order);
		if (stationQueue.canServeImmediately()) {
			stationQueue.startService();
			startService(order, eventScheduler, currentTime);
		} else {
			stationQueue.enqueue(order);
		}
	}

	public void leave(@NonNull final Order order, @NonNull final EventScheduler eventScheduler,
			@NonNull final Double currentTime) {
		simulationStatistics.closeClientOrder(order);
		order.setEndTime(currentTime);

		StationWorkflowRouter.routeToNextStations(order, eventScheduler, currentTime);

		if (order.getChildStationWorkflows().isEmpty()) {
			simulationStatistics.addServedOrder(order);
		}

		stationQueue.finishService()
				.ifPresent(nextOrder -> startService(nextOrder, eventScheduler, currentTime));
	}

	private void startService(@NonNull final Order order, @NonNull final EventScheduler eventScheduler,
			@NonNull final Double currentTime) {
		final double serviceTime = serviceTimeDistribution.sample();
		final double leaveTime = serviceTime + currentTime;
		eventScheduler.schedule(new LeaveEvent(leaveTime, order, eventScheduler)); // Current event in service will
																					// leave in leaveTime, so schedule
																					// it's leave event
	}

}
