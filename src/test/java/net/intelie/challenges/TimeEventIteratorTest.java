package net.intelie.challenges;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

/**
 * An abstraction of an event store.
 * <p>
 * Events may be stored in memory, data files, a database, on a remote
 * server, etc. For this challenge, you should implement an in-memory
 * event event store.
 */
public class TimeEventIteratorTest {

	protected Vector<Event> events;

	@Before
	public void setUp() throws Exception {
		events = new Vector<Event>();
		String type = "type";
		events.add(new Event(type, 19));
		events.add(new Event(type, 9));
		events.add(new Event(type, 12));
		events.add(new Event(type, 10));
		events.add(new Event(type, 20));
		events.add(new Event(type, 18));
	}


	@Test
	public final void testMoveNext_AllElementsOfIterator() {
		TimeEventIterator iterator = new TimeEventIterator(events, 1, 25);
		for(int i = 0; i < events.size(); i++)
			assertTrue(iterator.moveNext());
		assertFalse(iterator.moveNext());
	}
	
	@Test
	public final void testMoveNext_Filtered() {
		TimeEventIterator iterator = new TimeEventIterator(events, 10, 20);
		for(int i = 0; i < 4; i++)
			assertTrue(iterator.moveNext());
		assertFalse(iterator.moveNext());
	}

	@Test(expected=IllegalStateException.class)
	public final void testCurrent_MoveNextWasNeverCalled() {
		TimeEventIterator iterator = new TimeEventIterator(events, 1, 25);
		iterator.current();
	}

	@Test(expected=IllegalStateException.class)
	public final void testCurrent_MoveNextWereFalse() {
		TimeEventIterator iterator = new TimeEventIterator(events, 21, 25);
		assertFalse(iterator.moveNext());
		iterator.current();
	}
	
	@Test
	public final void testCurrent_DoesNotModify() {
		TimeEventIterator iterator = new TimeEventIterator(events, 1, 25);
		for(int i = 0; i < events.size(); i++) {
			iterator.moveNext();
			assertTrue(events.get(i) == iterator.current());
			assertTrue(events.get(i) == iterator.current());
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public final void testRemove_MoveNextWasNeverCalled() {
		TimeEventIterator iterator = new TimeEventIterator(events, 1, 25);
		iterator.remove();
	}

	@Test(expected=IllegalStateException.class)
	public final void testRemove_MoveNextWereFalse() {
		TimeEventIterator iterator = new TimeEventIterator(events, 21, 25);
		assertFalse(iterator.moveNext());
		iterator.remove();
	}
	
	@Test
	public final void testRemove_DoesNotInsertNewElement() {
		Vector<Event> temp = new Vector<Event>(events);
		TimeEventIterator iterator = new TimeEventIterator(temp, 10, 20);
		
		// => Removing index 0 and 3 from temp vector
		for(int i = 0; i < 4; i++) {
			iterator.moveNext();
			if(i % 2 == 0)
				iterator.remove();
		}	
		//events vector contains all elements from temp vector
		for(int i = 0; i < temp.size(); i++)
			assertTrue(events.contains(temp.get(i)));

	}
	
	@Test
	public final void testRemove_VectorDoesNotContainRemovedElements() {
		Vector<Event> temp = new Vector<Event>(events);
		TimeEventIterator iterator = new TimeEventIterator(temp, 10, 20);
		
		// => Removing index 0 and 3 from temp vector
		for(int i = 0; i < 4; i++) {
			iterator.moveNext();
			if(i % 2 == 0)
				iterator.remove();
		}	
		
		//temp vector does not contain removed elements (index: 0 and 3 from events vector)
		assertFalse(temp.contains(events.get(0)));
		assertFalse(temp.contains(events.get(3)));
	}

}
