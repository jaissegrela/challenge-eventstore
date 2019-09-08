package net.intelie.challenges;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class InMemoryEventStoreTest {
	
	InMemoryEventStore store;

	@Before
	public void setUp() throws Exception {
		store = new InMemoryEventStore();
	}
	
	protected Event[] toArrayList(EventIterator it){
		ArrayList<Event> elements = new ArrayList<Event>();
		while(it.moveNext())
			elements.add(it.current());
		
		Event[] result = new Event[elements.size()];
		elements.toArray(result);
		return result;
	}


	@Test
	public final void testInsert() {
		String type1 = "Type 1";
		String type10 = "Type 10";
		Event e1 = new Event(type1, 5);
		Event e2 = new Event(type10, 4);
		Event e3 = new Event(type1, 6);

		store.insert(e1);
		store.insert(e2);
		store.insert(e3);
		
		Event[] actual = toArrayList(store.query(type1, 1, 10));
		Event[] expected = new Event[] {e1, e3};
		
		assertArrayEquals(expected, actual);
		
		actual = toArrayList(store.query(type10, 1, 10));
		expected = new Event[] {e2};
		
		assertArrayEquals(expected, actual);
	}

	@Test
	public final void testRemoveAll_TypeNotSpecifiedNotRemoved() {
		String type1 = "Type 1";
		String type10 = "Type 10";
		
		Event e1 = new Event(type10, 5);
		Event e2 = new Event(type1, 4);
		Event e3 = new Event(type10, 6);

		store.insert(e1);
		store.insert(e2);
		store.insert(e3);
		
		store.removeAll(type1);
		
		Event[] actual = toArrayList(store.query(type10, 1, 10));
		Event[] expected = new Event[] {e1, e3};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public final void testRemoveAll_TypeSpecifiedRemoved() {
		String type1 = "Type 1";
		String type10 = "Type 10";
		
		Event e1 = new Event(type10, 5);
		Event e2 = new Event(type1, 4);
		Event e3 = new Event(type10, 6);

		store.insert(e1);
		store.insert(e2);
		store.insert(e3);
		
		store.removeAll(type1);

		Event[] actual = toArrayList(store.query(type1, 1, 10));
		Event[] expected = new Event[] {};
		assertArrayEquals(expected, actual);
	}

	@Test
	public final void testQuery_TypeNotFound() {
		store.insert(new Event("Type 10", 5));
		
		Event[] actual = toArrayList(store.query("Type 1", 1, 10));
		Event[] expected = new Event[] {};
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public final void testQuery() {
		String type1 = "Type 1";
		Event e1 = new Event(type1, 5);
		store.insert(e1);
		
		Event[] actual = toArrayList(store.query(type1, 1, 10));
		Event[] expected = new Event[] {e1};
		
		assertArrayEquals(expected, actual);
	}

}
