package org.sim.stat.single;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.model.client.ClientRecord;
import org.sim.model.order.Order;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SimulationStatistics {
	private final Collection<Order> servedOrders;
	private final ClientRecord clientRecord;

	public void addServedOrder(@NonNull final Order order) {
		servedOrders.add(order);
	}

	public void openClientOrder(@NonNull final Order order) {
		clientRecord.openClientOrder(order.getId());
	}

	public void closeClientOrder(@NonNull final Order order) {
		clientRecord.completeClientOrder(order.getId());
	}

	private Map<Integer, MinMaxPair> getMinMaxPerOrderId() {
		return servedOrders.stream()
				.filter(order -> clientRecord.isClientReady(order.getId()))
				.collect(Collectors.toMap(
						Order::getId,
						order -> new MinMaxPair(order.getStartTime(), order.getEndTime()),
						(existing, replacement) -> new MinMaxPair(
								existing.min(), // startTime is always the same
								Math.max(existing.max(), replacement.max()))));
	}

	private record MinMaxPair(double min, double max) {
	}

	public SimulationResults getSimulationResults() {
		final Map<Integer, MinMaxPair> minMaxPerOrderId = getMinMaxPerOrderId();
		final int numberOfServedClients = minMaxPerOrderId.size();

		final List<Double> waitTimes = minMaxPerOrderId.values().stream()
				.mapToDouble(pair -> pair.max() - pair.min())
				.boxed()
				.toList();

		final double totalWaitTime = waitTimes.stream()
				.mapToDouble(Double::doubleValue)
				.sum();

		final double averageWaitTime = numberOfServedClients > 0 ? totalWaitTime / numberOfServedClients : 0.0;

		return new SimulationResults(averageWaitTime, numberOfServedClients, waitTimes);
	}

}
