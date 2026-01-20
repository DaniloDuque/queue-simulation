package org.sim.stat;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.model.Clients;
import org.sim.model.Order;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class StatisticsCollector {
	private final Collection<Order> servedOrders;
	private final Clients clients;

	public void addServedOrder(@NonNull final Order order) {
		servedOrders.add(order);
	}

	private List<Order> getServedOrdersSortedById() {
		return servedOrders.stream()
				.filter(order -> clients.isClientReady(order.getId()))
				.sorted(Comparator.comparing(Order::getId))
				.collect(Collectors.toList());
	}

	// public void printStats() {
	// if (servedOrders.isEmpty())
	// return;
	//
	// final double[] queueTimes =
	// servedOrders.stream().mapToDouble(Order::getTotalWaitingTimeInQueue).toArray();
	// final double[] serviceTimes =
	// servedOrders.stream().mapToDouble(Order::getTotalWaitingTimeInService)
	// .toArray();
	// final double[] totalTimes = servedOrders.stream()
	// .mapToDouble(order -> order.getTotalWaitingTimeInQueue() +
	// order.getTotalWaitingTimeInService())
	// .toArray();
	//
	// final double totalQueueTime = Arrays.stream(queueTimes).sum();
	// final double totalServiceTime = Arrays.stream(serviceTimes).sum();
	// final double totalSystemTime = totalQueueTime + totalServiceTime;
	//
	// final double meanQueueTime = totalQueueTime / servedOrders.size();
	// final double meanServiceTime = totalServiceTime / servedOrders.size();
	// final double meanSystemTime = totalSystemTime / servedOrders.size();
	//
	// // Calculate variance of total waiting time
	// final double meanTotalTime = Arrays.stream(totalTimes).average().orElse(0.0);
	// final double variance = Arrays.stream(totalTimes)
	// .map(time -> Math.pow(time - meanTotalTime, 2))
	// .average().orElse(0.0);
	// final double stdDev = Math.sqrt(variance);
	//
	// Arrays.sort(queueTimes);
	// Arrays.sort(serviceTimes);
	//
	// final double medianQueueTime = getMedian(queueTimes);
	// final double maxQueueTime = queueTimes[queueTimes.length - 1];
	// final double maxServiceTime = serviceTimes[serviceTimes.length - 1];
	//
	// final double utilization = totalServiceTime / (totalServiceTime +
	// totalQueueTime) * 100;
	//
	// log.info("\n=== SIMULATION STATISTICS ===");
	// log.info("Clients processed: {}", servedOrders.size());
	// log.info("--- Queue Performance ---");
	// log.info("Mean queue time: {}", String.format("%.2f", meanQueueTime));
	// log.info("Median queue time: {}", String.format("%.2f", medianQueueTime));
	// log.info("Max queue time: {}", String.format("%.2f", maxQueueTime));
	// log.info("--- Service Performance ---");
	// log.info("Mean service time: {}", String.format("%.2f", meanServiceTime));
	// log.info("Max service time: {}", String.format("%.2f", maxServiceTime));
	// log.info("--- System Performance ---");
	// log.info("Mean total time: {}", String.format("%.2f", meanSystemTime));
	// log.info("Variance of total time: {}", String.format("%.2f", variance));
	// log.info("Std deviation of total time: {}", String.format("%.2f", stdDev));
	// log.info("System utilization: {}%", String.format("%.1f", utilization));
	//
	// log.info("--- Per-Station Statistics ---");
	// for (final StationName station : stationQueueTimes.keySet()) {
	// final Integer count = stationCounts.get(station);
	// if (count == null || count == 0)
	// continue;
	//
	// final double avgQueue = stationQueueTimes.get(station) / count;
	// final Double serviceTime = stationServiceTimes.get(station);
	// final double avgService = serviceTime != null ? serviceTime / count : 0.0;
	// final int arrivals = stationArrivals.getOrDefault(station, 0);
	// log.info("{}: Arrivals {}, Queue {}, Service {}, Completed {}", station,
	// arrivals,
	// String.format("%.2f", avgQueue),
	// String.format("%.2f", avgService),
	// count);
	// }
	// }
	//
	// private double getMedian(final double[] sortedArray) {
	// int n = sortedArray.length;
	// return n % 2 == 0 ? (sortedArray[n / 2 - 1] + sortedArray[n / 2]) / 2.0 :
	// sortedArray[n / 2];
	// }
}
