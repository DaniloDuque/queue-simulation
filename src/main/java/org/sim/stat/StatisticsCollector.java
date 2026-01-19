package org.sim.stat;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.client.Client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class StatisticsCollector {
	private final Collection<Client> servedClients;
	private final Map<String, Double> stationQueueTimes;
	private final Map<String, Double> stationServiceTimes;
	private final Map<String, Integer> stationCounts;
	private final Map<String, Integer> stationArrivals;

	public void addServedClient(@NonNull final Client client) {
		servedClients.add(client);
	}

	public void addStationQueueTime(String stationName, double queueTime) {
		stationQueueTimes.put(stationName, stationQueueTimes.get(stationName) == null ? queueTime
				: stationQueueTimes.get(stationName) + queueTime);
		stationCounts.put(stationName, stationCounts.get(stationName) == null ? 1 : stationCounts.get(stationName) + 1);
	}

	public void addStationArrival(String stationName) {
		stationArrivals.put(stationName,
				stationArrivals.get(stationName) == null ? 1 : stationArrivals.get(stationName) + 1);
	}

	public void addStationServiceTime(String stationName, double serviceTime) {
		stationServiceTimes.put(stationName,
				stationServiceTimes.get(stationName) == null ? serviceTime
						: stationServiceTimes.get(stationName) + serviceTime);
	}

	public void printStats() {
		if (servedClients.isEmpty())
			return;

		final double[] queueTimes = servedClients.stream().mapToDouble(Client::getTotalWaitingTimeInQueue).toArray();
		final double[] serviceTimes = servedClients.stream().mapToDouble(Client::getTotalWaitingTimeInService)
				.toArray();

		final double totalQueueTime = Arrays.stream(queueTimes).sum();
		final double totalServiceTime = Arrays.stream(serviceTimes).sum();
		final double totalSystemTime = totalQueueTime + totalServiceTime;

		final double meanQueueTime = totalQueueTime / servedClients.size();
		final double meanServiceTime = totalServiceTime / servedClients.size();
		final double meanSystemTime = totalSystemTime / servedClients.size();

		Arrays.sort(queueTimes);
		Arrays.sort(serviceTimes);

		final double medianQueueTime = getMedian(queueTimes);
		final double maxQueueTime = queueTimes[queueTimes.length - 1];
		final double maxServiceTime = serviceTimes[serviceTimes.length - 1];

		final double utilization = totalServiceTime / (totalServiceTime + totalQueueTime) * 100;

		log.info("\n=== SIMULATION STATISTICS ===");
		log.info("Clients processed: {}", servedClients.size());
		log.info("--- Queue Performance ---");
		log.info("Mean queue time: {}", String.format("%.2f", meanQueueTime));
		log.info("Median queue time: {}", String.format("%.2f", medianQueueTime));
		log.info("Max queue time: {}", String.format("%.2f", maxQueueTime));
		log.info("--- Service Performance ---");
		log.info("Mean service time: {}", String.format("%.2f", meanServiceTime));
		log.info("Max service time: {}", String.format("%.2f", maxServiceTime));
		log.info("--- System Performance ---");
		log.info("Mean total time: {}", String.format("%.2f", meanSystemTime));
		log.info("System utilization: {}%", String.format("%.1f", utilization));

		log.info("--- Per-Station Statistics ---");
		for (String station : stationQueueTimes.keySet()) {
			final double avgQueue = stationQueueTimes.get(station) / stationCounts.get(station);
			final double avgService = stationServiceTimes.get(station) / stationCounts.get(station);
			final int throughput = stationCounts.get(station);
			final int arrivals = stationArrivals.getOrDefault(station, 0);
			log.info("{}: Arrivals {}, Queue {}, Service {}, Completed {}", station, arrivals,
					String.format("%.2f", avgQueue),
					String.format("%.2f", avgService),
					throughput);
		}
	}

	private double getMedian(double[] sortedArray) {
		int n = sortedArray.length;
		return n % 2 == 0 ? (sortedArray[n / 2 - 1] + sortedArray[n / 2]) / 2.0 : sortedArray[n / 2];
	}
}
