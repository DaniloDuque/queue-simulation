package org.sim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sim.engine.SimulationEngine;
import org.sim.event.EventGenerator;
import org.sim.module.SimulationModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimulationModule());

        EventGenerator generator = injector.getInstance(EventGenerator.class);
        SimulationEngine engine = injector.getInstance(SimulationEngine.class);

        generator.generateEventsUntil(3600 * 8.0).forEach(engine::schedule);
        engine.run(3600 * 8.0);
    }
}
