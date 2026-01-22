package org.sim.station.assignment;

import com.google.common.collect.ImmutableMap;
import org.sim.station.StationName;

public record StationConfiguration(
        ImmutableMap<StationName, Integer> workerCountPerStation,
        ImmutableMap<StationName, StationSpecification> stationSpecifications
) {}
