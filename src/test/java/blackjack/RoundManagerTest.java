package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for RoundManager focusing on splitting,
 * dealing, and overall round flow in a controlled scenario.
 */
public class RoundManagerTest {
    /**
     * create a mock deck with 6 cards,
     * starts game, 1 player, stands immediately
     */

    @Test
    void testPlayRound_SimpleNoSplit() {

        List<Card> riggedCards = new ArrayList<>();

        // 2 player cards
        riggedCards.add(new Card(Rank.TEN,  Suit.HEARTS));
        riggedCards.add(new Card(Rank.FIVE, Suit.HEARTS));

        // dealer cards
        riggedCards.add(new Card(Rank.NINE, Suit.SPADES));
        riggedCards.add(new Card(Rank.EIGHT, Suit.SPADES));

        // extra cards so dont run out
        riggedCards.add(new Card(Rank.TWO,  Suit.CLUBS));
        riggedCards.add(new Card(Rank.SEVEN, Suit.DIAMONDS));

        Deck mockDeck = new MockDeck(riggedCards);

        // user stand immediately
        String userInput = "stand\nno\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        // Create 1 player + dealer
        Player player = new Player("P1");
        player.setHitStrategy(new UserHitStrategy(io));
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // play a single round
        manager.playRound(List.of(player), dealer);

        // Assert final conditions:
        // Player - Should have exactly 2 cards in the first hand
        assertEquals(1, player.getHands().size(), "Player should have only 1 hand (no splits).");
        assertEquals(2, player.getHands().get(0).getCards().size(), "After standing immediately, player should end with 2 cards.");

        // Score = 10 + 5 => 15
        assertEquals(15, player.getScore(), "Expected player's total to be 15 (TEN + FIVE).");

        // Dealer -  1 hand, starting with 9 + 8 => 17 => should stand automatically
        assertEquals(1, dealer.getHands().size(), "Dealer has 1 hand ");
        assertEquals(2, dealer.getHands().get(0).getCards().size(), "Dealer should have exactly 2 cards if at 17 or above from the start.");

        assertEquals(17, dealer.getScore(), "Dealer's initial total = 9 + 8 => 17 => stands, no extra draw.");
    }

    /**
     * 2) Scenario: single player gets a pair of 8s, chooses to split once,
     * then stands on both hands. Dealer is below 17 and draws exactly one card.
     */
    @Test
    void testPlayRound_SplitOnce() {
        List<Card> riggedCards = new ArrayList<>();

        // Player initial pair
        riggedCards.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
        riggedCards.add(new Card(Rank.EIGHT, Suit.CLUBS));

        // Dealer initial 2 =>  TEN + SIX => 16
        riggedCards.add(new Card(Rank.TEN, Suit.SPADES));
        riggedCards.add(new Card(Rank.SIX,  Suit.HEARTS));

        // Split => each old/new hand gets 1 more card:
        riggedCards.add(new Card(Rank.TWO, Suit.CLUBS));
        riggedCards.add(new Card(Rank.THREE, Suit.CLUBS));

        // Dealer hits once
        riggedCards.add(new Card(Rank.FIVE, Suit.SPADES));  // dealer = 16 + 5 => 21
        // more cards so deck isnt empty
        riggedCards.add(new Card(Rank.FOUR, Suit.DIAMONDS));

        Deck mockDeck = new MockDeck(riggedCards);

        // user wants to split, and then stand on both hands
        String userInput = "yes\nstand\nstand\nno\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        // Create a single player, set the user strategy
        Player player = new Player("Splitter");
        player.setHitStrategy(new UserHitStrategy(io));
        // Dealer
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // Act
        manager.playRound(List.of(player), dealer);

        // Player => after splitting => 2 hands
        // each should have 2 cards
        assertEquals(2, player.getHands().size(), "Player should have 2 hands after splitting once.");

        // Hand #1 => [EIGHT of DIAMONDS, TWO of CLUBS]
        // Hand #2 => [EIGHT of CLUBS, THREE of CLUBS]
        assertEquals(2, player.getHands().get(0).getCards().size(), "First splitted hand has 2 cards.");
        assertEquals(2, player.getHands().get(1).getCards().size(), "Second splitted hand has 2 cards.");

        // Dealer => started with [TEN of SPADES, SIX of HEARTS] => 16 => should draw 1 card so + 5 so total=21
        assertEquals(1, dealer.getHands().size(), "Dealer has 1 hand");
        assertEquals(3, dealer.getHands().get(0).getCards().size(), "Dealer should draw exactly 1 extra card from 16 => now 21.");

        // Confirm final dealer score = 21
        assertEquals(21, dealer.getScore(), "Dealer's final total should be 21 (10 + 6 + 5).");
    }

}
