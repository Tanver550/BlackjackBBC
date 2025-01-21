package blackjack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the ConsoleIO class.
 */
class ConsoleIOTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsoleIO consoleIO;

    @BeforeEach
    void setUp() {
        // redirect System.out to the test output stream
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
        // Clear out the output stream
        outContent.reset();
    }

    // test if text can be printed
    @Test
    void testPrintAndPrintln() {
        consoleIO = new ConsoleIO();
        consoleIO.print("Hello, ");
        consoleIO.println("world!");

        // Verify what got printed
        String printed = outContent.toString();

        boolean ok = printed.contains("Hello, world!");
        assertTrue(ok, "Expected 'Hello, world!' to appear in the output");
    }

    @Test
    void testReadLine() {
        String inputString = "test input line\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        consoleIO = new ConsoleIO();
        String result = consoleIO.readLine("Enter something: ");

        // check what was printed to console
        String printed = outContent.toString();
        // The prompt "Enter something: " should have been printed
        boolean hasPrompt = printed.contains("Enter something:");

        assertTrue(hasPrompt, "Should print the prompt before reading line");
        assertEquals("test input line", result, "Should read the input line");
    }

    @Test
    void testReadInt_ValidNumber() {
        String inputString = "42\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        consoleIO = new ConsoleIO();
        int number = consoleIO.readInt("Enter a number: ");

        String printed = outContent.toString();
        boolean hasPrompt = printed.contains("Enter a number:");

        assertTrue(hasPrompt, "Should print the prompt before reading integer");
        assertEquals(42, number, "Should parse the integer 42 correctly");
    }

    @Test
    void testReadInt_InvalidThenValid() {
        //  simulate entering "abc" (invalid), then "10" (valid)
        String inputString = "abc\n10\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

        consoleIO = new ConsoleIO();
        int number = consoleIO.readInt("Enter a number: ");

        // expect that it should have prompted again after the invalid "abc"
        String printed = outContent.toString();
        boolean hasErrorMessage = printed.contains("Invalid input. Please enter a number.");
        assertTrue(hasErrorMessage, "Expected an error message after invalid input.");

        assertEquals(10, number, "Expected final parsed number to be 10 after retry");
    }
}
