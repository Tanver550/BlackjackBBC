package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for BlackjackGame,
 * using a rigged deck (MockDeck) and simulated console input.
 */
public class BlackjackGameTest {


    // test to see when Ace is drawn, selecting value correclt adds to score
    @Test
    void testPlay_SinglePlayerAceChoice() {

        // create a player draw an ace, set the value to 1
        String userInput = "1\nTanver\nhit\n1\nstand\nno\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        List<Card> rigged = new ArrayList<>();
        rigged.add(new Card(Rank.EIGHT, Suit.CLUBS));
        rigged.add(new Card(Rank.THREE, Suit.DIAMONDS));
        rigged.add(new Card(Rank.NINE, Suit.SPADES));
        rigged.add(new Card(Rank.SEVEN, Suit.HEARTS));
        // next card => Ace of Hearts for the player hit
        rigged.add(new Card(Rank.ACE, Suit.HEARTS));

        rigged.add(new Card(Rank.FIVE, Suit.DIAMONDS));

        Deck mockDeck = new MockDeck(rigged);

        // create game, set rigged deck
        BlackjackGame game = new BlackjackGame();
        game.setDeck(mockDeck);

        // run it
        game.play();

        assertEquals(1, game.getPlayers().size());
        Player Tanver = game.getPlayers().get(0);


        assertEquals(1, Tanver.getHands().size(), "No splits => should have 1 hand.");
        int cardCount = Tanver.getHands().get(0).getCards().size();
        assertEquals(3, cardCount, "Player hit exactly once => 3 cards in final hand.");

        int dealerCardCount = game.getDealer().getHands().get(0).getCards().size();
        assertTrue(dealerCardCount >= 2 && dealerCardCount <= 5, "Dealer might have drawn if below 17.");
    }


    // test to see if the player can split, then has 2 hands of cards
    @Test
    void testPlay_SinglePlayerSplit() {

        String input = "1\nTanver\nyes\nstand\nstand\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<Card> rigged = new ArrayList<>();
        rigged.add(new Card(Rank.EIGHT, Suit.HEARTS));
        rigged.add(new Card(Rank.EIGHT, Suit.CLUBS));
        rigged.add(new Card(Rank.TEN, Suit.SPADES));
        rigged.add(new Card(Rank.SIX, Suit.DIAMONDS));

        rigged.add(new Card(Rank.TWO, Suit.HEARTS));
        rigged.add(new Card(Rank.THREE, Suit.CLUBS));

        rigged.add(new Card(Rank.FIVE, Suit.HEARTS));

        rigged.add(new Card(Rank.FOUR, Suit.HEARTS));

        Deck mockDeck = new MockDeck(rigged);

        // game
        BlackjackGame game = new BlackjackGame();
        game.setDeck(mockDeck);

        // run
        game.play();

        assertEquals(1, game.getPlayers().size());
        Player tanver = game.getPlayers().get(0);

        // after splitting => 2 hands
        assertEquals(2, tanver.getHands().size(), "Should have 2 hands after splitting pair of 8s.");

        // Each splitted hand => 2 or 3 cards depending on hits
        for (Hand h : tanver.getHands()) {
            assertEquals(2, h.getCards().size(), "We said 'stand' on each splitted hand => each remains 2 cards.");
        }

        int dealerCount = game.getDealer().getHands().get(0).getCards().size();
        assertTrue(dealerCount >= 2 && dealerCount <= 5);
    }
}
