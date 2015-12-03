package edu.iastate.cs228.hw4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * @author Madeline Andreassen
 * 
 */

public class _JUnit_Madeline {

	private HashMap<Character, Integer> varTable = new HashMap<>();

	/**
	 * Copied from Yuxiang Zhang
	 */
	private void fillTable(char[] cs, int[] is) {
		if (cs.length != is.length)
			throw new IllegalArgumentException("cs.length != is.length");

		varTable.clear();
		for (int i = 0; i < cs.length; i++) {
			varTable.put(cs[i], is[i]);
		}
	}

	@Test
	public void testInfixPostixMethod() throws Exception {
		InfixExpression exp = new InfixExpression("2 + 3 * 4");
		exp.postfix();
		assertEquals("2 3 4 * +", exp.postfixString());

		exp = new InfixExpression("2 * 3 + 5");
		exp.postfix();
		assertEquals("2 3 * 5 +", exp.postfixString());

		exp = new InfixExpression("( 1 + 2 ) * 7");
		exp.postfix();
		assertEquals("1 2 + 7 *", exp.postfixString());

		exp = new InfixExpression("2 * 3 / 5");
		exp.postfix();
		assertEquals("2 3 * 5 /", exp.postfixString());

		exp = new InfixExpression("a * b / 2 ^ c ^ d - 3 * ( ( e - f ) * ( g - h ) + 5 )");
		exp.postfix();
		assertEquals("a b * 2 c d ^ ^ / 3 e f - g h - * 5 + * -", exp.postfixString());

		exp = new InfixExpression(
				"2 * b % 3 + ( a - ( b - ( c - d * e ) ) ) ^ ( 4 + b * ( c - d ) ) ^ ( 1 - 6 / ( i - j ) )");
		exp.postfix();
		assertEquals("2 b * 3 % a b c d e * - - - 4 b c d - * + 1 6 i j - / - ^ ^ +", exp.postfixString());

		exp = new InfixExpression("( 5 + f ) / ( 2 - a ) * b ^ c ^ ( d + e ) - ( 1 + 2 * ( ( g - h ) / ( a + b ) ) )");
		exp.postfix();
		assertEquals("5 f + 2 a - / b c d e + ^ ^ * 1 2 g h - a b + / * + -", exp.postfixString());

		exp = new InfixExpression("2 ^ a ^ b * c - ( ( d + e ) * 2 + f ^ ( g - h ) ) - 5 / ( i + j )");
		exp.postfix();
		assertEquals("2 a b ^ ^ c * d e + 2 * f g h - ^ + - 5 i j + / -", exp.postfixString());

		exp = new InfixExpression("( 1 / ( 2 - 3 + 4 ) ) * ( 5 - 6 ) * 7");
		exp.postfix();
		assertEquals("1 2 3 - 4 + / 5 6 - * 7 *", exp.postfixString());

		exp = new InfixExpression("7 - ( 2 * 3 + 5 ) * ( 8 - 4 / 2 )");
		exp.postfix();
		assertEquals("7 2 3 * 5 + 8 4 2 / - * -", exp.postfixString());
	}

	@Test
	public void testInfixEvaluate() throws Exception {
		InfixExpression exp = new InfixExpression("2 + 3 * 4");
		assertEquals(14, exp.evaluate());

		exp = new InfixExpression("2 * 3 + 5");
		assertEquals(11, exp.evaluate());

		exp = new InfixExpression("( 1 + 2 ) * 7");
		assertEquals(21, exp.evaluate());

		exp = new InfixExpression("2 * 3 / 6");
		assertEquals(1, exp.evaluate());

		fillTable(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' },
				new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 1 });

		exp = new InfixExpression("a * b / 2 ^ c ^ d - 3 * ( ( e - f ) * ( g - h ) + 5 )");
		exp.setVarTable(varTable);
		assertEquals(-18, exp.evaluate());

		exp = new InfixExpression(
				"2 * b % 3 + ( a - ( b - ( c - d * e ) ) ) ^ ( 4 + b * ( c - d ) ) ^ ( 1 - 6 / ( i - j ) )");
		exp.setVarTable(varTable);
		assertEquals(325, exp.evaluate());

		exp = new InfixExpression("( 5 + f ) / ( 2 - a ) * b ^ c ^ ( d + e ) - ( 1 + 2 * ( ( g - h ) / ( a + b ) ) )");
		exp.setVarTable(varTable);
		assertEquals(2147483636, exp.evaluate());

