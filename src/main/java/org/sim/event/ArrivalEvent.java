package org.sim.event;

import org.sim.model.Client;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

@AllArgsConstructor(onConstructor_ = @Inject)
public class ArrivalEvent implements Event {

    private final double arrivalTime;
    private final Client client;

    @Override
    public double time() {
        return arrivalTime;
    }

    @Override
    public void process() {
    }
}
