package org.sim.engine;

public interface Event extends Comparable<Event> {

    double time(); // scheduled time of event
    void process(); // what happens when event fires

    @Override
    default int compareTo(Event other) {
        return Double.compare(this.time(), other.time());
    }
}
