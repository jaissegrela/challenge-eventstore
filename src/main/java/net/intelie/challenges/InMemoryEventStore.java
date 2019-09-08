package net.intelie.challenges;

import java.util.Hashtable;
import java.util.Vector;


/**
 * An event store.
 * <p>
 * InMemoryEventStore is an in-memory implementation of event store
 */
public class InMemoryEventStore implements EventStore {

	protected Hashtable<String, Vector<Event>> store;
	
	public InMemoryEventStore()
	{
		store = new Hashtable<String, Vector<Event>>();
	}

	@Override
	public synchronized void insert(Event event) {
		if (event != null) {
			Vector<Event> temp = store.get(event.type());
			if (temp == null) {
				temp = new Vector<Event>();
				store.put(event.type(), temp);
			}
			temp.add(event);
		}
	}

	@Override
	public void removeAll(String type) {
		store.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		Vector<Event> temp = store.get(type);
		if(temp == null)
			temp = new Vector<Event>();
		return new TimeEventIterator(temp, startTime, endTime);
	}

}
