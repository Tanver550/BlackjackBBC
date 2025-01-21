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
     * Scenario 1:
     * Given I play a game of blackjack,
     *  When I am dealt my opening hand,
     *  Then I have two cards.
     */
    @Test
    void scenario1_testTwoInitialCardsDealt() {
        // create a rigged deck of at least 4 cards
        List<Card> riggedCards = List.of(
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.NINE,Suit.SPADES)
        );
        Deck mockDeck = new MockDeck(riggedCards);


        String userInput = "stand\nno\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        // Create 1 player + a dealer
        Player player = new Player("Tanver");
        player.setHitStrategy(new UserHitStrategy(io));
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // Play one round
        manager.playRound(List.of(player), dealer);

        // Now check the initial dealing => each player first hand has 2 cards,
        // and the dealer has 2 cards.
        assertEquals(2, player.getHands().get(0).getCards().size(), "Player should have exactly 2 cards in the opening hand.");
    }

    /**
     * Scenario 2:
     * set up the user to type "hit" once, then "stand" => confirm they end with 3 cards,
     * and the final score is correct.
     */
    @Test
    void scenario2_testHitReceiveExtraCardUpdatedScore() {
        // Rigged deck
        List<Card> riggedCards = new ArrayList<>();
        // Player => 7, 3 => total 10
        riggedCards.add(new Card(Rank.SEVEN, Suit.CLUBS));
        riggedCards.add(new Card(Rank.THREE, Suit.HEARTS));
        // Dealer => 9, 2 => total 11
        riggedCards.add(new Card(Rank.NINE, Suit.SPADES));
        riggedCards.add(new Card(Rank.TWO,  Suit.DIAMONDS));
        // Next => 5 => player hit => total 15
        riggedCards.add(new Card(Rank.FIVE, Suit.CLUBS));
        riggedCards.add(new Card(Rank.EIGHT, Suit.HEARTS));

        Deck mockDeck = new MockDeck(riggedCards);

        // means the player hits once, then stands, then "no" => no new round
        String userInput = "hit\nstand\nno\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        Player player = new Player("Tanver");
        player.setHitStrategy(new UserHitStrategy(io));
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        manager.playRound(List.of(player), dealer);

        // Player => started with 2 cards => hit => should now have 3 total
        int finalCardCount = player.getHands().get(0).getCards().size();
        assertEquals(3, finalCardCount, "After hitting once, player should have 3 cards.");

        // The final score should be 15 if  7+3=10 plus 5 => 15
        int finalScore = player.getHands().get(0).calculateScore();
        assertEquals(15, finalScore, "Expected final score to be 7+3+5=15 after one hit.");
    }

    /**
     * Scenario 3:
     * "Given I have a valid hand of cards,
     *  When I choose to 'stand',
     *  Then I receive no further cards
     *  And my score is evaluated."
     */
    @Test
    void scenario3_testStandNoExtraCard() {
        //  create a deck => 2 for player, 2 for dealer,
        // So the player can stand immediately => no extra draw
        List<Card> rigged = List.of(
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.SIX,  Suit.DIAMONDS),
                new Card(Rank.SEVEN,Suit.CLUBS),
                new Card(Rank.NINE,Suit.SPADES)

        );
        Deck mockDeck = new MockDeck(rigged);

        // user input => "stand\nno\n"
        String input = "stand\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        Player player = new Player("Tanver");
        player.setHitStrategy(new UserHitStrategy(io));
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        manager.playRound(List.of(player), dealer);

        // The player should never have drawn a 3rd card => still 2 in the first hand
        int finalCount = player.getHands().get(0).getCards().size();
        assertEquals(2, finalCount, "After standing immediately, the player remains with 2 cards.");

        // the final score for the player => 12
        int finalScore = player.getHands().get(0).calculateScore();
        assertEquals(12, finalScore, "Expected final score of 12 (10+2).");
    }

    /**
     * Scenario 5:
     * "Given my score is updated,
     *  When it is 22 or more
     *  Then I am 'bust' and do not have a valid hand."
     */
    @Test
    void scenario5_testBust() {
        // give the player a low initial total ,keep hitting,
        // eventually going over 21.

        List<Card> riggedCards = new ArrayList<>();
        // Player => 5(Clubs), 6(Hearts) => total=11
        riggedCards.add(new Card(Rank.FIVE, Suit.CLUBS));
        riggedCards.add(new Card(Rank.SIX, Suit.HEARTS));
        // Dealer => 9(Spades), 9(Diamonds) => total=18
        riggedCards.add(new Card(Rank.NINE, Suit.SPADES));
        riggedCards.add(new Card(Rank.NINE, Suit.DIAMONDS));
        // Next 2 hits for the player => 7, Queen => 7 => 18, then +10 => 28 => bust
        riggedCards.add(new Card(Rank.SEVEN, Suit.CLUBS));
        riggedCards.add(new Card(Rank.QUEEN, Suit.HEARTS));
        // leftover
        riggedCards.add(new Card(Rank.TWO, Suit.SPADES));

        Deck mockDeck = new MockDeck(riggedCards);

        // user input => "hit\nhit\nstand\nno\n"
        String input = "hit\nhit\nstand\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ConsoleIO io = new ConsoleIO();
        RoundManager manager = new RoundManager(mockDeck, io);

        Player player = new Player("Bust");
        player.setHitStrategy(new UserHitStrategy(io));
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        manager.playRound(List.of(player), dealer);

        // After 2 hits, the players final total => 28 => bust
        int finalScore = player.getHands().get(0).calculateScore();
        assertTrue(finalScore > 21, "Player should be bust with final score > 21 after two hits.");

        int finalCardCount = player.getHands().get(0).getCards().size();
        assertEquals(4, finalCardCount, "Player took 2 hits => total 4 cards in final hand.");
    }

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
