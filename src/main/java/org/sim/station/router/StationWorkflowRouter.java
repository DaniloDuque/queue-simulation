package org.sim.station.router;

import lombok.NonNull;
import org.sim.engine.EventScheduler;
import org.sim.event.ArrivalEvent;
import org.sim.generator.OrderSizeGenerator;
import org.sim.model.order.Order;

import java.util.Collection;

public class StationWorkflowRouter {
	public static void routeToNextStations(@NonNull final Order order, @NonNull final EventScheduler scheduler,
			@NonNull final Double currentTime) {
		final Collection<StationWorkflow> nextStations = order.getChildStationWorkflows();
		for (final StationWorkflow workflow : nextStations) {
			final int orderSize = OrderSizeGenerator.generate();
			for (int i = 0; i < orderSize; i++) {
				final Order newOrder = new Order(order.getId(), order.getStartTime(), workflow);
				scheduler.schedule(new ArrivalEvent(currentTime, newOrder, scheduler));
			}
		}
	}
}
