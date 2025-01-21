package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
deck testing. this is for the deck from which cards are handed out. makes sure the deck is correct so scores are legitamate
 */

public class DeckTest {

    //deck should always have 52 in it
    @Test
    void testDeckSize() {
        Deck deck = new Deck(1);
        assertEquals(52, deck.cardsLeft());
    }

    //shuffle does not chnage the size
    @Test
    void testShuffleDoesNotChangeSize() {
        Deck deck = new Deck(1);
        deck.shuffle();
        assertEquals(52, deck.cardsLeft());
    }

    @Test
    void testDealCard() {
        Deck deck = new Deck(1);
        Card card = deck.dealCard();
        assertNotNull(card);
        assertEquals(51, deck.cardsLeft());
    }

    //trying to deal from an empty deck is not allowed
    @Test
    void testEmptyDeckThrowsException() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.dealCard();
        }
        assertThrows(IllegalStateException.class, deck::dealCard);
    }
}
