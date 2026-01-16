package org.sim.event;

import org.sim.engine.*;
import org.sim.model.Client;
import org.sim.station.ServiceStation;

public class DepartureEvent implements Event {

    private final double time;
    private final ServiceStation station;
    private final Client client;
    private final SimulationEngine engine;

    public DepartureEvent(double time, ServiceStation station, Client client,
            SimulationEngine engine) {
        this.time = time;
        this.station = station;
        this.client = client;
        this.engine = engine;
    }

    @Override
    public double time() {
        return time;
    }

    @Override
    public void process() {
        station.completeService(client, engine);
    }
}
