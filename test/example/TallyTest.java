package example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import example.Tally;

/**
 * @author W.P. McNeill
 * 
 */
public class TallyTest {

	/**
	 * {A => 3, Z => 3, B => 2}
	 */
	private Tally<String> azb;
	/**
	 * Empty tally
	 */
	private Tally<String> empty;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		empty = new Tally<String>();
		azb = new Tally<String>();
		azb.addToCount("A", 3);
		azb.addToCount("B", 2);
		azb.addToCount("Z", 3);
	}

	/**
	 * Test method for {@link example.TallyTest#getCount()}.
	 */
	@Test
	public void testGetCount() {
		assertEquals(0, empty.getCount("A"));
		assertEquals(3, azb.getCount("A"));
		assertEquals(2, azb.getCount("B"));
		assertEquals(3, azb.getCount("Z"));
	}

	/**
	 * Test method for {@link example.TallyTest#getTotalCount()}.
	 */
	@Test
	public void testGetTotalCount() {
		assertEquals(new Long(0), empty.getTotalCount());
		assertEquals(new Long(3+2+3), azb.getTotalCount());
	}

	
	/**
	 * Test method for {@link example.TallyTest#getLargestKey()}.
	 */
	@Test
	public void testGetLargestKey() {
		assertEquals(null, empty.getLargestKey());
		assertEquals("A", azb.getLargestKey());		
	}
	
	/**
	 * Test method for {@link example.TallyTest#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("", empty.toString());
		// A	3
		// Z	3
		// B	2
		assertEquals("A\t3\n" + "Z\t3\n" + "B\t2", azb.toString());
	}
}
