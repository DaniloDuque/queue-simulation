package org.sim.engine;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sim.event.Event;

@Slf4j
@AllArgsConstructor(onConstructor_ = @Inject)
public class SimulationEngine {

    private final SimulationClock clock;
    private final EventQueue eventQueue;

    public double now() {
        return clock.now();
    }

    public void schedule(@NonNull final Event event) {
        eventQueue.schedule(event);
    }

    public void run(final double untilTime) throws IllegalArgumentException {
        log.info("Simulation started");

        while (true) {
            Event currentEvent = eventQueue.nextEvent();

            if (currentEvent == null || currentEvent.time() > untilTime) {
                break;
            }

            clock.advanceTo(currentEvent.time());
            log.info("Processing event {} at time {}",
                                            currentEvent.getClass().getSimpleName(),
                                            clock.now());
            currentEvent.process();
        }

        log.info("Simulation finished at time {}", clock.now());
    }

    private boolean isRunning(@NonNull final Event next, final double untilTime) {
        return next.time() <= untilTime;
    }

}
