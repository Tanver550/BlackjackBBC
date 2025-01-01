package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class BlackjackGameTest {

    @Test
    void testInitialisation() {
        // Simulate input for 3 players
        ByteArrayInputStream input = new ByteArrayInputStream("3\na\nb\nc\n".getBytes());
        System.setIn(input);

        BlackjackGame game = new BlackjackGame();
        game.initialiseGame();

        // Validate players
        assertEquals(3, game.getPlayers().size());
        assertEquals("a", game.getPlayers().get(0).getName());
        assertEquals("b", game.getPlayers().get(1).getName());
        assertEquals("c", game.getPlayers().get(2).getName());

        // Validate dealer
        assertNotNull(game.getDealer());
        assertEquals("Dealer", game.getDealer().getName());
    }

    // test scenario 2 and 3
    @Test
    void Scenario2_3_testPlayerHitsAndStands() {
        Player player = new Player("TestPlayer");
        Scanner scanner = new Scanner(System.in);

        // Add initial cards
        player.addCard(new Card("8", "Hearts", 8), false, scanner); // Player-specific logic
        player.addCard(new Card("5", "Clubs", 5), false, scanner);
        assertEquals(13, player.getScore());

        // Simulate hitting, so the score is added
        player.addCard(new Card("7", "Diamonds", 7), false, scanner); // Total: 20
        assertEquals(20, player.getScore());

        // Simulate standing, the plaeyrs score doesnt change as they have not drawn a card
        assertEquals(20, player.getScore()); // Score remains unchanged

    }

    @Test
    void scenario5_testPlayerBust() {
        Player player = new Player("TestPlayer");
        Scanner scanner = new Scanner(System.in);

        // Add cards that cause a bust
        player.addCard(new Card("King", "Hearts", 10), false, scanner);
        player.addCard(new Card("Queen", "Diamonds", 10), false, scanner);
        player.addCard(new Card("5", "Clubs", 5), false, scanner); // Total: 25 (Bust)

        assertTrue(player.getScore() > 21); // Verify bust condition
    }

    // test to see if player is dealt exacttly 2 card in the opening hand
    //enter player name, (game deals cards) the stand, after staniding the player should have 2 cards in the hand
    @Test
    void scenario1_testOpeningHand() {
        String userInput = "1\nTanver\nstand\nno\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        // Create and run the game
        BlackjackGame game = new BlackjackGame();
        game.play();

        // Now we can assert that the only player "Alice" got exactly 2 cards
        assertEquals(1, game.getPlayers().size(), "Should have exactly 1 player");
        Player alice = game.getPlayers().get(0);
        assertEquals(2, alice.getHand().size(),
                "Player should be dealt exactly 2 cards at the start of the round");
    }


}

