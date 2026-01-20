package org.sim.stat;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
public class TestResultsAnalyzer {
	private final ConcurrentLinkedDeque<SimulationResults> resultsList;

	public TestResultsAnalyzer() {
		this.resultsList = new ConcurrentLinkedDeque<>();
	}

	public void addResults(@NonNull final SimulationResults results) {
		resultsList.add(results);
	}

	public ConfigurationSummary getResults() {
		double minWaitTime = Double.MAX_VALUE;
		double maxWaitTime = Double.MIN_VALUE;
		int maxNumberOfServedClients = Integer.MIN_VALUE;
		int minNumberOfServedClients = Integer.MAX_VALUE;
		double sumOfAverageWaitTimes = 0;
		int sumOfServedClients = 0;

		for (final SimulationResults results : resultsList) {
			minNumberOfServedClients = Math.min(minNumberOfServedClients, results.numberOfServedClients());
			maxNumberOfServedClients = Math.max(maxNumberOfServedClients, results.numberOfServedClients());
			minWaitTime = Math.min(minWaitTime, results.averageWaitTime());
			maxWaitTime = Math.max(maxWaitTime, results.averageWaitTime());
			sumOfAverageWaitTimes += results.averageWaitTime();
			sumOfServedClients += results.numberOfServedClients();
		}

		double meanWaitTime = sumOfAverageWaitTimes / resultsList.size();
		double meanNumberOfServedClients = (double) sumOfServedClients / resultsList.size();

		double varianceInWaitTime = 0;
		double varianceInNumberOfServedClients = 0;

		for (final SimulationResults results : resultsList) {
			varianceInWaitTime += Math.pow(results.averageWaitTime() - meanWaitTime, 2);
			varianceInNumberOfServedClients += Math.pow(results.numberOfServedClients() - meanNumberOfServedClients, 2);
		}

		varianceInWaitTime /= resultsList.size();
		varianceInNumberOfServedClients /= resultsList.size();

		return new ConfigurationSummary(meanWaitTime, meanNumberOfServedClients,
				minWaitTime, maxWaitTime, Math.sqrt(varianceInWaitTime),
				minNumberOfServedClients, maxNumberOfServedClients, Math.sqrt(varianceInNumberOfServedClients));
	}
}
