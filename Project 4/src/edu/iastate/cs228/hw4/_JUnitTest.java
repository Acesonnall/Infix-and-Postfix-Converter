package edu.iastate.cs228.hw4;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Random;

import org.junit.Test;

/**
 * Some very basic tests
 * 
 * @author Yuxiang Zhang
 * 
 */
public class _JUnitTest {

	private int n = 1000; // number of test cases
	private Random r = new Random();
	private HashMap<Character, Integer> varTable = new HashMap<>();

	private void fillTable(char[] cs, int[] is) {
		if (cs.length != is.length)
			throw new IllegalArgumentException("cs.length != is.length");

		varTable.clear();
		for (int i = 0; i < cs.length; i++) {
			varTable.put(cs[i], is[i]);
		}
	}

	@Test
	public void test_infix() throws Exception {
		Expression exp = new InfixExpression("( ( a + b - c ) * d / e ) ^ 1 ^ 2 ^ 3 % 4 ^ 5");
		assertEquals("((a + b - c) * d / e) ^ 1 ^ 2 ^ 3 % 4 ^ 5", exp.toString());

		for (int i = 0; i < n; i++) {
			int a = r.nextInt(), b = r.nextInt(), c = r.nextInt(), d = r.nextInt(), e = r.nextInt();
			fillTable(new char[] { 'a', 'b', 'c', 'd', 'e' }, new int[] { a, b, c, d, e });
			exp.setVarTable(varTable);
			int expected = (a + b - c) * d / e % (1 << 10);
			assertEquals(expected, exp.evaluate());
		}
	}

	@Test
	public void test_postfix() throws Exception {
		Expression exp = new PostfixExpression("a b c d e + - * /");
		assertEquals("a b c d e + - * /", exp.toString());

		for (int i = 0; i < n; i++) {
			int a = r.nextInt(), b = r.nextInt(), c = r.nextInt(), d = r.nextInt(), e = r.nextInt();
			fillTable(new char[] { 'a', 'b', 'c', 'd', 'e' }, new int[] { a, b, c, d, e });
			exp.setVarTable(varTable);
			int expected = a / (b * (c - (d + e)));
			assertEquals(expected, exp.evaluate());
		}
	}

	@Test
	public void test_right_associativity() throws Exception {
		assertEquals(2, new InfixExpression("  2 ^ 1   ^ 2").evaluate());
		assertEquals(4, new InfixExpression("( 2 ^ 1 ) ^ 2").evaluate());

		assertEquals(12, new InfixExpression("  2 ^ 3   ^ 2 % 100").evaluate());
		assertEquals(64, new InfixExpression("( 2 ^ 3 ) ^ 2 % 100").evaluate());

		assertEquals(44, new InfixExpression("  4 ^ 3   ^ 2 % 100").evaluate());
		assertEquals(96, new InfixExpression("( 4 ^ 3 ) ^ 2 % 100").evaluate());
	}

	@Test
	public void test_toString() throws Exception {
		assertEquals("(a + b - c) * d", // remove extra spaces
				new InfixExpression("\t \t ( \t \t a \t + \t b \t - \t c \t \t ) \t * \t d \t").toString());

		assertEquals("a b c d + - *", // remove extra spaces
				new PostfixExpression("\t a \t b \t c \t d \t \t + \t - \t * \t \t \t \t \t \t").toString());
	}

}