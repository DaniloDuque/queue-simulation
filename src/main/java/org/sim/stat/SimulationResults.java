package org.sim.stat;

import java.util.Collection;

public record SimulationResults(double averageWaitTime, int numberOfServedClients, Collection<Double> waitTimes) {
}
