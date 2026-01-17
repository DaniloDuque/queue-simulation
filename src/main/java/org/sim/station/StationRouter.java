package org.sim.station;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.AllArgsConstructor;
import java.util.LinkedList;
import java.util.Queue;

import org.sim.module.Constants;

@AllArgsConstructor(onConstructor_ = @Inject)
public class StationRouter {

    private final @Named(Constants.CASHIER_STATION_NAME) ServiceStation cashier;
    private final @Named(Constants.DRINKS_STATION_NAME) ServiceStation drinks;
    private final @Named(Constants.FRIER_STATION_NAME) ServiceStation frier;
    private final @Named(Constants.DESERT_STATION_NAME) ServiceStation desert;
    private final @Named(Constants.CHICKEN_STATION_NAME) ServiceStation chicken;

    public Queue<ServiceStation> getStationSequence() {
        final Queue<ServiceStation> stationSequence = new LinkedList<>();

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
