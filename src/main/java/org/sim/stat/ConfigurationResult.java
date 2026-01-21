package org.sim.stat;

import java.util.Map;

import lombok.NonNull;
import org.sim.station.StationName;

public record ConfigurationResult(
    Map<StationName, Integer> workerConfiguration,
    double averageWaitTime,
    double averageServedClients,
    double minWaitTime,
    double maxWaitTime,
    double waitTimeStdDev,
    int minServedClients,
    int maxServedClients,
    double servedClientsStdDev
) implements Comparable<ConfigurationResult> {

    @Override
    public int compareTo(@NonNull final ConfigurationResult other) {
        // Higher served clients is better (negate for descending order)
        // return Double.compare(other.averageServedClients, this.averageServedClients);
        return Double.compare(this.averageWaitTime, other.averageWaitTime);
    }
}
