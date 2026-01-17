package org.sim.station;

import java.util.Queue;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.LeaveEvent;
import org.sim.model.Client;
import org.sim.stat.StatisticsCollector;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class ServiceStation {

    private final int workers;
    private final String name;
    private final ServiceTimeDistribution dist;
    private final Queue<Client> queue;
    private final StatisticsCollector statisticsCollector;

    private int busyWorkers;

    public void arrive(@NonNull final Client client,
                                    @NonNull final SimulationEngine engine) {
        client.setQueueStartTime(engine.now()); // Track when queuing starts
        if (busyWorkers < workers) {
            startService(client, engine);
        } else {
            queue.add(client);
        }
    }

    public void leave(@NonNull final Client client,
                                    @NonNull final SimulationEngine engine) {

        final double serviceTime = engine.now() - client.getServiceStartTime();
        client.addWaitingTimeInService(serviceTime);

        // send event to the next station in the sequence
        final Queue<ServiceStation> clientStationSequence = client.getStationSequence();
        if (!clientStationSequence.isEmpty()) {
            final ServiceStation nextStation = clientStationSequence.poll();
            nextStation.arrive(client, engine);
        } else {
            statisticsCollector.addServedClient(client);
        }

        if (queue.isEmpty()) {
            busyWorkers--;
        } else {
            startService(queue.poll(), engine);
        }
    }

    private void startService(@NonNull final Client client,
                                    @NonNull final SimulationEngine engine) {
        // Calculate queue waiting time
        final double queueTime = engine.now() - client.getQueueStartTime();
        client.addWaitingTimeInQueue(queueTime);

        client.setServiceStartTime(engine.now()); // Track service start
        busyWorkers++;
        final double leaveTime = dist.sample() + engine.now();
        engine.schedule(new LeaveEvent(leaveTime, this, client, engine));
    }

}
