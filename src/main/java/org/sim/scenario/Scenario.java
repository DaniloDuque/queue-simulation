package org.sim.scenario;

import org.sim.optimizer.OptimizerResult;

import java.util.Collection;

public interface Scenario {
	Collection<OptimizerResult> test();
}
