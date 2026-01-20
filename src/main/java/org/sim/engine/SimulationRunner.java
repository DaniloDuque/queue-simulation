package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import org.sim.event.EventGenerator;
import org.sim.module.Constants;
import org.sim.station.StationWorkflow;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationRunner extends Thread {
	private final int numberOfSimulations;
	private final double simulationTime;
	private final EventGenerator eventGenerator;
	private final SimulationEngine engine;
	private final StationWorkflow stationWorkflow;

	@Override
	public void run() {
		final ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);

		for (int i = 0; i < numberOfSimulations; i++) {
			executor.submit(new SingleSimulationRunner(simulationTime, eventGenerator, engine, stationWorkflow));
		}

		executor.shutdown();
	}
}
