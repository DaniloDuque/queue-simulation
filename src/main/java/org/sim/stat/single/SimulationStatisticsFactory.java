package org.sim.stat.single;

import org.sim.model.client.ClientRecord;
import org.sim.model.client.ClientRecordFactory;

import java.util.LinkedList;

public class SimulationStatisticsFactory {
	public static SimulationStatistics create() {
		final ClientRecord clientRecord = ClientRecordFactory.create();
		return new SimulationStatistics(new LinkedList<>(), clientRecord);
	}
}
