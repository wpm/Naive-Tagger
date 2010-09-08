/**
 * 
 */
package example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import example.CategoryCounts;

/**
 * @author W.P. McNeill
 * 
 */
public class CategoryCountsTest {

	/**
	 * Empty category counts
	 */
	private CategoryCounts<String, String> empty;
	/**
	 * {"bank" => {"NN" => 3, "VB" => 2}, {"run" => {"NN" => 2, "VB" => 1}}}
	 */
	private CategoryCounts<String, String> bankRun;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		empty = new CategoryCounts<String, String>();
		bankRun = new CategoryCounts<String, String>();
		for (int i = 0; i < 3; i++)
			bankRun.addToken("bank", "NN");
		for (int i = 0; i < 2; i++)
			bankRun.addToken("bank", "VB");
		for (int i = 0; i < 2; i++)
			bankRun.addToken("run", "NN");
		bankRun.addToken("run", "VB");
	}

	/**
	 * Test method for {@link example.CategoryCounts#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("", empty.toString());
		// bank:
		// NN 3
		// VB 2
		// run:
		// NN 2
		// VB 1
		assertEquals("bank:\n" + "NN\t3\n" + "VB\t2\n" + "run:\n" + "NN\t2\n"
				+ "VB\t1", bankRun.toString());
	}

	/**
	 * Test method for {@link example.CategoryCounts#getMostCommonCategory()}.
	 */
	@Test
	public void testGetMostCommonCategory() {
		assertEquals(null, empty.getMostCommonCategory());
		assertEquals("NN", bankRun.getMostCommonCategory());
	}

}
