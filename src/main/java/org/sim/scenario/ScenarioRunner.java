package org.sim.scenario;

import lombok.extern.slf4j.Slf4j;
import org.sim.optimizer.OptimizerResult;
import org.sim.stat.multiple.TestResult;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ScenarioRunner {

	public static void runAllScenarios() {
		log.info("=== RUNNING ALL SCENARIOS ===");

		// Scenario A: Minimum budget for ≤3 min wait time
		runScenarioA();

		// Scenario B: $2000 budget optimization
		runScenarioB();

		// Scenario C: $3000 budget optimization
		runScenarioC();

		// Scenario D: Reduced cashier service time (2 min)
		runScenarioD();

		// Scenario E: 50% chicken probability
		runScenarioE();
	}

	private static void runScenarioA() {
		log.info("\n=== SCENARIO A: Minimum Budget for ≤3 min Wait Time ===");
		final ScenarioA scenarioA = new ScenarioA();
		final Collection<OptimizerResult> results = scenarioA.test();
		results.forEach(result -> logOptimizerResult("Minimum budget for ≤3 min Wait Time", result));
	}

	private static void runScenarioB() {
		log.info("\n=== SCENARIO B: $2000 Budget Optimization ===");
		final ScenarioB scenarioB = new ScenarioB();
		final Collection<OptimizerResult> results = scenarioB.test();
		results.forEach(result -> logOptimizerResult("$2000 Budget", result));
	}

	private static void runScenarioC() {
		log.info("\n=== SCENARIO C: $3000 Budget Optimization ===");
		final ScenarioC scenarioC = new ScenarioC();
		final Collection<OptimizerResult> results = scenarioC.test();
		results.forEach(result -> logOptimizerResult("$3000 Budget", result));
	}

	private static void runScenarioD() {
		log.info("\n=== SCENARIO D: Reduced Cashier Service Time (2 min) ===");
		final ScenarioD scenarioD = new ScenarioD();
		final Collection<OptimizerResult> results = scenarioD.test();
		results.forEach(result -> logOptimizerResult("Reduced Cashier Time", result));
	}

	private static void runScenarioE() {
		log.info("\n=== SCENARIO E: 50% Chicken Probability ===");
		final ScenarioE scenarioE = new ScenarioE();
		final Collection<OptimizerResult> results = scenarioE.test();
		results.forEach(result -> logOptimizerResult("50% Chicken Probability", result));
	}

	private static void logOptimizerResult(String scenarioName, OptimizerResult result) {
		log.info("{} - Optimized Values: {}", scenarioName, result.getOptimizedValues());
		logTestResults(scenarioName, result.getBestResults());
	}

	private static void logTestResults(String scenarioName, List<TestResult> results) {
		for (int i = 0; i < results.size(); i++) {
			TestResult result = results.get(i);
			log.info("{} - Config {}: Wait Time: {}s, Config: {}",
					scenarioName, i + 1, String.format("%.2f", result.averageWaitTime()), result.workerConfiguration());
		}
	}
}
