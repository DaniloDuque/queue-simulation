package org.sim.stat;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.StationName;

public record ConfigurationResult(
    ImmutableMap<StationName, Integer> workerConfiguration,
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
//        Higher served clients is better (negate for descending order)
//        return Double.compare(other.averageServedClients, this.averageServedClients);
//        final double thisResult = this.averageServedClients / this.averageWaitTime;
//        final double otherResult = other.averageServedClients / other.averageWaitTime;
//        return Double.compare(otherResult, thisResult);
          return Double.compare(this.averageWaitTime, other.averageWaitTime);
    }
}
