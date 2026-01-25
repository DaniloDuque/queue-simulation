package org.sim.stat.multiple;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.sim.station.StationName;

import java.util.Collection;

public record TestResult(
        double averageWaitTime,
        double averageServedClients,
        double minWaitTime,
        double maxWaitTime,
        double waitTimeStdDev,
        int minServedClients,
        int maxServedClients,
        double servedClientsStdDev,
        @NonNull ImmutableMap<StationName, Integer> workerConfiguration,
        // New detailed statistics
        double median,
        double variance,
        int mode,
        double q1,
        double q3,
        double p90,
        double p95,
        double p99,
        @NonNull ImmutableMap<String, Double> stationCovariances,
        @NonNull Collection<Double> allWaitTimes
        ) implements Comparable<TestResult> {
    @Override
    public int compareTo(@NonNull final TestResult other) {
        return Double.compare(this.averageWaitTime, other.averageWaitTime);
    }
}
