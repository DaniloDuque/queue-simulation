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
        log.debug("Scheduled event {} at time {}", event.getClass().getSimpleName(), event.time());
    }

    public void run(final double untilTime) {
        log.info("Simulation started");

        while (!eventQueue.isEmpty()) {
            final Event next = eventQueue.nextEvent();

            if (next.time() > untilTime) {
                break;
            }

            clock.advanceTo(next.time());
            log.debug("Processing event {} at time {}", next.getClass().getSimpleName(),
                    clock.now());

            next.process();
        }

        log.info("Simulation finished at time {}", clock.now());
    }
}
