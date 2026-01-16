package org.sim.model;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Client {

    private final long id;
    private final double arrivalTime;

    @Setter
    private double totalWaitingTime = 0.0;
}
