package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
this is for each individual card, make sures its properities are assignmed correctly
 */

public class CardTest {
    //see if a card can be made
    @Test
    void testCardCreation() {

        Card card = new Card(Rank.ACE, Suit.SPADES);

        // Check rank suit and initial value
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.SPADES, card.getSuit());
        assertEquals(11, card.getValue());
    }

    // correctly make a string, with the card deatails
    @Test
    void testCardToString() {
        Card card = new Card(Rank.KING, Suit.HEARTS);

        assertEquals("KING of HEARTS", card.toString());
    }
}
