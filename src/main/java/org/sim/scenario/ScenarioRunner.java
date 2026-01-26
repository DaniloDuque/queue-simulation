package org.sim.scenario;

import lombok.extern.slf4j.Slf4j;
import org.sim.graph.SimpleGraphGenerator;
import org.sim.optimizer.OptimizerResult;
import org.sim.stat.multiple.TestResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ScenarioRunner {

	public static void runAllScenarios() {
		log.info("=== RUNNING ALL SCENARIOS ===");

		final List<List<TestResult>> allScenarioResults = new ArrayList<>();
		final String[] scenarioNames = { "ScenarioA", "ScenarioB", "ScenarioC", "ScenarioD", "ScenarioE" };

		// Scenario A: Minimum budget for ≤3 min wait time
		allScenarioResults.add(runScenarioA());

		// Scenario B: $2000 budget optimization
		allScenarioResults.add(runScenarioB());

		// Scenario C: $3000 budget optimization
		allScenarioResults.add(runScenarioC());

		// Scenario D: Reduced cashier service time (2 min)
		allScenarioResults.add(runScenarioD());

		// Scenario E: 50% chicken probability
		allScenarioResults.add(runScenarioE());

		// Generate all graphs
		log.info("=== GENERATING GRAPHS FOR ALL SCENARIOS ===");
		for (int i = 0; i < scenarioNames.length; i++) {
			SimpleGraphGenerator.generateGraphs(scenarioNames[i], allScenarioResults.get(i));
		}
	}

	private static List<TestResult> runScenarioA() {
		log.info("\n=== SCENARIO A: Minimum Budget for ≤3 min Wait Time ===");
		final ScenarioA scenarioA = new ScenarioA();
		final Collection<OptimizerResult> results = scenarioA.test();
		final List<TestResult> testResults = new ArrayList<>();

		results.forEach(result -> {
			logOptimizerResult("Minimum budget for ≤3 min Wait Time", result);
			testResults.addAll(result.getBestResults());
		});

		return testResults;
	}

	private static List<TestResult> runScenarioB() {
		log.info("\n=== SCENARIO B: $2000 Budget Optimization ===");
		final ScenarioB scenarioB = new ScenarioB();
		final Collection<OptimizerResult> results = scenarioB.test();
		final List<TestResult> testResults = new ArrayList<>();

		results.forEach(result -> {
			logOptimizerResult("$2000 Budget", result);
			testResults.addAll(result.getBestResults());
		});

		return testResults;
	}

	private static List<TestResult> runScenarioC() {
		log.info("\n=== SCENARIO C: $3000 Budget Optimization ===");
		final ScenarioC scenarioC = new ScenarioC();
		final Collection<OptimizerResult> results = scenarioC.test();
		final List<TestResult> testResults = new ArrayList<>();

		results.forEach(result -> {
			logOptimizerResult("$3000 Budget", result);
			testResults.addAll(result.getBestResults());
		});

		return testResults;
	}

	private static List<TestResult> runScenarioD() {
		log.info("\n=== SCENARIO D: Reduced Cashier Service Time (2 min) ===");
		final ScenarioD scenarioD = new ScenarioD();
		final Collection<OptimizerResult> results = scenarioD.test();
		final List<TestResult> testResults = new ArrayList<>();

		results.forEach(result -> {
			logOptimizerResult("Reduced Cashier Time", result);
			testResults.addAll(result.getBestResults());
		});

		return testResults;
	}

	private static List<TestResult> runScenarioE() {
		log.info("\n=== SCENARIO E: 50% Chicken Probability ===");
		final ScenarioE scenarioE = new ScenarioE();
		final Collection<OptimizerResult> results = scenarioE.test();
		final List<TestResult> testResults = new ArrayList<>();

		results.forEach(result -> {
			logOptimizerResult("50% Chicken Probability", result);
			testResults.addAll(result.getBestResults());
		});

		return testResults;
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
