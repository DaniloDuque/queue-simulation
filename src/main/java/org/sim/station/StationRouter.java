package org.sim.station;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sim.module.Constants;

@AllArgsConstructor(onConstructor_ = @Inject)
public class StationRouter {

    private final ServiceStation cashier;
    private final ServiceStation drinks;
    private final ServiceStation frier;
    private final ServiceStation desert;
    private final ServiceStation chicken;

    public Collection<ServiceStation> getStationSequence() {
        final List<ServiceStation> stationSequence = new ArrayList<>();

        if (Math.random() < Constants.CASHIER_PROB) {
            stationSequence.add(cashier);
        }
        if (Math.random() < Constants.DRINKS_PROB) {
            stationSequence.add(drinks);
        }
        if (Math.random() < Constants.FRIER_PROB) {
            stationSequence.add(frier);
        }
        if (Math.random() < Constants.DESERT_PROB) {
            stationSequence.add(desert);
        }
        if (Math.random() < Constants.CHICKEN_PROB) {
            stationSequence.add(chicken);
        }

        return stationSequence;
    }
}
