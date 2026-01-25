package org.sim.optimizer;

import java.util.List;

import lombok.NonNull;
import org.sim.stat.multiple.TestResult;
import org.sim.module.Constants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class OptimizerResult {
	private final List<Double> optimizedValues;
	private final List<TestResult> bestResults;

	public OptimizerResult(@NonNull final List<Double> optimized, @NonNull final List<TestResult> results) {
		assert (results.size() == Constants.NUMBER_OF_COMBINATIONS_EXPECTED
				&& optimized.size() == Constants.NUMBER_OF_COMBINATIONS_EXPECTED);

		this.optimizedValues = optimized;
		this.bestResults = results;
	}

}
