package org.sim.station;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.AllArgsConstructor;

import java.util.*;

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
        final List<ServiceStation> shuffleList = new ArrayList<>();

        // Cashier is always first
        if (Math.random() < Constants.CASHIER_PROB) {
            stationSequence.add(cashier);
        }

        // Add other stations to shuffle list based on probabilities
        if (Math.random() < Constants.DRINKS_PROB) {
            shuffleList.add(drinks);
        }
        if (Math.random() < Constants.FRIER_PROB) {
            shuffleList.add(frier);
        }
        if (Math.random() < Constants.DESERT_PROB) {
            shuffleList.add(desert);
        }
        if (Math.random() < Constants.CHICKEN_PROB) {
            shuffleList.add(chicken);
        }

        // Shuffle the remaining stations
        Collections.shuffle(shuffleList);
        stationSequence.addAll(shuffleList);

        return stationSequence;
    }
}
