package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    void testDeckSize() {
        Deck deck = new Deck();
        assertEquals(52, deck.cardsLeft());
    }

    @Test
    void testShuffleDoesNotChangeSize() {
        Deck deck = new Deck();
        deck.shuffle();
        assertEquals(52, deck.cardsLeft());
    }

    @Test
    void testDealCard() {
        Deck deck = new Deck();
        Card card = deck.dealCard();
        assertNotNull(card);
        assertEquals(51, deck.cardsLeft());
    }

    @Test
    void testEmptyDeckThrowsException() {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }
        assertThrows(IllegalStateException.class, deck::dealCard);
    }
}
