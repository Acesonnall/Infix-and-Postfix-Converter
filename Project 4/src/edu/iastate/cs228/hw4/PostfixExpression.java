package edu.iastate.cs228.hw4;

/**
 *  
 * @author
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PostfixExpression extends Expression {
	private int leftOperand; // left operand for the current evaluation step
	private int rightOperand; // right operand for the current evaluation step

	private PureStack<Integer> operandStack; // stack of operands

	/**
	 * Constructor stores the input postfix string and initializes the operand
	 * stack.
	 * 
	 * @param st
	 *            input postfix string.
	 * @param varTbl
	 *            hash map that stores variables from the postfix string and
	 *            their values.
	 */
	public PostfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super(st, varTbl);
		operandStack = new ArrayBasedStack<Integer>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public PostfixExpression(String s) {
		super(s);
		operandStack = new ArrayBasedStack<Integer>();
	}

	/**
	 * Outputs the postfix expression according to the format in the project
	 * description. Needs to first call the method toStringHelper() from the
	 * class Expression.
	 */
	@Override
	public String toString() {
		// remove spaces between parentheses and operands
		return postfixExpression.replaceAll("\\s+", " ").trim();
	}

	/**
	 * Resets the postfix expression.
	 * 
	 * @param st
	 */
	public void resetPostfix(String st) {
		postfixExpression = st;
	}

	/**
	 * Scan the postfixExpression and carry out the following:
	 * 
	 * 1. Whenever an integer is encountered, push it onto operandStack. 2.
	 * Whenever an operator is encountered, invoke it on the two elements popped
	 * from operandStack, and push the result back onto the stack. 3. On
	 * encountering a character that is not a digit, an operator, or a blank
	 * space, stop the evaluation.
	 * 
	 * @return value of the postfix expression
	 * @throws ExpressionFormatException
	 *             with one of the messages below:
	 * 
	 *             -- "Invalid character" if encountering a character that is
	 *             not a digit, an operator or a whitespace (blank, tab); --
	 *             "Too many operands" if operandStack is non-empty at the end
	 *             of evaluation; -- "Too many operators" if getOperands()
	 *             throws NoSuchElementException; -- "Divide by zero" if
	 *             division or modulo is the current operation and rightOperand
	 *             == 0; -- "0^0" if the current operation is "^" and
	 *             leftOperand == 0 and rightOperand == 0; -- self-defined
	 *             message if the error is not one of the above.
	 * 
	 *             UnassignedVariableException if the operand as a variable does
	 *             not have a value stored in the hash map. In this case, the
	 *             exception is thrown with the message
	 * 
	 *             -- "Variable <name> was not assigned a value", where
	 *             <name> is the name of the variable.
	 * 
	 */
	@Override
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		Scanner items = new Scanner(postfixExpression);
		String nextItem;
		int cumulativeRank = 0;
		while (items.hasNext()) {
			nextItem = items.next();

			if (isInt(nextItem)) {
				cumulativeRank++;
				operandStack.push(Integer.parseInt(nextItem));

			} else if (isVariable(nextItem.charAt(0))) {
				if (varTable.containsKey(nextItem.charAt(0))) {
					cumulativeRank++;
					operandStack.push(varTable.get(nextItem.charAt(0)));
				} else {
					items.close();
					throw new UnassignedVariableException("Variable " + nextItem + " was not assigned a value");
				}
			} else if (isOperator(nextItem.charAt(0))) {
				cumulativeRank--;

				try {
					getOperands();
				} catch (NoSuchElementException e) {
					items.close();
					throw new ExpressionFormatException("Too many operators");
				}

				if ((nextItem.charAt(0) == '%' || nextItem.charAt(0) == '/') && rightOperand == 0) {
					items.close();
					throw new ExpressionFormatException("Divide by zero");
				} else if (nextItem.charAt(0) == '^' && (rightOperand == 0 && leftOperand == 0)) {
					items.close();
					throw new ExpressionFormatException("0^0");
				}

				operandStack.push(compute(nextItem.charAt(0)));
			} else {
				items.close();
				throw new ExpressionFormatException("Invalid character");
			}
		}
		items.close();
		if (cumulativeRank != 1) {
			items.close();
			throw new ExpressionFormatException("Too many operands");
		}
		return operandStack.pop();

	}

	/**
	 * Pops the right and left operands from operandStack, and assign them to
	 * rightOperand and leftOperand, respectively. The stack must have at least
	 * two entries. Otherwise, throws NoSuchElementException.
	 */
	private void getOperands() throws NoSuchElementException {
		// When the stack is empty
		if (operandStack.size() < 2) {
			throw new NoSuchElementException();
		}

		rightOperand = operandStack.pop();
		leftOperand = operandStack.pop();
	}

	/**
	 * Computes "leftOperand op rightOprand".
	 * 
	 * @param op
	 *            operator that acts on leftOperand and rightOperand.
	 * @return
	 */
	private int compute(char op) {

		// Performs arithmetic
		switch (op) {
		case '+':
			return leftOperand + rightOperand;
		case '-':
			return leftOperand - rightOperand;
		case '*':
			return leftOperand * rightOperand;
		case '/':
			return leftOperand / rightOperand;
		case '%':
			return leftOperand % rightOperand;
		case '^':
			return (int) Math.pow(leftOperand, rightOperand);
		}

		// When there are no valid operators
		assert false;
		return 0;
	}
}
