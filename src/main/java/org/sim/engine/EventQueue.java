package org.sim.engine;

import org.sim.event.Event;

import java.util.PriorityQueue;

public class EventQueue {

	private final PriorityQueue<Event> queue = new PriorityQueue<>();

	public void schedule(Event event) {
		queue.add(event);
	}

	public Event nextEvent() {
		return queue.poll();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}

}
