package example.test;

import static org.junit.Assert.*;

import org.junit.Test;

import example.Tally;

/**
 * @author W.P. McNeill
 * 
 */
public class TallyTest {

	@Test
	public void testEmptyTally() {
		Tally<String> t = new Tally<String>();
		assertEquals(null, t.getLargestKey());
		assertEquals(0, t.getCount("A"));
	}

	@Test
	public void testNonEmptyTally() {
		Tally<String> t = new Tally<String>();
		t.addToCount("A", 3);
		t.addToCount("B", 2);
		t.addToCount("Z", 3);
		assertEquals(3, t.getCount("A"));
		assertEquals(2, t.getCount("B"));
		assertEquals(3, t.getCount("Z"));
		assertEquals("A", t.getLargestKey());
	}
}
