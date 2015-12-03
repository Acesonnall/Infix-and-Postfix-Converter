package edu.iastate.cs228.hw4;

public class Operator implements Comparable<Operator> {
	public char operator; // operator

	private int inputPrecedence; // input precedence of operator in the range
									// [0, 5]
	private int stackPrecedence; // stack precedence of operator in the range
									// [-1, 3]

	/**
	 * Constructor
	 * 
	 * @param ch
	 */
	public Operator(char ch) {
		operator = ch;
		inputPrecedence = (operator == '+' || operator == '-' ? 1
				: operator == '*' || operator == '/' || operator == '%' ? 2
						: operator == '^' ? 4 : operator == '(' ? 5 : 0);
		stackPrecedence = (operator == '+' || operator == '-' ? 1
				: operator == '*' || operator == '/' || operator == '%' ? 2
						: operator == '^' ? 3 : operator == '(' ? -1 : 0);
	}

	/**
	 * Returns 1, 0, -1 if the stackPrecedence of this operator is greater than,
	 * equal to, or less than the inputPrecedence of the parameter operator op.
	 * It's for determining whether this operator on the stack should be output
	 * before pushing op onto the stack.
	 */
	@Override
	public int compareTo(Operator op) {
		return Integer.compare(this.stackPrecedence, op.inputPrecedence);
	}

	/**
	 * 
	 * @return char Returns the operator character.
	 */
	public char getOp() {
		return operator;
	}
}
