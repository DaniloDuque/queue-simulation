package org.sim.stat.single;

import java.util.Collection;

public record SimulationResults(double averageWaitTime, int numberOfServedClients, Collection<Double> waitTimes) {
}
