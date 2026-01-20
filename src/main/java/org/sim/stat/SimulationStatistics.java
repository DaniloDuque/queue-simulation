package org.sim.stat;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.model.Clients;
import org.sim.model.Order;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SimulationStatistics {
	private final Collection<Order> servedOrders;
	private final Clients clients;

	public void addServedOrder(@NonNull final Order order) {
		servedOrders.add(order);
	}

	public void openClientOrder(@NonNull final Order order) {
		clients.openClientOrder(order.getId());
	}

	public void closeClientOrder(@NonNull final Order order) {
		clients.completeClientOrder(order.getId());
	}

	private Map<Integer, MinMaxPair> getMinMaxPerOrderId() {
		return servedOrders.stream()
				.filter(order -> clients.isClientReady(order.getId()))
				.collect(Collectors.toMap(
						Order::getId,
						order -> new MinMaxPair(
								Math.min(order.getStartTime(), order.getEndTime()),
								Math.max(order.getStartTime(), order.getEndTime())),
						(existing, replacement) -> new MinMaxPair(
								Math.min(existing.min(), replacement.min()),
								Math.max(existing.max(), replacement.max()))));
	}

	private record MinMaxPair(double min, double max) {
	}

	public SimulationResults getSimulationResults() {
		final Map<Integer, MinMaxPair> minMaxPerOrderId = getMinMaxPerOrderId();
		final int totalClients = minMaxPerOrderId.size();

		log.info("Total clients: {}", totalClients);
		final double totalWaitTime = minMaxPerOrderId.values().stream()
				.mapToDouble(pair -> pair.max() - pair.min())
				.sum();

		final double averageWaitTime = totalClients > 0 ? totalWaitTime / totalClients : 0.0;

		log.info("Average wait time: {}", averageWaitTime);
		return new SimulationResults(averageWaitTime);
	}

}
