package org.sim.stat;

public record ConfigurationSummary(
    double averageWaitTime,
    double averageServedClients,
    double minWaitTime,
    double maxWaitTime,
    double waitTimeStdDev,
    int minServedClients,
    int maxServedClients,
    double servedClientsStdDev
) {
}
