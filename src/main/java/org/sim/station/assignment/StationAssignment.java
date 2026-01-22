package org.sim.station.assignment;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sim.stat.single.SimulationStatistics;
import org.sim.station.Station;
import org.sim.station.StationName;

@Getter
@AllArgsConstructor
public class StationAssignment {
	private final SimulationStatistics simulationStatistics;
	private final ImmutableMap<StationName, Station> stations;
}
