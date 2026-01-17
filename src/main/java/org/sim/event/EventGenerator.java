package org.sim.event;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.math3.distribution.PoissonDistribution;

import org.sim.engine.SimulationEngine;
import org.sim.station.StationRouter;
import org.sim.station.ServiceStation;
import org.sim.model.Client;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class EventGenerator {
    private final StationRouter stationRouter;
    private final PoissonDistribution poissonDistribution;
    private final SimulationEngine simulationEngine;

    public Collection<Event> generateEventsUntil(final double untilTime) {
        final List<Event> eventSequence = new ArrayList<>();
        double currentTime = 0;
        int clientId = 0;

        while (currentTime <= untilTime) {
            int arrivals = poissonDistribution.sample(); // number of arrivals in this
                                                         // interval
            for (int i = 0; i < arrivals; i++) {
                eventSequence.add(generateEvent(clientId++, currentTime));
            }
            currentTime += 1.0; // advance by 1 time unit
        }

        return eventSequence;
    }

    private double getArrivalTime(final double currentTime) {
        return currentTime + poissonDistribution.sample();
    }

    private Event generateEvent(final int clientId, final double currentTime) {
        final Queue<ServiceStation> stationSequence = stationRouter.getStationSequence();
        final Client client = new Client(clientId, stationSequence);

        final double arrivalTime = getArrivalTime(currentTime);
        return new ArrivalEvent(arrivalTime, client, stationSequence.peek(),
                                        simulationEngine);
    }

}
