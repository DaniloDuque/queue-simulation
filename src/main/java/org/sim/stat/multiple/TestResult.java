package org.sim.stat.multiple;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.StationName;

public record TestResult(
        double averageWaitTime,
        double averageServedClients,
        double minWaitTime,
        double maxWaitTime,
        double waitTimeStdDev,
        int minServedClients,
        int maxServedClients,
        double servedClientsStdDev,
        @NonNull ImmutableMap<StationName, Integer> workerConfiguration
        ) implements Comparable<TestResult> {
    @Override
    public int compareTo(@NonNull final TestResult other) {
        return Double.compare(this.averageWaitTime, other.averageWaitTime);
    }
}
