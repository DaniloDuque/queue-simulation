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
    private boolean eventsRunning = true;

    public double now() {
        return clock.now();
    }

    public int getQueueSize() {
        return eventQueue.getSize();
    }

    public void schedule(@NonNull final Event event) {
        eventQueue.schedule(event);
        log.debug("Scheduled event {} at time {}", event.getClass().getSimpleName(), event.time());
    }

    public void run(final double untilTime) {
        log.info("Simulation started");

        while (!eventQueue.isEmpty()) {
            final Event next = eventQueue.nextEvent();

            if (!isRunning(next, untilTime)) {
                break;
            }

            clock.advanceTo(next.time());
            log.debug("Processing event {} at time {}", next.getClass().getSimpleName(),
                    clock.now());

            next.process();
        }

        log.info("Simulation finished at time {}", clock.now());
    }

    public boolean isRunning(final Event next, final double untilTime) {
        eventsRunning = next.time() > untilTime;
        return eventsRunning;
    }
}
