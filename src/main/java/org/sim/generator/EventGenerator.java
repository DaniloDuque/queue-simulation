package org.sim.generator;

import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.PoissonDistribution;

import org.sim.station.StationRouter;
import org.sim.station.ServiceStation;
import org.sim.model.Client;
import org.sim.event.Event;
import org.sim.event.ArrivalEvent;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class EventGenerator {
    private final StationRouter stationRouter;
    private final PoissonDistribution poissonDistribution;

    public Collection<Event> generateEventsUntil(final double untilTime) {
        double currentTime = 0;
        int clientId = 0;
        final List eventSequence = new ArrayList<Event>();
        do {
            final Event currentEvent = generateEvent(clientId++, currentTime);
            currentTime = currentEvent.time();
            eventSequence.add(currentEvent);
        } while (currentTime <= untilTime);

        return eventSequence;
    }

    private double getArrivalTime(final double currentTime) {
        return currentTime + poissonDistribution.sample();
    }

    private Event generateEvent(final int clientId, final double currentTime) {
        final Collection<ServiceStation> stationSequence = stationRouter.getStationSequence();
        final Client client = new Client(clientId, stationSequence);

        final double arrivalTime = getArrivalTime(currentTime);
        final Event event = new ArrivalEvent(arrivalTime, client);
        return event;
    }

}
