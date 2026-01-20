package org.sim.assignment;

import org.sim.station.ServiceStation;
import org.sim.station.StationName;

import java.util.Map;

public record StationAssignment(
    Map<StationName, ServiceStation> stations,
    Map<StationName, Integer> workerCounts
) {
}
