package org.sim.optimizer.budget;

import java.util.List;

import org.sim.stat.multiple.TestResult;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class OptimizerResult {
	private final Double optimizedValue;
	private final List<TestResult> bestResults;

	public OptimizerResult(Double optimized, List<TestResult> results) {
		assert (results.size() == 3);
		this.optimizedValue = optimized;
		this.bestResults = results;
	}

}
