package org.sim.stat;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.client.Client;

import java.util.Collection;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class StatisticsCollector {
	private final Collection<Client> servedClients;
	private final Map<String, Double> stationQueueTimes;
	private final Map<String, Double> stationServiceTimes;
	private final Map<String, Integer> stationCounts;

	public void addServedClient(@NonNull final Client client) {
		servedClients.add(client);
	}

	public void addStationQueueTime(String stationName, double queueTime) {
		stationQueueTimes.put(stationName, stationQueueTimes.get(stationName) == null ? queueTime
				: stationQueueTimes.get(stationName) + queueTime);
		stationCounts.put(stationName, stationCounts.get(stationName) == null ? 1 : stationCounts.get(stationName) + 1);
	}

	public void addStationServiceTime(String stationName, double serviceTime) {
		stationServiceTimes.put(stationName,
				stationServiceTimes.get(stationName) == null ? serviceTime
						: stationServiceTimes.get(stationName) + serviceTime);
	}

	public void printStats() {
		if (servedClients.isEmpty())
			return;

		double[] queueTimes = servedClients.stream().mapToDouble(Client::getTotalWaitingTimeInQueue).toArray();
		double[] serviceTimes = servedClients.stream().mapToDouble(Client::getTotalWaitingTimeInService).toArray();

		double totalQueueTime = java.util.Arrays.stream(queueTimes).sum();
		double totalServiceTime = java.util.Arrays.stream(serviceTimes).sum();
		double totalSystemTime = totalQueueTime + totalServiceTime;

		double meanQueueTime = totalQueueTime / servedClients.size();
		double meanServiceTime = totalServiceTime / servedClients.size();
		double meanSystemTime = totalSystemTime / servedClients.size();

		java.util.Arrays.sort(queueTimes);
		java.util.Arrays.sort(serviceTimes);

		double medianQueueTime = getMedian(queueTimes);
		double maxQueueTime = queueTimes[queueTimes.length - 1];
		double maxServiceTime = serviceTimes[serviceTimes.length - 1];

		double utilization = totalServiceTime / (totalServiceTime + totalQueueTime) * 100;

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
			double avgQueue = stationQueueTimes.get(station) / stationCounts.get(station);
			double avgService = stationServiceTimes.get(station) / stationCounts.get(station);
			int throughput = stationCounts.get(station);
			log.info("{}: Queue {}, Service {}, Completed {}", station, String.format("%.2f", avgQueue),
					String.format("%.2f", avgService),
					throughput);
		}
	}

	private double getMedian(double[] sortedArray) {
		int n = sortedArray.length;
		return n % 2 == 0 ? (sortedArray[n / 2 - 1] + sortedArray[n / 2]) / 2.0 : sortedArray[n / 2];
	}
}
