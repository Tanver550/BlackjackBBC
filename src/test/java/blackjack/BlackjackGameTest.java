package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

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

    @Test
    void testPlayerHitsAndStands() {
        Player player = new Player("TestPlayer");

        // Add initial cards
        player.addCard(new Card("8", "Hearts", 8));
        player.addCard(new Card("5", "Clubs", 5));
        assertEquals(13, player.getScore());

        // Simulate hitting
        player.addCard(new Card("7", "Diamonds", 7)); // Total: 20
        assertEquals(20, player.getScore());

        // Simulate standing (no change in score)
        assertEquals(20, player.getScore());
    }

    @Test
    void testPlayerBust() {
        Player player = new Player("TestPlayer");

        // Add cards that cause a bust
        player.addCard(new Card("King", "Hearts", 10));
        player.addCard(new Card("Queen", "Diamonds", 10));
        player.addCard(new Card("5", "Clubs", 5)); // Total: 25 (Bust)

        assertTrue(player.getScore() > 21);
    }


}
