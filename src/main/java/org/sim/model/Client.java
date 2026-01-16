package org.sim.model;

import java.util.Collection;

import org.sim.station.ServiceStation;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Client {

    private final long id;
    private final double arrivalTime;

    @Setter
    private Collection<ServiceStation> stationSequence;

    @Setter
    private double totalWaitingTime = 0.0;
}
