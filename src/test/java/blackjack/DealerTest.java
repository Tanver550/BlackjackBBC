package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {

    // create a dealer and make sure the dealer has no cards in the beginning
    @Test
    public void testDealerInitialisation() {
        Dealer dealer = new Dealer();
        assertEquals("Dealer", dealer.getName());
        assertEquals(0, dealer.getHand().getCards().size());
    }


    // test to see if the dealer can add cards to the hand
    @Test
    void testAddCardsToHand() {
        Dealer dealer = new Dealer();
        assertEquals(0, dealer.getHand().getCards().size(), " Dealer should have no cards in hand");

        Card card1 = new Card(Rank.KING, Suit.HEARTS); // value = 10
        dealer.addCard(card1);

        assertEquals(1, dealer.getHand().getCards().size(), "Dealer should now have 1 card in hand");
        assertEquals(10, dealer.getScore(), "Dealer score should be 10 (KING)");

        Card card2 = new Card(Rank.SEVEN, Suit.CLUBS); // value = 7
        dealer.addCard(card2);

        assertEquals(2, dealer.getHand().getCards().size(), "Dealer should now have 2 cards in hand");
        assertEquals(17, dealer.getScore(), "Dealer score should now be 17 (KING=10 + SEVEN=7)");
    }

    // test to see if the dealer can go bust once they go over 21
    @Test
    void testDealerBust() {
        Dealer dealer = new Dealer();
        // 10 + 10 + 5 = 25, meaning dealer goes bust
        dealer.addCard(new Card(Rank.KING, Suit.HEARTS));
        dealer.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        dealer.addCard(new Card(Rank.FIVE, Suit.DIAMONDS));

        assertTrue(dealer.getScore() > 21, "Dealer should be bust with a total > 21.");
    }

    // test to see if the dealer will keep hitting until score is 17
    @Test
    void testDealerHitStrategyBelow17() {
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // Add cards summing to 14
        dealer.addCard(new Card(Rank.NINE, Suit.HEARTS));
        dealer.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        // Because Dealer extends Player call dealer.shouldHit(dealer)
        boolean hitDecision = dealer.shouldHit(dealer);
        assertTrue(hitDecision, "Dealer should hit if under 17 (score is 14).");
    }

    // test to see if the dealer stops hitting once the score is 17
    @Test
    void testDealerHitStrategyAt17OrAbove() {
        Dealer dealer = new Dealer();
        dealer.setHitStrategy(new DealerHitStrategy());

        // 10 + 7 = 17
        dealer.addCard(new Card(Rank.TEN, Suit.CLUBS));
        dealer.addCard(new Card(Rank.SEVEN, Suit.HEARTS));

        // attempt to hit again
        boolean hitDecision = dealer.shouldHit(dealer);
        assertFalse(hitDecision, "Dealer should NOT hit at 17 or above.");
    }

}
