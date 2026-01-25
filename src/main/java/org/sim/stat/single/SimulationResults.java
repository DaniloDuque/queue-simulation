package org.sim.stat.single;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.StationName;

import java.util.Collection;

public record SimulationResults(
    @NonNull Double averageWaitTime,
    @NonNull Integer numberOfServedClients,
    @NonNull Collection<Double> waitTimes,
    @NonNull ImmutableMap<StationName, Collection<Double>> stationServiceTimes
) {}
