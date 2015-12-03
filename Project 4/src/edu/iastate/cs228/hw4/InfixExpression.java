package edu.iastate.cs228.hw4;

/**
 *  
 * @author
 *
 */

import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix
 * conversion using one stack, and evaluates the converted postfix expression.
 *
 */

public class InfixExpression extends Expression {
	private String infixExpression; // the infix expression to convert
	private boolean postfixReady = false; // postfix already generated if true

	private PureStack<Operator> operatorStack; // stack of operators

	/**
	 * Constructor stores the input infix string, and initializes the operand
	 * stack and the hash map.
	 * 
	 * @param st
	 *            input infix string.
	 * @param varTbl
	 *            hash map storing all variables in the infix expression and
	 *            their values.
	 */
	public InfixExpression(String st, HashMap<Character, Integer> varTbl) {
		infixExpression = st;
		varTable = varTbl;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public InfixExpression(String s) {
		infixExpression = s;
		varTable = new HashMap<Character, Integer>();
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Outputs the infix expression according to the format in the project
	 * description. It first calls the method toStringHelper() from the class
	 * Expression.
	 */
	@Override
	public String toString() {
		// remove spaces between parentheses and operands
		return infixExpression.replaceAll("\\s+", " ").trim().replace("( ", "(").replace(" )", ")");
	}

	/**
	 * @return equivalent postfix expression, or
	 * 
	 *         a null string if a call to postfix() inside the body (when
	 *         postfixReady == false) throws an exception.
	 * @throws ExpressionFormatException
	 */
	public String postfixString() throws ExpressionFormatException {
		try {
			postfix();
		} catch (ExpressionFormatException e) {
			return null;
		}
		return postfixExpression.replaceAll("\\s+", " ").trim();
	}

	/**
	 * Resets the infix expression.
	 * 
	 * @param st
	 */
	public void resetInfix(String st) {
		infixExpression = st;
		postfixReady = false;
	}

	/**
	 * Converts infixexpression to an equivalent postfix string stored at
	 * postfixExpression. If postfixReady == false, the method scans the
	 * infixExpression, and does the following (for algorithm details refer to
	 * the relevant PowerPoint slides):
	 * 
	 * 1. Skips a whitespace character. 2. Writes a scanned operand to
	 * postfixExpression. 3. If a scanned operator has a higher input precedence
	 * than the stack precedence of the top operator on the operatorStack, push
	 * it onto the stack. 4. Otherwise, first calls outputHigherOrEqual() before
	 * pushing the scanned operator onto the stack. No push if the scanned
	 * operator is ). 5. Keeps track of the cumulative rank of the infix
	 * expression.
	 * 
	 * During the conversion, catches errors in the infixExpression by throwing
	 * ExpressionFormatException with one of the following messages:
	 * 
	 * -- "Operator expected" if the cumulative rank goes above 1; --
	 * "Operand expected" if the rank goes below 0; -- "Missing '('" if scanning
	 * a ‘)’ results in popping the stack empty with no '('; -- "Missing ')'" if
	 * a '(' is left unmatched on the stack at the end of the scan; --
	 * "Invalid character" if a scanned char is neither a digit nor an operator;
	 * 
	 * If an error is not one of the above types, throw the exception with a
	 * message you define.
	 * 
	 * Sets postfixReady to true.
	 */
	public void postfix() throws ExpressionFormatException {
		postfixExpression = "";
		Scanner items = new Scanner(infixExpression);
		String nextItem;
		int cumulativeRank = 0;
		while (items.hasNext()) {
			nextItem = items.next();
			if (nextItem.charAt(0) == ' ') {
				nextItem = items.next();
			} else if (isInt(nextItem) || isVariable(nextItem.charAt(0))) {
				cumulativeRank++;
				if (cumulativeRank > 1) {
					items.close();
					throw new ExpressionFormatException("Operator expected");
				}
				postfixExpression += " " + nextItem + " ";
			} else if (isOperator(nextItem.charAt(0))) {
				cumulativeRank--;
				if (nextItem.charAt(0) == '(' || nextItem.charAt(0) == ')') {
					cumulativeRank++;
				}
				if (cumulativeRank < 0) {
					items.close();
					throw new ExpressionFormatException("Operand expected");
				}
				Operator nextOp = new Operator(nextItem.charAt(0));

				if (!operatorStack.isEmpty() && operatorStack.peek().compareTo(nextOp) == -1) {
					operatorStack.push(nextOp);
				} else {
					outputHigherOrEqual(nextOp);
					if (nextOp.operator != ')') {
						operatorStack.push(nextOp);
					} else if (nextOp.operator == ')' && !operatorStack.isEmpty()
							&& operatorStack.peek().operator == '(') {
						operatorStack.pop();
					} else if (nextOp.operator == ')' && operatorStack.isEmpty()) {
						items.close();
						throw new ExpressionFormatException("Missing '('");
					}
				}
			} else if (operatorStack.isEmpty() && nextItem.charAt(0) != ')') {
				Operator nextOp = new Operator(nextItem.charAt(0));
				operatorStack.push(nextOp); // When stack is empty.
			} else {
				items.close();
				throw new ExpressionFormatException("Invalid charater");
			}
		}
		while (!operatorStack.isEmpty() && operatorStack.peek().operator != '(') {
			postfixExpression += " " + operatorStack.pop().operator;
		}

		if (!operatorStack.isEmpty()) {
			items.close();
			throw new ExpressionFormatException("Missing ')'");
		}
		items.close();
		postfixReady = true;
	}

	/**
	 * This function first calls postfix() to convert infixExpression into
	 * postfixExpression. Then it creates a PostfixExpression object and calls
	 * its evaluate() method (which may throw an exception). It also passes any
	 * exception thrown by the evaluate() method of the PostfixExpression object
	 * upward the chain.
	 * 
	 * @return value of the infix expression
	 * @throws ExpressionFormatException,
	 *             UnassignedVariableException
	 */
	@Override
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		postfix();
		PostfixExpression infixPostfix = new PostfixExpression(postfixExpression, varTable);
		int result = 0;
		try {
			result = infixPostfix.evaluate();
		} catch (ExpressionFormatException | UnassignedVariableException e) {
			throw e; // Correct?
		}
		return result;
	}

	/**
	 * Pops the operator stack and outputs as long as the operator on the top of
	 * the stack has a stack precedence greater than or equal to the input
	 * precedence of the current operator op. Writes the popped operators to the
	 * string postfixExpression.
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the
	 * stack but does not write it to postfixExpression.
	 * 
	 * @param op
	 *            current operator
	 */
	private void outputHigherOrEqual(Operator op) {
		while (!operatorStack.isEmpty()) {
			if (operatorStack.peek().compareTo(op) == 1 || operatorStack.peek().compareTo(op) == 0) {
				if (op.operator == ')' && operatorStack.peek().operator == '(') {
					operatorStack.pop();
				} else {
					postfixExpression += " " + operatorStack.pop().operator;
				}
			} else {
				break;
			}
		}
	}

	// other helper methods if needed
}
