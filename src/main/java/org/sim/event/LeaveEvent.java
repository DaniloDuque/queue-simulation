package org.sim.event;

import lombok.AllArgsConstructor;
import org.sim.engine.*;
import org.sim.model.Client;
import org.sim.station.ServiceStation;

@AllArgsConstructor
public class LeaveEvent implements Event {

    private final double departureTime;
    private final ServiceStation station;
    private final Client client;
    private final SimulationEngine engine;

    @Override
    public double time() {
        return departureTime;
    }

    @Override
    public void process() {
        station.leave(client, engine);
    }
}
