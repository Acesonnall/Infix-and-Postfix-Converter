package edu.iastate.cs228.hw4;

import java.util.HashMap;

public abstract class Expression {
	protected String postfixExpression;
	protected HashMap<Character, Integer> varTable; // hash map to store
													// variables in the

	protected Expression() {
		// no implementation needed
		// removable when you are done
	}

	/**
	 * Initialization with a provided hash map.
	 * 
	 * @param varTbl
	 */
	protected Expression(String st, HashMap<Character, Integer> varTbl) {
		// No characters other than the six operator characters, the two
		// parentheses, the ten digits, and the 26 English letters (in smaller
		// case) are expected from the input. Your code need to check on this.
		postfixExpression = st;
		varTable = varTbl;
	}

	/**
	 * Initialization with a default hash map.
	 * 
	 * @param st
	 */
	protected Expression(String st) {
		varTable = new HashMap<Character, Integer>();
		postfixExpression = st;
	}

	/**
	 * Useful with the d
	 * 
	 * @param varTbl
	 */
	public void setVarTable(HashMap<Character, Integer> varTbl) {
		varTable.clear();
		varTable.putAll(varTbl);
	}

	/**
	 * Evaluates the infix or postfix expression.
	 * 
	 * @return value of the expression
	 * @throws ExpressionFormatException,
	 *             UnassignedVariableException
	 */
	public abstract int evaluate() throws ExpressionFormatException, UnassignedVariableException;

	// --------------------------------------------------------
	// Helper methods for InfixExpression and PostfixExpression
	// --------------------------------------------------------

	/**
	 * Check if a string represents an integer. You may call the static method
	 * Integer.parseInt().
	 * 
	 * @param s
	 * @return
	 */
	protected static boolean isInt(String s) {
		if (s == null) {
			return false;
		}
		int length = s.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (s.charAt(0) == '-') {
			return false;

			// Alternate case checks for negatives:
			// if (length == 1) {
			// return false;
			// }
			// i = 1;
		}
		for (; i < length; i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if a char represents an operator, i.e., one of '+', '-', '*', '/',
	 * '%', '^'.
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '&' || c == '^' || c == ')' || c == '(' || c == '%'
				? true : false;
	}

	/**
	 * Check if a char is a variable, i.e., a lower case English letter.
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isVariable(char c) {
		return c < 'a' || c > 'z' ? false : true;
	}
}
