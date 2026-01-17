package org.sim.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.sim.distribution.BinomialServiceTimeDistribution;
import org.sim.distribution.ExponentialServiceTimeDistribution;
import org.sim.distribution.GeometricServiceTimeDistribution;
import org.sim.distribution.NormalServiceTimeDistribution;
import org.sim.engine.EventQueue;
import org.sim.engine.SimulationClock;
import org.sim.engine.SimulationEngine;
import org.sim.event.EventGenerator;
import org.sim.stat.StatisticsCollector;
import org.sim.station.ServiceStation;
import org.sim.station.StationRouter;
import java.util.LinkedList;

public class SimulationModule extends AbstractModule {

    @Provides
    @Singleton
    StatisticsCollector provideStatisticsCollector() {
        return new StatisticsCollector(new LinkedList<>());
    }

    @Provides
    @Singleton
    @Named(Constants.CASHIER_STATION_NAME)
    ServiceStation provideCashier(StatisticsCollector statisticsCollector) {
        return new ServiceStation(Constants.NUMBER_OF_CASHIER_WORKERS,
                                        Constants.CASHIER_STATION_NAME,
                                        new ExponentialServiceTimeDistribution(
                                                                        Constants.CASHIER_STATION_MEAN),
                                        new LinkedList<>(), statisticsCollector, 0);
    }

    @Provides
    @Singleton
    @Named(Constants.DRINKS_STATION_NAME)
    ServiceStation provideDrinks(StatisticsCollector statisticsCollector) {
        return new ServiceStation(Constants.NUMBER_OF_DRINKS_WORKERS,
                                        Constants.DRINKS_STATION_NAME,
                                        new ExponentialServiceTimeDistribution(
                                                                        Constants.DRINKS_STATION_MEAN),
                                        new LinkedList<>(), statisticsCollector, 0);
    }

    @Provides
    @Singleton
    ExponentialDistribution provideArrivalDistribution() {
        return new ExponentialDistribution(1.0 / Constants.ARRIVAL_LAMBDA);
    }

    @Provides
    @Singleton
    @Named(Constants.FRIER_STATION_NAME)
    ServiceStation provideFrier(StatisticsCollector statisticsCollector) {
        return new ServiceStation(Constants.NUMBER_OF_FRIER_WORKERS,
                                        Constants.FRIER_STATION_NAME,
                                        new NormalServiceTimeDistribution(
                                                                        Constants.FRIER_STATION_MEAN,
                                                                        Constants.FRIER_STATION_STD),
                                        new LinkedList<>(), statisticsCollector, 0);
    }

    @Provides
    @Singleton
    @Named(Constants.DESERT_STATION_NAME)
    ServiceStation provideDesert(StatisticsCollector statisticsCollector) {
        return new ServiceStation(Constants.NUMBER_OF_DESERT_WORKERS,
                                        Constants.DESERT_STATION_NAME,
                                        new BinomialServiceTimeDistribution(
                                                                        Constants.DESERT_STATION_N,
                                                                        Constants.DESERT_STATION_P),
                                        new LinkedList<>(), statisticsCollector, 0);
    }

    @Provides
    @Singleton
    @Named(Constants.CHICKEN_STATION_NAME)
    ServiceStation provideChicken(StatisticsCollector statisticsCollector) {
        return new ServiceStation(Constants.NUMBER_OF_CHICKEN_WORKERS,
                                        Constants.CHICKEN_STATION_NAME,
                                        new GeometricServiceTimeDistribution(
                                                                        Constants.CHICKEN_STATION_P),
                                        new LinkedList<>(), statisticsCollector, 0);
    }

    @Provides
    @Singleton
    StationRouter provideStationRouter(
                                    @Named(Constants.CASHIER_STATION_NAME) ServiceStation cashier,
                                    @Named(Constants.DRINKS_STATION_NAME) ServiceStation drinks,
                                    @Named(Constants.FRIER_STATION_NAME) ServiceStation frier,
                                    @Named(Constants.DESERT_STATION_NAME) ServiceStation desert,
                                    @Named(Constants.CHICKEN_STATION_NAME) ServiceStation chicken) {
        return new StationRouter(cashier, drinks, frier, desert, chicken);
    }

    @Provides
    @Singleton
    SimulationClock provideSimulationClock() {
        return new SimulationClock();
    }

    @Provides
    @Singleton
    EventQueue provideEventQueue() {
        return new EventQueue();
    }

    @Provides
    @Singleton
    SimulationEngine provideSimulationEngine(SimulationClock clock,
                                    EventQueue eventQueue) {
        return new SimulationEngine(clock, eventQueue);
    }

    @Provides
    @Singleton
    EventGenerator provideEventGenerator(StationRouter stationRouter,
                                    ExponentialDistribution arrivalDistribution,
                                    SimulationEngine simulationEngine) {
        return new EventGenerator(stationRouter, arrivalDistribution, simulationEngine);
    }
}
