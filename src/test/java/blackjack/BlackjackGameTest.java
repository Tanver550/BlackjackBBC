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

    // test scenario 2 and 3
    @Test
    void Scenario2_3_testPlayerHitsAndStands() {

        Player player = new Player("TestPlayer");

        player.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        player.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertEquals(13, player.getScore());

        player.addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));
        assertEquals(20, player.getScore());

        assertEquals(20, player.getScore());

    }

    @Test
    void scenario5_testPlayerBust() {
        Player player = new Player("TestPlayer");

        player.addCard(new Card(Rank.KING, Suit.HEARTS));
        player.addCard(new Card(Rank.QUEEN, Suit.DIAMONDS));
        player.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertTrue(player.getScore() > 21, "Player should bust with a total > 21");
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

        // assert that the player got exactly 2 cards
        assertEquals(1, game.getPlayers().size(), "Should have exactly 1 player");
        Player alice = game.getPlayers().get(0);
        assertEquals(2, alice.getHand().size(), "Player should be dealt exactly 2 cards at the start of the round");
    }
}

