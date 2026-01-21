package org.sim.stat;

import org.sim.model.ClientRecord;
import org.sim.model.ClientRecordFactory;

import java.util.LinkedList;

public class SimulationStatisticsFactory {
	public static SimulationStatistics create() {
		final ClientRecord clientRecord = ClientRecordFactory.create();
		return new SimulationStatistics(new LinkedList<>(), clientRecord);
	}
}
