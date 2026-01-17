package org.sim.station;

import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.LeaveEvent;
import org.sim.model.Client;

@Slf4j
@AllArgsConstructor
public class ServiceStation {

    private final int workers;
    private final String name;
    private final ServiceTimeDistribution dist;
    private final Queue<Client> queue;

    private int busyWorkers;

    public void arrive(@NonNull final Client c, @NonNull final SimulationEngine engine) {
        if (busyWorkers < workers) {
            startService(c, engine);
        } else {
            queue.add(c);
        }
    }

    public void leave(@NonNull final Client client,
                                    @NonNull final SimulationEngine engine) {

        // send event to the next station in the sequence
        final Queue<ServiceStation> clientStationSequence = client.getStationSequence();
        if (!clientStationSequence.isEmpty()) {
            final ServiceStation nextStation = clientStationSequence.poll();
            nextStation.arrive(client, engine);
        }

        if (queue.isEmpty()) {
            busyWorkers--;
        } else {
            startService(queue.poll(), engine);
        }
    }

    private void startService(@NonNull final Client client,
                                    @NonNull final SimulationEngine engine) {
        busyWorkers++;
        final double leaveTime = dist.sample() + engine.now();
        engine.schedule(new LeaveEvent(leaveTime, this, client, engine));
    }

}
