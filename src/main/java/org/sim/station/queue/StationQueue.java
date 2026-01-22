package org.sim.station.queue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.sim.model.order.Order;

import java.util.Optional;
import java.util.Queue;

@AllArgsConstructor
public class StationQueue {
	private final Queue<Order> queue;
	private final int totalWorkers;

	private int busyWorkers = 0;

	public boolean canServeImmediately() {
		return busyWorkers < totalWorkers;
	}

	public void startService() {
		busyWorkers++;
	}

	public Optional<Order> finishService() {
		if (queue.isEmpty()) {
			busyWorkers--;
			return Optional.empty();
		}
		return Optional.of(queue.poll());
	}

	public void enqueue(@NonNull final Order order) {
		queue.add(order);
	}
}
