package org.sim.station.assignment;

import lombok.NonNull;
import org.sim.station.distribution.ServiceTimeDistribution;


public record StationSpecification(@NonNull ServiceTimeDistribution dist) {
}
