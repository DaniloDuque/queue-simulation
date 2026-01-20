package org.sim.station;

import lombok.NonNull;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.model.Order;
import org.sim.stat.SimulationStatistics;

import java.util.Queue;

public record StationSpecification(@NonNull ServiceTimeDistribution dist, @NonNull Queue<Order>queue, @NonNull SimulationStatistics simulationStatistics) {
}
