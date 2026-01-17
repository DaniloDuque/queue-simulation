package org.sim.stat;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.model.Client;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
public class StatisticsCollector {
    private final Collection<Client> servedClients;

    public void addServedClient(@NonNull final Client client) {
        servedClients.add(client);
    }

    public void printStats() {
        if (servedClients.isEmpty())
            return;

        double totalQueueTime = servedClients.stream()
                                        .mapToDouble(Client::getTotalWaitingTimeInQueue)
                                        .sum();
        double totalServiceTime = servedClients.stream()
                                        .mapToDouble(Client::getTotalWaitingTimeInService)
                                        .sum();

        double meanQueueTime = totalQueueTime / servedClients.size();
        double meanServiceTime = totalServiceTime / servedClients.size();

        log.info("\n=== SIMULATION STATS ===");
        log.info("Completed clients: {}", servedClients.size());
        log.info("Mean queue waiting time: {}", String.format("%.2f", meanQueueTime));
        log.info("Mean service time: {}", String.format("%.2f", meanServiceTime));
    }
}
