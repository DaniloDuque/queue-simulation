package org.sim.station.queue;

import org.sim.model.order.Order;

import java.util.LinkedList;
import java.util.Queue;

public class StationQueueFactory {
	public static StationQueue create(final int numberOfWorkers) {
		final Queue<Order> queue = new LinkedList<>();
		return new StationQueue(queue, numberOfWorkers, 0);
	}
}
