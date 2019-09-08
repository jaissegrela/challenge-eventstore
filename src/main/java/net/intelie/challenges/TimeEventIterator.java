package net.intelie.challenges;

import java.util.Vector;

/**
 * An iterator over an event collection.
 * <p>
 * This iterator filters an event collection by timestamp range disregarding
 * type
 */
public class TimeEventIterator implements EventIterator {

	protected Vector<Event> events;
	protected int index;
	protected Event current;
	protected long startTime, endTime;

	protected TimeEventIterator(Vector<Event> events, long startTime, long endTime) {
		this.events = events;
		this.startTime = startTime;
		this.endTime = endTime;
		index = -1;
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public synchronized boolean moveNext() {
		while (++index < events.size()) {
			current = events.get(index);
			if (startTime <= current.timestamp() && current.timestamp() < endTime)
				return true;
		}
		current = null;
		return false;
	}

	@Override
	public Event current() {
		if (current == null)
			throw new IllegalStateException();
		return current;
	}

	@Override
	public synchronized void remove() {
		if (current == null)
			throw new IllegalStateException();
		events.remove(index--);
	}

}