		exp = new InfixExpression("2 ^ a ^ b * c - ( ( d + e ) * 2 + f ^ ( g - h ) ) - 5 / ( i + j )");
		exp.setVarTable(varTable);
		assertEquals(-12, exp.evaluate());

		exp = new InfixExpression("( 6 / ( 2 - 3 + 4 ) ) * ( 5 - 6 ) * 7");
		assertEquals(-14, exp.evaluate());

		exp = new InfixExpression("7 - ( 2 * 3 + 1 ) * ( 8 - 4 / 2 )");
		assertEquals(-35, exp.evaluate());
	}

	@Test
	public void testPostfixEvaluate() throws Exception {
		PostfixExpression exp = new PostfixExpression("2 3 4 * +");
		assertEquals(14, exp.evaluate());

		exp = new PostfixExpression("2 3 * 5 +");
		assertEquals(11, exp.evaluate());

		exp = new PostfixExpression("1 2 + 7 *");
		assertEquals(21, exp.evaluate());

		exp = new PostfixExpression("2 3 * 6 /");
		assertEquals(1, exp.evaluate());

		fillTable(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j' },
				new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 1 });

		exp = new PostfixExpression("a b * 2 c d ^ ^ / 3 e f - g h - * 5 + * -");
		exp.setVarTable(varTable);
		assertEquals(-18, exp.evaluate());

		exp = new PostfixExpression("2 b * 3 % a b c d e * - - - 4 b c d - * + 1 6 i j - / - ^ ^ +");
		exp.setVarTable(varTable);
		assertEquals(325, exp.evaluate());

		exp = new PostfixExpression("5 f + 2 a - / b c d e + ^ ^ * 1 2 g h - a b + / * + -");
		exp.setVarTable(varTable);
		assertEquals(2147483636, exp.evaluate());

		exp = new PostfixExpression("2 a b ^ ^ c * d e + 2 * f g h - ^ + - 5 i j + / -");
		exp.setVarTable(varTable);
		assertEquals(-12, exp.evaluate());

		exp = new PostfixExpression("6 2 3 - 4 + / 5 6 - * 7 *");
		assertEquals(-14, exp.evaluate());

		exp = new PostfixExpression("7 2 3 * 1 + 8 4 2 / - * -");
		assertEquals(-35, exp.evaluate());
	}

	@Test
	public void testInfixExpressionFormatException() {

		Throwable e = null;
		// "Operator expected" if the cumulative rank goes above 1;
		InfixExpression infix = new InfixExpression("1 2 + 3");
		try {
			infix.postfix();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Operand expected" if the rank goes below 0;
		e = null;
		infix = new InfixExpression("1 - - 2");
		try {
			infix.postfix();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Missing '('" if scanning a ‘)’ results in popping the stack empty
		// with no '(';
		e = null;
		infix = new InfixExpression("( 1 - 2 + 6 * 8 - 10 ) - 1 )");
		try {
			infix.postfix();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Missing ')'" if a '(' is left unmatched on the stack at the end of
		// the scan;
		e = null;
		infix = new InfixExpression("( ( 1 + 3 ) / 2");
		try {
			infix.postfix();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Invalid character" if a scanned char is neither a digit nor an
		// operator;
		e = null;
		infix = new InfixExpression("1 + 2 = + 3");
		try {
			infix.postfix();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);
	}

	@Test
	public void testEvaluateExpressionFormatException() {
		Throwable e = null;

		// "Invalid character" if encountering a character that is not a digit,
		// an operator
		// or a whitespace (blank, tab);
		Expression exp = new PostfixExpression("1 2 + A %");
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Too many operands" if operandStack is non-empty at the end of
		// evaluation;
		e = null;
		exp = new PostfixExpression("1 2 + 7");
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Too many operators" if getOperands() throws NoSuchElementException;
		e = null;
		exp = new PostfixExpression("1 2 + + 7");
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Divide by zero" if division or modulo is the current operation and
		// rightOperand == 0;
		e = null;
		exp = new PostfixExpression("1 2 + 2 2 - /");
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "0^0" if the current operation is "^" and leftOperand == 0 and
		// rightOperand == 0;
		e = null;
		exp = new PostfixExpression("2 2 - 2 2 - ^");
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof ExpressionFormatException);

		// "Variable <name> was not assigned a value", where <name> is the name
		// of the variable.
		e = null;
		exp = new PostfixExpression("2 a * b /");
		fillTable(new char[] { 'a' }, new int[] { 1 });
		exp.setVarTable(varTable);
		try {
			exp.evaluate();
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof UnassignedVariableException);

	}

}
