package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class InfixPostfix {

	/**
	 * Repeatedly evaluates input infix and postfix expressions. See the project
	 * description for the input description. It constructs a HashMap object for
	 * each expression and passes it to the created InfixExpression or
	 * PostfixExpression object.
	 * 
	 * @param args
	 * @throws ExpressionFormatException
	 * @throws UnassignedVariableException
	 * @throws FileNotFoundException
	 **/
	public static void main(String[] args)
			throws ExpressionFormatException, UnassignedVariableException, FileNotFoundException {
		System.out.println(
				"Evaluation of Infix and Postfix Expressions\nkeys: 1 (standard input) 2 (file input) 3 (exit)\n(Enter \"I\" before an infix expression, \"P\" before a postfix expression)");
		int trial = 0;
		int key = 0;
		Scanner scan = new Scanner(System.in);
		while (true) {
			Scanner scan2 = new Scanner(System.in);
			System.out.print("Trail " + (trial + 1) + ": ");
			key = scan.nextInt();
			if (key == 1) {
				System.out.println(key + "\nExpression: ");
				String userExp = scan2.nextLine();

				if (userExp.contains("I")) {
					String trimmedUExp = userExp.replaceFirst("I", "");
					InfixExpression userInfix = new InfixExpression(trimmedUExp);
					System.out.println("Infix form: " + userInfix.toString());
					PostfixExpression convertedInfix;
					try {
						userInfix.postfix();
						convertedInfix = new PostfixExpression(userInfix.postfixExpression);
					} catch (ExpressionFormatException e) {
						scan.close();
						scan2.close();
						throw e;
					}
					System.out.println("Postfix form: " + convertedInfix);
					if (userInfix.postfixExpression.contains("[a-zA-Z]+")) {
						System.out.println("where");
					}
					Scanner scan3 = new Scanner(userInfix.postfixExpression);
					Scanner scan4 = new Scanner(System.in);
					HashMap<Character, Integer> vrTbl = new HashMap<Character, Integer>();
					while (scan3.hasNext()) {
						char nextItem = scan3.next().charAt(0);
						if (Expression.isVariable(nextItem) && !vrTbl.containsKey(nextItem)) {
							System.out.println(nextItem + " = ");
							vrTbl.put(nextItem, scan4.nextInt());
						}
					}
					scan3.close();
					scan4.close();
					try {
						System.out.println("Expression value: " + new InfixExpression(trimmedUExp, vrTbl).evaluate());
					} catch (UnassignedVariableException e) {
						scan.close();
						scan2.close();
						scan3.close();
						scan4.close();
						throw e;
					}
				} else if (userExp.contains("P")) {
					String trimmedUExp = userExp.replaceFirst("P", "");
					PostfixExpression userPostfix = new PostfixExpression(trimmedUExp);
					System.out.println("Postfix form: " + userPostfix.toString());
					if (userPostfix.postfixExpression.contains("[a-zA-Z]+")) {
						System.out.println("where");
					}
					Scanner scan3 = new Scanner(userPostfix.postfixExpression);
					Scanner scan4 = new Scanner(System.in);
					HashMap<Character, Integer> vrTbl = new HashMap<Character, Integer>();
					while (scan3.hasNext()) {
						char nextItem = scan3.next().charAt(0);
						if (Expression.isVariable(nextItem)) {
							System.out.println(nextItem + " = ");
							vrTbl.put(nextItem, scan4.nextInt());
						}
					}
					scan3.close();
					scan4.close();
					try {
						System.out.println("Expression value: " + new PostfixExpression(trimmedUExp, vrTbl).evaluate());
					} catch (UnassignedVariableException e) {
						scan.close();
						scan2.close();
						scan3.close();
						scan4.close();
						throw e;
					}
				} else {
					System.out.println("P or I not prefixed before expression");
				}
			} else if (key == 2) {
				System.out.print("Input from a file\nEnter file name: ");
				String inputFileName = scan.next();

				Scanner scan5 = new Scanner(new File(inputFileName));
				while (scan5.hasNextLine()) {
					String fileExp = scan5.nextLine();
					if (fileExp.contains("I")) {
						String trimmedUExp = fileExp.replaceFirst("I", "");
						InfixExpression userInfix = new InfixExpression(trimmedUExp);
						System.out.println("Infix form: " + userInfix.toString());
						PostfixExpression convertedInfix;
						try {
							userInfix.postfix();
							convertedInfix = new PostfixExpression(userInfix.postfixExpression);
						} catch (ExpressionFormatException e) {
							scan5.close();
							throw e;
						}
						System.out.println("Postfix form: " + convertedInfix);

						Scanner scan6 = new Scanner(userInfix.postfixExpression);
						HashMap<Character, Integer> vrTbl = new HashMap<Character, Integer>();
						while (scan6.hasNext()) {
							if (Expression.isVariable(scan6.next().charAt(0))) {
								System.out.println("where");
								String mappings = getMappings(fileExp);
								if (mappings != "") {
									if (matchMappings(inputFileName, mappings, vrTbl)) {
										for (char k : vrTbl.keySet()) {
											System.out.println(k + " = " + vrTbl.get(k));
										}
										try {
											System.out.println("Expression value: "
													+ new InfixExpression(trimmedUExp, vrTbl).evaluate());
										} catch (UnassignedVariableException e) {
											scan6.close();
											throw e;
										}
										break;
									} else {
										Scanner scan3 = new Scanner(trimmedUExp);
										Scanner scan4 = new Scanner(System.in);
										while (scan3.hasNext()) {
											char nextItem = scan3.next().charAt(0);
											if (Expression.isVariable(nextItem) && !vrTbl.containsKey(nextItem)) {
												System.out.println(nextItem + " = ");
												vrTbl.put(nextItem, scan4.nextInt());
											}
										}
										scan3.close();
										scan4.close();
										try {
											System.out.println("Expression value: "
													+ new InfixExpression(trimmedUExp, vrTbl).evaluate());
										} catch (UnassignedVariableException e) {
											scan3.close();
											scan4.close();
											throw e;
										}
										break;
									}
								}
							} else if (!scan6.hasNext()) {
								try {
									System.out.println(
											"Expression value: " + new InfixExpression(trimmedUExp, vrTbl).evaluate());
								} catch (UnassignedVariableException e) {
									scan6.close();
									throw e;
								}
								scan6.close();
								break;
							}
						}
					} else if (fileExp.contains("P")) {
						String trimmedUExp = fileExp.replaceFirst("P", "");
						PostfixExpression userPostfix = new PostfixExpression(trimmedUExp);
						System.out.println("Postfix form: " + userPostfix.toString());
						Scanner scan6 = new Scanner(userPostfix.postfixExpression);
						HashMap<Character, Integer> vrTbl = new HashMap<Character, Integer>();
						while (scan6.hasNext()) {
							if (Expression.isVariable(scan6.next().charAt(0))) {
								System.out.println("where");
								String mappings = getMappings(fileExp);
								if (mappings != "") {
									if (matchMappings(inputFileName, mappings, vrTbl)) {
										for (char k : vrTbl.keySet()) {
											System.out.println(k + " = " + vrTbl.get(k));
										}
										try {
											System.out.println("Expression value: "
													+ new PostfixExpression(trimmedUExp, vrTbl).evaluate());
										} catch (UnassignedVariableException e) {
											scan6.close();
											throw e;
										}
										scan6.close();
										break;
									} else {
										Scanner scan3 = new Scanner(trimmedUExp);
										Scanner scan4 = new Scanner(System.in);
										while (scan3.hasNext()) {
											char nextItem = scan3.next().charAt(0);
											if (Expression.isVariable(nextItem) && !vrTbl.containsKey(nextItem)) {
												System.out.println(nextItem + " = ");
												vrTbl.put(nextItem, scan4.nextInt());
											}
										}
										scan3.close();
										scan4.close();
										try {
											System.out.println("Expression value: "
													+ new PostfixExpression(trimmedUExp, vrTbl).evaluate());
										} catch (UnassignedVariableException e) {
											scan3.close();
											scan4.close();
											throw e;
										}
									}
								}
							} else if (!scan6.hasNext()) {
								try {
									System.out.println("Expression value: "
											+ new PostfixExpression(trimmedUExp, vrTbl).evaluate());
								} catch (UnassignedVariableException e) {
									scan6.close();
									throw e;
								}
								scan6.close();
								break;
							}
						}
					}
				}

			} else {
				break;
			}
			scan2.close();
		}
		scan.close();
		System.exit(0);
	}

	// helper methods if needed
	private static String getMappings(String f) throws FileNotFoundException {
		Scanner scan = new Scanner(f);
		String mappings = "";
		while (scan.hasNext()) {
			char temp = scan.next().charAt(0);
			if (Expression.isVariable(temp)) {
				mappings += temp + " ";
			}
		}
		scan.close();
		return mappings;
	}

	private static boolean matchMappings(String f, String m, HashMap<Character, Integer> vT)
			throws FileNotFoundException {
		Scanner scan = new Scanner(new File(f));
		boolean state = false;
		while (scan.hasNextLine()) {
			Scanner scan2 = new Scanner(m);
			String temp = scan.nextLine();
			while (scan2.hasNext()) {
				String temp2 = scan2.next();
				if (temp.trim().startsWith(temp2)) {
					state = true;
					Scanner scan3 = new Scanner(temp);
					String temp3 = "";
					while (scan3.hasNext()) {
						temp3 = scan3.next();
					}
					vT.put(temp2.charAt(0), Integer.parseInt(temp3));
				}
			}
			scan2.close();
		}
		scan.close();
		return state;
	}
}
