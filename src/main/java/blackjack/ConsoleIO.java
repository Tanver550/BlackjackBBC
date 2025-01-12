package blackjack;

import java.util.Scanner;

// handles all the logic for the for the BlackjackGame. means less code like system.out is required in the BlackJackGame class

public class ConsoleIO {
    private final Scanner scanner;

    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a message without a newline
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Prints a message with a newline
     */
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Reads a line of input from the user
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Reads an integer from the user. Retries if the input is invalid.
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
