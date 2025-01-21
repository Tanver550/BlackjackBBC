package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {

    // 1) Ensure Dealer is initialised correctly
    @Test
    public void testDealerInitialisation() {
        Dealer dealer = new Dealer();
        assertEquals("Dealer", dealer.getName());
        // Check first hand is empty (no cards)
        assertEquals(0, dealer.getHands().get(0).getCards().size(), "Dealer's first hand should have 0 cards initially.");
    }

    // 2) Test adding cards to the Dealers first hand
    @Test
    void testAddCardsToHand() {
        Dealer dealer = new Dealer();
        assertEquals(0, dealer.getHands().get(0).getCards().size(), "Dealer should have no cards initially.");

        Card card1 = new Card(Rank.KING, Suit.HEARTS); // value = 10
        dealer.addCard(card1); // goes to the first hand by default

        // Now the first hand should have 1 card
        assertEquals(1, dealer.getHands().get(0).getCards().size(), "Dealer's first hand should have 1 card after adding.");
        assertEquals(10, dealer.getScore(), "Dealer score should be 10 (KING).");

        Card card2 = new Card(Rank.SEVEN, Suit.CLUBS); // value = 7
        dealer.addCard(card2);

        // First hand should now have 2 cards
        assertEquals(2, dealer.getHands().get(0).getCards().size(), "Dealer's first hand should have 2 cards now.");
        assertEquals(17, dealer.getScore(), "Dealer score should be 17 (10 + 7).");
    }

    // 3) Test that the dealer can bust (>21)
    @Test
    void testDealerBust() {
        Dealer dealer = new Dealer();
        //  10 + 10 + 5 = 25
        dealer.addCard(new Card(Rank.KING, Suit.HEARTS));
        dealer.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        dealer.addCard(new Card(Rank.FIVE, Suit.DIAMONDS));

        // getScore() should now be > 21
        assertTrue(dealer.getScore() > 21, "Dealer should bust with a total > 21.");
    }

    // 4) Test that DealerHitStrategy => dealer hits if under 17
    @Test
    void testDealerHitStrategyBelow17() {
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // 9 + 5 = 14, so dealer should hit
        dealer.addCard(new Card(Rank.NINE, Suit.HEARTS));
        dealer.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        boolean hitDecision = dealer.shouldHit(dealer);
        assertTrue(hitDecision, "Dealer should hit if under 17 (score is 14).");
    }

    // 5) Test that Dealer does NOT hit at 17 or above
    @Test
    void testDealerHitStrategyAt17OrAbove() {
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // 10 + 7 = 17
        dealer.addCard(new Card(Rank.TEN, Suit.CLUBS));
        dealer.addCard(new Card(Rank.SEVEN, Suit.HEARTS));

        boolean hitDecision = dealer.shouldHit(dealer);
        assertFalse(hitDecision, "Dealer should NOT hit at 17 or above.");
    }
}
