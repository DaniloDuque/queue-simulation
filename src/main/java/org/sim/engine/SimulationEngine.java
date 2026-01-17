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

        while (!eventQueue.isEmpty()) {
            final Event next = eventQueue.nextEvent();

            if (!isRunning(next, untilTime)) {
                break;
            }

            clock.advanceTo(next.time());
            log.info("Processing event {} at time {}", next.getClass().getSimpleName(),
                                            clock.now());
            next.process();
        }

        log.info("Simulation finished at time {}", clock.now());
    }

    private boolean isRunning(@NonNull final Event next, final double untilTime) {
        return next.time() <= untilTime;
    }

}
