package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
this is for each individual card, make sures its properities are assignmed correctly
 */

public class CardTest {
    @Test
    void testCardCreation() {
        Card card = new Card("Ace", "Spades", 11);
        assertEquals("Ace", card.getRank());
        assertEquals("Spades", card.getSuit());
        assertEquals(11, card.getValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card("King", "Hearts", 10);
        assertEquals("King of Hearts", card.toString());
    }
}
