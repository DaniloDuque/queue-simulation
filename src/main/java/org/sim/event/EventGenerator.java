package org.sim.event;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import org.sim.engine.SimulationEngine;
import org.sim.station.StationRouter;
import org.sim.station.ServiceStation;
import org.sim.model.Client;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class EventGenerator {
    private final StationRouter stationRouter;
    private final ExponentialDistribution exponentialDistribution;
    private final SimulationEngine simulationEngine;

    public Collection<Event> generateEventsUntil(final double untilTime) {
        final List<Event> eventSequence = new ArrayList<>();
        double currentTime = 0;
        int clientId = 0;

        while (currentTime <= untilTime) { // Change < to <=
            double interArrivalTime = exponentialDistribution.sample();
            currentTime += interArrivalTime;

            // Always add the event, even if it goes slightly past untilTime
            eventSequence.add(generateEvent(clientId++, currentTime));
        }

        log.info("Generated {} arrival events", eventSequence.size());
        log.info("Last arrival at time: {}", eventSequence.get(eventSequence.size() - 1)
                                        .time());
        log.info("Target time: {}", untilTime);

        return eventSequence;
    }

    private Event generateEvent(final int clientId, final double currentTime) {
        final Queue<ServiceStation> stationSequence = stationRouter.getStationSequence();
        final Client client = new Client(clientId, stationSequence);

        return new ArrivalEvent(currentTime, client, stationSequence.peek(),
                                        simulationEngine);
    }

}
