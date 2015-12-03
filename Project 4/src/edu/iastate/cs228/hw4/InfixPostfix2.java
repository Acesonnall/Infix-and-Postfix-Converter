package edu.iastate.cs228.hw4;

import java.util.Scanner;

public class InfixPostfix2 {

	public static void main(String[] args) {
		initialize();
		evaluate();
	}

	// Helpers

	private static void initialize() {
		System.out.println("Evaluation of Infix and Postfix Expressions");
		System.out.println("keys: 1 (standard input) 2 (file input) 3 (exit)");
		System.out.println("(Enter “I” before an infix expression, “P” before a postfix expression)");
	}

	private static void evaluate() {
		int key = 0;
		int trial = 0;
		Scanner scan = new Scanner(System.in);
		while (true) {
			trial++;
			System.out.println("Trial " + trial + ": ");
			key = scan.nextInt();
			if (key == 1) {
				standard_input();
			} else if (key == 2) {
				file_input();
			} else {
				break;
			}
		}
		scan.close();
		exit();
	}

	private static void standard_input() {

	}

	private static void file_input() {

	}

	private static void exit() {
		System.exit(0);
	}
}
