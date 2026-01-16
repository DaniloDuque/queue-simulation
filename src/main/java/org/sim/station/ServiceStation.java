package org.sim.station;

import java.util.Queue;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.distribution.ServiceTimeDistribution;
import org.sim.engine.SimulationEngine;
import org.sim.event.DepartureEvent;
import org.sim.model.Client;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class ServiceStation {

    private final String name;
    private final int workers;
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

    private void startService(Client client, SimulationEngine engine) {
        busyWorkers++;
        double serviceTime = dist.sample();
        engine.schedule(new DepartureEvent(engine.now() + serviceTime, this, client, engine));
    }

    public void completeService(Client finishedClient, SimulationEngine engine) {
        // later we will forward finishedClient to next station

        if (queue.isEmpty()) {
            busyWorkers--;
        } else {
            startService(queue.poll(), engine);
        }
    }

}
